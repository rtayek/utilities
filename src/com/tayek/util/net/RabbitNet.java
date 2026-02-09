package com.tayek.util.net;
import static com.tayek.util.io.Print.p;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashSet;
import java.util.Set;
import com.tayek.util.core.Et;
import com.tayek.util.core.Pair;
import com.tayek.util.exec.Exec;
import static com.tayek.util.net.Net.*;
public class RabbitNet {
	public static Set<Pair<Integer,SocketAddress>> discover(boolean real,int n,int service) {
		Set<Pair<Integer,SocketAddress>> socketAddresses=new LinkedHashSet<>();
		Set<Pair<Integer,SocketAddress>> good=new LinkedHashSet<>();
		if(real) {
			for(int i=11;i<11+n;i++) // fragile!
				socketAddresses.add(new Pair<Integer,SocketAddress>(i-10,new InetSocketAddress(tabletRouterPrefix+i,service)));
		} else {
			for(int i=1;i<=n;i++)
				socketAddresses.add(new Pair<Integer,SocketAddress>(i,new InetSocketAddress(defaultHost,service+i)));
			for(int i=1;i<=n;i++)
				socketAddresses.add(new Pair<Integer,SocketAddress>(i,new InetSocketAddress(testingHost,service+i)));
		}
		int retries=3;
		for(Pair<Integer,SocketAddress> pair:socketAddresses) {
			p("trying : "+pair);
			for(int i=1;i<=1+retries;i++) {
				Socket socket=connect(pair.second,real?1_000:200);
				if(socket!=null) {
					try {
						socket.close();
					} catch(IOException e) {
						p("caught: "+e);
						e.printStackTrace();
					}
					p("adding: "+pair);
					if(good.contains(pair)) p(good+" already contains: "+pair);
					good.add(pair);
					break;
				}
			}
		}
		return good;
	}
	public static String aTabletId(Integer tabletId) {
		return "T"+tabletId;
	}
	public static Set<Pair<Integer,SocketAddress>> discoverTestTablets(int n,int serviceBase) {
		return discover(false,n,serviceBase);
	}
	public static Set<Pair<Integer,SocketAddress>> discoverRealTablets(int n) {
		return discover(true,n,defaultReceivePort);
	}
	public static void main(String args[]) throws UnknownHostException {
		final Et et=new Et();
		printNetworkInterfaces();
		InetAddress localHost=InetAddress.getLocalHost();
		p("local: "+localHost);
		String host=localHost.getHostName();
		p("host: "+host);
		InetAddress inetAddress=InetAddress.getByName(host);
		p("address: "+inetAddress);
		printInetAddresses(networkStub);
		printInetAddresses(tabletRouterPrefix);
		printInetAddresses(defaultHost);
		if(!defaultHost.equals(testingHost)) printInetAddresses(testingHost);
		Set<InetAddress> inetAddresses=addressesWith(tabletRouterPrefix);
		p("addresses on: "+tabletRouterPrefix+" are: "+inetAddresses);
		if(!inetAddresses.contains(InetAddress.getByName(raysPcOnTabletNetworkToday)))
			p("address has changed, expected: "+raysPcOnTabletNetworkToday+", but got: "+inetAddresses);
		inetAddresses=addressesWith(raysRouterPrefix);
		p("addresses on: "+raysRouterPrefix+" are: "+inetAddresses);
		if(!inetAddresses.contains(InetAddress.getByName(raysPcOnRaysNetwork)))
			p("address has changed, expected: "+raysPcOnTabletNetworkToday+", but got: "+inetAddresses);
		p(raysRouter+" "+canConnect(raysRouter,80,1_000));
		p(tabletRouter+" "+canConnect(tabletRouter,80,1_000));
		p("ping at: "+et);
		int rc=Exec.exec("ping "+tabletRouter);
		p("ping returns: "+rc+" at: "+et);
		p("exec ping at: "+et);
		rc=Exec.ping(tabletRouter);
		p("exec ping returns: "+rc+" at: "+et);
		new Thread(new Runnable() {
			@Override public void run() {
				p("ping in thread at: "+et);
				int rc=Exec.exec("ping "+tabletRouter);
				p("ping in thread returns: "+rc+" at: "+et);
			}
		}).start();
		new Thread(new Runnable() {
			@Override public void run() {
				p("exec ping in thread at: "+et);
				int rc=Exec.ping(tabletRouter);
				p("exec ping in thread returns: "+rc+" at: "+et);
			}
		}).start();
	}
															// router on my pc
}
