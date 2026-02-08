package com.tayek.util.io;
import static org.junit.Assert.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.*;
import org.junit.rules.TestRule;
import com.tayek.util.log.LoggingHandler;
import com.tayek.util.net.Net;
import com.tayek.util.junit.BasicTestWatcher;
import com.tayek.util.concurrent.Threads;
import static com.tayek.util.io.Print.*;
public class IOTestCase {
    @Rule public TestRule watcher=new BasicTestWatcher();

    @BeforeClass public static void setUpBeforeClass() throws Exception {
        LoggingHandler.init();
    }
    @AfterClass public static void tearDownAfterClass() throws Exception {}
    @Before public void setUp() throws Exception {
        p("get localhost: "+InetAddress.getLocalHost());
        host=InetAddress.getLocalHost().getHostName();
        p("host: "+host);
    }
    @After public void tearDown() throws Exception {
        Threads.printThreads();
    }
    @Test public void testNormalStuff() throws UnknownHostException {
        InetAddress[] x=InetAddress.getAllByName(host);
        p(host+" all by name: "+Arrays.asList(x));
        p(host+" get by name: "+InetAddress.getByName(host));
    }
    @Test public void testGetByName() throws InterruptedException,ExecutionException {
        Net.GetByNameCallable task=new Net.GetByNameCallable(Net.testingHost);
        task.run();
        InetAddress inetAddress=task.inetAddress;
        assertTrue(inetAddress.getHostAddress().contains(Net.testingHost));
    }
    @Test public void testGetNetworkInterfacesWithHost() throws InterruptedException,ExecutionException {
        Net.AddressesWithCallable addressesWithCallable=new Net.AddressesWithCallable(Net.testingHost);
        addressesWithCallable.run();
        Set<InetAddress> inetAddresses=addressesWithCallable.addresses;
        assertTrue(inetAddresses.size()>0);
        if(inetAddresses.size()>1) p("more than one nic: "+inetAddresses);
        InetAddress inetAddress=inetAddresses.iterator().next();
        assertTrue(inetAddress.getHostAddress().contains(Net.testingHost));
    }
    @Test public void testGetNetworkInterfacesWithNetworkPrefix() throws InterruptedException,ExecutionException {
        // you will need a wireless nic or be able to plug in to the real network for this to work
        Net.AddressesWithCallable addressesWithCallable=new Net.AddressesWithCallable(Net.testingHost);
        addressesWithCallable.run();
        Set<InetAddress> inetAddresses=addressesWithCallable.addresses;
        assertTrue(inetAddresses.size()>0);
        if(inetAddresses.size()>1) p("more than one nic: "+inetAddresses);
        InetAddress inetAddress=inetAddresses.iterator().next();
        assertTrue(inetAddress!=null);
        p("checking: "+inetAddress+" for: "+Net.tabletRouterPrefix );
        // this is failing, looks like i may have changed testing host from 0 network.
        assertTrue(inetAddress.getHostAddress().contains(Net.tabletRouterPrefix));
    }
    @Test public void testGetNetworkInterfacesWithNetworkPrefix192dot168() throws InterruptedException,ExecutionException {
        // you will need a wireless nic or be able to plug in to the real network for this to work
        Net.AddressesWithCallable addressesWithCallable=new Net.AddressesWithCallable(Net.testingHost);
        addressesWithCallable.run();
        Set<InetAddress> inetAddresses=addressesWithCallable.addresses;
        assertTrue(inetAddresses.size()>0);
        if(inetAddresses.size()>1) p("more than one nic: "+inetAddresses);
        boolean foundOne=false;
        // this is also failing, looks like i may have changed testing host from 0 network.
        for(InetAddress inetAddress:inetAddresses)
            if(inetAddress.getHostAddress().contains(Net.tabletRouterPrefix)) foundOne=true;
        assertTrue(foundOne);
    }
    String host;
}


