package com.tayek.util.net;
import static org.junit.Assert.*;
import java.io.IOException;
import java.net.ServerSocket;
import org.junit.Test;
public class NetTestCase {
    @Test public void testCanConnectUsesTheGivenPort() throws IOException {
        try(ServerSocket serverSocket=new ServerSocket(0)) {
            assertTrue(Net.canConnect("localhost",serverSocket.getLocalPort(),1_000));
        }
    }
    @Test public void testCanNotConnectToAClosedPort() throws IOException {
        int port;
        try(ServerSocket serverSocket=new ServerSocket(0)) {
            port=serverSocket.getLocalPort();
        }
        assertFalse(Net.canConnect("localhost",port,1_000));
    }
}
