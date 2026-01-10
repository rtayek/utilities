package p;
import java.io.*;
import java.net.*;
import org.apache.commons.logging.*;
public class Utilities {
	public static Class getClass_() {
		StackTraceElement[] e = new Exception().getStackTrace();
		System.out.println("--------------");
		for (StackTraceElement stackTraceElement : e)
			System.out.println(stackTraceElement);
		System.out.println("--------------");
		Class temp = null;
		try {
			temp = Class.forName(e[1].getClassName());
			System.out.println(temp);
		} catch (ClassNotFoundException e1) {
			throw new RuntimeException("strange");
		}
		return temp;
	}
	// make this return the packet or null?
	public static DatagramPacket receive(final DatagramSocket socket) {
		final byte[] buffer = new byte[256];
		final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			staticLog.error(e);
			return null; // means an error occurred. oops, can a packet be null?
		}
		return packet;
	}
	public static boolean send(final DatagramSocket datagramSocket, final DatagramPacket received, String response) {
		return send(datagramSocket, received.getAddress(), received.getPort(), response);
	}
	public static boolean send(final DatagramSocket datagramSocket, final InetAddress address, final int port,
			String response) {
		final byte[] buffer = response.getBytes();
		final DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, address, port);
		try {
			datagramSocket.send(datagramPacket);
			return true;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void shutdown(final Thread thread) {
		if (thread != null) {
			staticLog.info("shutting down: " + thread + ", state=" + thread.getState());
			thread.interrupt();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				staticLog.info(Thread.currentThread().getName() + " was interrupted trying to sleep after interrupting"
						+ thread.getName() + " " + e);
			}
			staticLog.info("after interrupting: " + thread + ", state=" + thread.getState());
			try {
				thread.join();
				staticLog.info("shut down: " + thread);
			} catch (InterruptedException e) {
				staticLog.info(Thread.currentThread().getName() + " join interruped");
			}
		}
	}
	static {
		System.out.println("Utilities");
	}

	private static final Log staticLog = LogFactory.getLog(getClass_());
}
