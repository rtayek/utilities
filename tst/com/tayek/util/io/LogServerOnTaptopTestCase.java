package com.tayek.util.io;

import java.io.IOException;
import java.util.logging.*;
import org.junit.*;
public class LogServerOnTaptopTestCase {
    private static final Logger logger=Logger.getLogger(LogServerOnTaptopTestCase.class.getName());
    @BeforeClass public static void setUpBeforeClass() throws Exception {}
    @AfterClass public static void tearDownAfterClass() throws Exception {}
    @Before public void setUp() throws Exception {}
    @After public void tearDown() throws Exception {}
    static void startSocketHandler(String host,Integer service) throws InterruptedException {
        try {
            SocketHandler socketHandler=new SocketHandler(host,service);
            Thread.sleep(1000);
            // socketHandler.setFormatter(new LoggingHandler());
            logger.info("got socket handler on: "+host+":"+service);
            socketHandler.setLevel(Level.ALL);
            logger.info("got socket handler: "+socketHandler+" to: "+host+":"+service);
            logger.addHandler(socketHandler);
            Logger global=Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
            global.addHandler(socketHandler);
            global.warning("global with socket handler.");
        } catch(IOException e) {
            logger.info("caught: '"+e+"' constructing socket handler on: "+host+":"+service);
        }
    }
    @Test public void test() throws InterruptedException {
        startSocketHandler("localhost",5000);
    }
}
