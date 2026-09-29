package com.tayek.util.exec;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.Assume;
import org.junit.Test;
public class ExecOutputTestCase {
    static String java() {
        return ProcessHandle.current().info().command().orElse("java");
    }
    @Test(timeout=60_000) public void testLargeOutputDoesNotHang() throws IOException {
        // prints far more than any pipe buffer holds, on both stdout and stderr.
        File source=File.createTempFile("Big",".java");
        source.deleteOnExit();
        Files.writeString(source.toPath(),"""
                public class Big {
                    public static void main(String[] args) {
                        String line="x".repeat(99);
                        for(int i=0;i<2_000;i++) { System.out.println(line); System.err.println(line); }
                    }
                }
                """);
        Exec exec=new Exec(new String[] {java(),source.getPath()}).run();
        assertEquals(0,exec.rc());
        assertEquals(2_000,exec.output().lines().count());
        assertEquals(2_000,exec.error().lines().count());
    }
    @Test(timeout=60_000) public void testStringConstructorSplitsArguments() {
        String java=java();
        Assume.assumeFalse("java path has a space",java.contains(" "));
        Exec exec=new Exec(java+" -version").run();
        assertEquals(0,exec.rc());
        assertTrue(exec.error().contains("version"));
    }
}
