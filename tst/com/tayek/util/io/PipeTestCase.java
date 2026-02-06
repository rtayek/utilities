package com.tayek.util.io;
import static org.junit.Assert.assertEquals;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;
public class PipeTestCase {
    static String writeFlushRead(BufferedReader in,Writer out,String string) throws IOException {
        out.write(string+'\n');
        out.flush();
        return in.readLine();
    }
    @Test public void testWriteReadOnPipe() throws IOException {
        Pipe pipe=new Pipe();
        String expected="message";
        String actual=writeFlushRead(pipe.in,pipe.out,expected);
        assertEquals(expected,actual);
    }
    @Test public void testWriteAndReadOnDifferentThreads() throws Exception {
        Pipe pipe=new Pipe();
        String expected="Hello";
        CountDownLatch done=new CountDownLatch(2);
        AtomicReference<Throwable> error=new AtomicReference<>();
        Thread writer=new Thread(() -> {
            try {
                pipe.out.write(expected);
                pipe.out.write("\n");
                pipe.out.flush();
            } catch(Throwable t) {
                error.set(t);
            } finally {
                done.countDown();
            }
        });
        Thread reader=new Thread(() -> {
            try {
                String actual=pipe.in.readLine();
                assertEquals(expected,actual);
            } catch(Throwable t) {
                error.set(t);
            } finally {
                done.countDown();
            }
        });
        writer.start();
        reader.start();
        done.await(2,TimeUnit.SECONDS);
        if(error.get()!=null) throw new AssertionError(error.get());
    }
    @Test public void testCrossWiring() throws Exception {
        Duplex duplex=new Duplex();
        duplex.front.out().write("foo\n");
        duplex.front.out().flush();
        String s1=duplex.back.in().readLine();
        assertEquals("foo",s1);
        duplex.back.out().write("bar\n");
        duplex.back.out().flush();
        String s2=duplex.front.in().readLine();
        assertEquals("bar",s2);
    }
    @Test public void testCrossWiring2() throws IOException {
        Duplex duplex=new Duplex();
        String expected="message";
        String actual=writeFlushRead(duplex.back.in(),duplex.front.out(),expected);
        assertEquals(expected,actual);
        String actual2=writeFlushRead(duplex.front.in(),duplex.back.out(),expected);
        assertEquals(expected,actual2);
    }
    void writeFlushRunRead(Copy frontEnd1,Copy frontEnd2) throws IOException {
        frontEnd1.out.write("foo\n");
        frontEnd1.out.flush();
        frontEnd2.run(true,false);
        String s1=frontEnd1.in.readLine();
        assertEquals("foo",s1);
    }
    @Test public void testwriteFlushRunRead() throws Exception {
        Duplex duplex=new Duplex();
        Copy frontEnd1=new Copy(duplex.front.in(),duplex.front.out());
        frontEnd1.name="1";
        Copy frontEnd2=new Copy(duplex.back.in(),duplex.back.out());
        frontEnd2.name="2";
        writeFlushRunRead(frontEnd1,frontEnd2);
    }
    @Test public void testwriteFlushRunRead2() throws Exception {
        Duplex duplex=new Duplex();
        Copy frontEnd1=new Copy(duplex.front.in(),duplex.front.out());
        frontEnd1.name="1";
        Copy frontEnd2=new Copy(duplex.back.in(),duplex.back.out());
        frontEnd2.name="2";
        writeFlushRunRead(frontEnd2,frontEnd1);
    }
}
