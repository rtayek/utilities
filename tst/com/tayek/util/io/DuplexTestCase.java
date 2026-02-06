package com.tayek.util.io;
import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class DuplexTestCase {
    @Before public void setUp() throws Exception {}
    @After public void tearDown() throws Exception {}
    @Test public void testCreatTwoEnds() throws Exception {
        new AnEndpoint(duplex.front);
        new AnEndpoint(duplex.back);
    }
    @Test public void testReadAndwrite2() throws Exception {
        AnEndpoint frontEnd1=new AnEndpoint(duplex.front);
        AnEndpoint frontEnd2=new AnEndpoint(duplex.back);
        frontEnd1.out.write("foo\n");
        frontEnd1.out.flush();
        Thread thread2=new Thread(frontEnd2);
        thread2.start();
        String s1=frontEnd1.in.readLine();
        assertEquals("foo",s1);
    }
    void writeFlushRead(AnEndpoint frontEnd1) throws IOException {
        frontEnd1.out.write("foo\n");
        frontEnd1.out.flush();
        String s1=frontEnd1.in.readLine();
        assertEquals("foo",s1);
    }
    @Test public void test1() throws Exception {
        AnEndpoint frontEnd1=new AnEndpoint(duplex.front); // crosswired
        AnEndpoint frontEnd2=new AnEndpoint(duplex.back); // crosswired
        Thread thread2=new Thread(frontEnd2);
        thread2.start();
        writeFlushRead(frontEnd1);
    }
    @Test public void testFront12() throws Exception {
        AnEndpoint frontEnd1=new AnEndpoint(duplex.front); // crosswired
        AnEndpoint frontEnd2=new AnEndpoint(duplex.back); // crosswired
        frontEnd1.out.write("foo\n");
        frontEnd1.out.flush();
        Thread thread2=new Thread(frontEnd2);
        thread2.start();
        String s1=frontEnd1.in.readLine();
        assertEquals("foo",s1);
    }
    @Test public void testFront21() throws Exception {
        AnEndpoint frontEnd1=new AnEndpoint(duplex.front); // crosswired
        AnEndpoint frontEnd2=new AnEndpoint(duplex.back); // crosswired
        frontEnd2.out.write("bar\n");
        frontEnd2.out.flush();
        Thread thread1=new Thread(frontEnd1);
        thread1.start();
        String s2=frontEnd2.in.readLine();
        assertEquals("bar",s2);
    }
    final long timeout=0;
    Duplex duplex=new Duplex();
}
