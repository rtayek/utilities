package com.tayek.util.net;
import static com.tayek.util.io.Print.*;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.logging.Logger;
import com.tayek.util.core.Et;
public class Net {
    public static String toString(ServerSocket serverSocket) {
        return serverSocket+": "+serverSocket.isBound()+" "+serverSocket.isClosed();
    }
    public static class ShutdownOptions {
        public boolean shutdownInput,shutdownOutput,closeInput,closeOutput,closeSocket=true;
    }
    public static class GetByNameCallable implements Runnable,java.util.concurrent.Callable<java.net.InetAddress> {
        public GetByNameCallable(String host) {
            this.host=host;
        }
        @Override public void run() {
            Thread.currentThread().setName(getClass().getName());
            try {
                inetAddress=InetAddress.getByName(host);
            } catch(UnknownHostException e) {}
        }
        @Override public InetAddress call() throws Exception {
            run();
            return inetAddress;
        }
        final String host;
        public InetAddress inetAddress;
    }
    public static Set<InetAddress> addressesWith(String networkPrefix) {
        Set<InetAddress> set=new LinkedHashSet<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces=NetworkInterface.getNetworkInterfaces();
            for(NetworkInterface networkInterface:Collections.list(networkInterfaces))
                for(InetAddress inetAddress:Collections.list(networkInterface.getInetAddresses()))
                    if(inetAddress.isSiteLocalAddress()&&inetAddress.getHostAddress().contains(networkPrefix)) set.add(inetAddress);
        } catch(SocketException e) {
            p("caught: "+e);
            e.printStackTrace();
        }
        return set;
    }
    public static InetAddress addressWith(String networkPrefix) {
        Set<InetAddress> inetAddresses=addressesWith(networkPrefix);
        if(inetAddresses.size()>1) logger.severe("more than one inetAddress: "+inetAddresses);
        return inetAddresses.size()>0?inetAddresses.iterator().next():null;
    }
    public static class AddressesWithCallable implements Runnable,java.util.concurrent.Callable<java.util.Set<java.net.InetAddress>> {
        public AddressesWithCallable(String networkPrefix) {
            this.networkPrefix=networkPrefix;
        }
        @Override public void run() {
            addresses=addressesWith(networkPrefix);
        }
        @Override public Set<InetAddress> call() throws Exception {
            Thread.currentThread().setName(getClass().getName());
            run();
            return addresses;
        }
        final String networkPrefix;
        public Set<InetAddress> addresses;
    }
    static void printNetworkInterface(NetworkInterface netint) {
        p("Display name: "+netint.getDisplayName()+", Name: "+netint.getName());
        Enumeration<InetAddress> inetAddresses=netint.getInetAddresses();
        for(InetAddress inetAddress:Collections.list(inetAddresses))
            p("\tInetAddress: "+inetAddress+" "+inetAddress.isSiteLocalAddress());
    }
    public static void printNetworkInterfaces() {
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces=NetworkInterface.getNetworkInterfaces();
            for(NetworkInterface networkInterface:Collections.list(networkInterfaces))
                printNetworkInterface(networkInterface);
        } catch(SocketException e) {
            p("ni caught: '"+e+"'");
        }
    }
    public static void printInetAddresses(String prefix) {
        Set<InetAddress> inetAddresses=addressesWith(prefix);
        p("addresses starting with: "+prefix+": "+inetAddresses);
    }
    public static ServerSocket serverSocket(SocketAddress socketAddress) {
        Et et=new Et();
        ServerSocket serverSocket=null;
        try {
            serverSocket=new ServerSocket();
            serverSocket.bind(socketAddress);
        } catch(BindException e) {
            e.printStackTrace();
            p("after: "+et+",  caught: '"+e+"'");
        } catch(IOException e) {
            e.printStackTrace();
            p("after: "+et+",  caught: '"+e+"'");
        } catch(Exception e) {
            e.printStackTrace();
            p("after: "+et+",  caught: '"+e+"'");
        }
        return serverSocket;
    }
    public static Socket silentConnect(SocketAddress socketAddress,int timeout) {
        Socket socket=new Socket();
        try {
            socket.connect(socketAddress,timeout);
            return socket;
        } catch(Exception e) {}
        return null;
    }
    public static Socket connect(SocketAddress socketAddress,int timeout) {
        Et et=new Et();
        Socket socket=new Socket();
        try {
            socket.connect(socketAddress,timeout);
            return socket;
        } catch(SocketTimeoutException e) {
            logger.warning(socketAddress+", after: "+et+", with timeout: "+timeout+", caught: '"+e+"'");
        } catch(IOException e) {
            logger.warning(socketAddress+", after: "+et+", with timeout: "+timeout+", caught: '"+e+"'");
        }
        return null;
    }
    public static boolean canConnect(String host,int service,int timeout) {
        boolean canConnect=false;
        InetSocketAddress inetSocketAddress=new InetSocketAddress(host,80);
        Socket socket=silentConnect(inetSocketAddress,timeout);
        if(socket!=null) {
            p("connected to router: "+host);
            canConnect=true;
            try {
                socket.close();
            } catch(IOException e) {}
        } else {
            p("can not connect to router: "+host);
            canConnect=false;
        }
        return canConnect;
    }
    public static final Logger logger=Logger.getLogger(Net.class.getName());
}
