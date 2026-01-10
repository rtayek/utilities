package com.tayek.io;
import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;
import java.util.logging.Logger;
import com.tayek.tablet.io.*;
import com.tayek.tablet.model.Message;
import com.tayek.tablet.model.Message.Sender;
public class IO {
    public static class ChatMessage {
        String text;
    }
    public interface EndPoint extends Runnable {
        @Override public void run();
        public void start(); // start a new thread and call run
        public void stop(); // closes this and causes run to return
        public void close(); // closes any sockets?
    }
    public static class TcpConnection {
        void accept(Socket socket) {
            this.socket=socket;
        }
        public void connect(SocketAddress remoteAddress) throws IOException {
            close();
            socket.connect(remoteAddress);
        }
        public void close() throws IOException {
            if(socket!=null) {
                socket.close();
                socket=null;
            }
        }
        int keepAliveMillis;
        int timeoutMillis;
        Socket socket;
    }
    public static class UdpConnection {
        public void bind(InetSocketAddress localPort) throws IOException {
            close();
            datagramSocket.bind(localPort);
        }
        public void connect(InetSocketAddress remoteAddress) throws IOException {
            close();
            // datagramSocket.bind(null);
            datagramSocket.connect(remoteAddress);
            connectedAddress=remoteAddress;
        }
        public void close() {
            connectedAddress=null;
            datagramSocket.close();
        }
        DatagramSocket datagramSocket; // (MulticastSocket in the clients case)
        InetSocketAddress connectedAddress;
        transient DatagramPacket packet;
    }
    public static class Listener {
        public void connected(Connection connection) {}
        public void disconnected(Connection connection) {}
        public void received(Connection connection,Object object) {}
        public void idle(Connection connection) {}
    }
    public static class Connection /*connection*/ { // rename to stream or pipe?
        protected Connection() {}
        void initialize() {
            tcp=new TcpConnection();
        }
        int id() {
            return id;
        }
        public void setTimeout(int timeoutMillis) {
            tcp.timeoutMillis=timeoutMillis;
        }
        public void setKeepAliveTCP(int keepAliveMillis) {
            tcp.keepAliveMillis=keepAliveMillis;
        }
        public void sendTCP(Object object) {}
        public void sendUDP(Object object) {}
        public void close() throws IOException {
            boolean wasConnected=isConnected;
            isConnected=false;
            tcp.close();
            if(udp!=null&&udp.connectedAddress!=null) udp.close();
            if(wasConnected) {
                ;// notifyDisconnected();
            }
            setConnected(false);
        }
        void setConnected(boolean isConnected) {
            this.isConnected=isConnected;
            if(isConnected&&name==null) name="Connection "+id;
        }
        public InetSocketAddress getRemoteAddressTCP() {
            if(tcp.socket!=null) return (InetSocketAddress)tcp.socket.getRemoteSocketAddress();
            return null;
        }
        public InetSocketAddress getRemoteAddressUDP() {
            if(udp.connectedAddress!=null) return udp.connectedAddress;
            return udpRemoteAddress;
        }
        public void addListener(Listener listener) {
            if(listener==null) throw new IllegalArgumentException("listener cannot be null.");
            synchronized(listenerLock) {
                Listener[] listeners=this.listeners;
                int n=listeners.length;
                for(int i=0;i<n;i++)
                    if(listener==listeners[i]) return;
                Listener[] newListeners=new Listener[n+1];
                newListeners[0]=listener;
                System.arraycopy(listeners,0,newListeners,1,n);
                this.listeners=newListeners;
            }
        }
        public void removeListener(Listener listener) {
            if(listener==null) throw new IllegalArgumentException("listener cannot be null.");
            synchronized(listenerLock) {
                Listener[] listeners=this.listeners;
                int n=listeners.length;
                if(n==0) return;
                Listener[] newListeners=new Listener[n-1];
                for(int i=0,ii=0;i<n;i++) {
                    Listener copyListener=listeners[i];
                    if(listener==copyListener) continue;
                    if(ii==n-1) return;
                    newListeners[ii++]=copyListener;
                }
                this.listeners=newListeners;
            }
        }
        int id=-1;
        private Object listenerLock=new Object();
        private Listener[] listeners= {};
        String name;
        EndPoint endPoint;
        UdpConnection udp;
        TcpConnection tcp;
        InetSocketAddress udpRemoteAddress;
        volatile boolean isConnected;
    }
    public static class Client extends Connection implements EndPoint {
        @Override public void run() {}
        @Override public void start() {}
        @Override public void stop() {}
        @Override public void close() {}
        public void addListener(Listener listener) {
            super.addListener(listener);
        }
        public void removeListener(Listener listener) {
            super.removeListener(listener);
        }
        private boolean isClosed;
        private volatile boolean shutdown;
        private int connectTimeout;
        private InetAddress connectHost;
        private int connectTcpPort;
        private int connectUdpPort;
    }
    public class Server implements EndPoint {
        protected Connection newConnection() {
            return new Connection();
        }
        public void bind(int tcpPort,int udpPort) throws IOException {
            bind(new InetSocketAddress(tcpPort),new InetSocketAddress(udpPort));
        }
        public void bind(InetSocketAddress tcpPort,InetSocketAddress udpPort) throws IOException {
            close();
            // synchronized (updateLock) {
            serverSocket.bind(tcpPort);
            if(udpPort!=null) {
                udp=new UdpConnection();
                udp.bind(udpPort);
            }
        }
        private void addConnection(Connection connection) {
            Connection[] newConnections=new Connection[connections.length+1];
            newConnections[0]=connection;
            System.arraycopy(connections,0,newConnections,1,connections.length);
            connections=newConnections;
        }
        void removeConnection(Connection connection) {
            ArrayList<Connection> temp=new ArrayList(Arrays.asList(connections));
            temp.remove(connection);
            connections=temp.toArray(new Connection[temp.size()]);
            // pendingConnections.remove(connection.id);
        }
        // BOZO - Provide mechanism for sending to multiple clients without
        // serializing multiple times.
        public void sendToAllTCP(Object object) {
            Connection[] connections=this.connections;
            for(int i=0,n=connections.length;i<n;i++) {
                Connection connection=connections[i];
                connection.sendTCP(object);
            }
        }
        public void sendToAllExceptTCP(int connectionID,Object object) {
            Connection[] connections=this.connections;
            for(int i=0,n=connections.length;i<n;i++) {
                Connection connection=connections[i];
                if(connection.id!=connectionID) connection.sendTCP(object);
            }
        }
        public void sendToTCP(int connectionID,Object object) {
            Connection[] connections=this.connections;
            for(int i=0,n=connections.length;i<n;i++) {
                Connection connection=connections[i];
                if(connection.id==connectionID) {
                    connection.sendTCP(object);
                    break;
                }
            }
        }
        public void sendToAllUDP(Object object) {
            Connection[] connections=this.connections;
            for(int i=0,n=connections.length;i<n;i++) {
                Connection connection=connections[i];
                connection.sendUDP(object);
            }
        }
        public void sendToAllExceptUDP(int connectionID,Object object) {
            Connection[] connections=this.connections;
            for(int i=0,n=connections.length;i<n;i++) {
                Connection connection=connections[i];
                if(connection.id!=connectionID) connection.sendUDP(object);
            }
        }
        public void sendToUDP(int connectionID,Object object) {
            Connection[] connections=this.connections;
            for(int i=0,n=connections.length;i<n;i++) {
                Connection connection=connections[i];
                if(connection.id==connectionID) {
                    connection.sendUDP(object);
                    break;
                }
            }
        }
        @Override public void run() {}
        @Override public void start() {}
        @Override public void stop() {}
        @Override public void close() {}
        UdpConnection udp;
        Connection[] connections= {};
        ServerSocket serverSocket;
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }
    public static class MyConnection implements Runnable { // my use in tablet
                                                           // program
        // so ignore for a while. the one here will not have a sender
        // this will probably be a sender?
        // looks like no one uses this?
        public MyConnection(Sender sender,int sourceTabletId,Socket socket) throws IOException {
            this.sender=sender;
            this.sourceTabletId=sourceTabletId;
            this.socket=socket;
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new OutputStreamWriter(socket.getOutputStream());
            thread=new Thread(this,sender+"<-"+sourceTabletId);
        }
        public void startThread() {
            System.out.println("starting connection thread: "+this);
            thread.start();
        }
        public void stopConnection() {
            shuttingDown=true;
            try {
                socket.close();
            } catch(IOException e) {
                System.out.println("caught6: "+e);
            }
            thread.interrupt();
            Thread t=thread;
            if(t.isAlive()) {
                if(t.isInterrupted()) Message.logger.info(t+" is interrupted");
                else Message.logger.info(t+" is not interrupted");
                t.interrupt();
            }
            try {
                t.join();
            } catch(InterruptedException e) {
                System.out.println("caught11: "+e);
            }
        }
        @Override public void run() {
            while(true)
                try {
                    String string=in.readLine();
                    logger.fine(this+" received: "+string);
                    if(string!=null) {
                        received++;
                        if(string.contains("\n")) throw new RuntimeException("oops");
                        Message message=Message.from(string);
                        // check for group?
                        String response="ok";
                        // TcpServer.send(response,out); // maybe do not always
                        // do
                        // this?
                        System.out.println(sender+" seting source to: "+message.tabletId+" in connection.");
                        sourceTabletId=message.tabletId;
                        if(!once) {
                            System.out.println(message+" from: "+message.tabletId);
                            // check the id, a bogus value was getting in!
                            thread.setName(sender+"<-"+sourceTabletId);
                            // Connection old=sender.addConnection(this);
                            once=true;
                        }
                        if(sender.receiver()!=null) sender.receiver().receive(message);
                    } else {
                        socket.shutdownInput();
                        socket.shutdownOutput();
                        socket.close();
                        // throw new RuntimeException();
                        break;
                    }
                } catch(IOException e) { // check shutting down, but it's far
                                         // away
                                         // :(
                    if(shuttingDown) {
                        break;
                    } else throw new RuntimeException(e);
                }
        }
        @Override public String toString() {
            return sender.tabletId()+"<-"+sourceTabletId+":"+received;
        }
        private final Sender sender;
        private boolean once;
        boolean shuttingDown;
        /*final*/ int sourceTabletId;
        private final Thread thread;
        private final Socket socket;
        private final BufferedReader in;
        private final Writer out;
        private int received;
        Logger logger=Logger.getLogger(getClass().getName());
    }
}
