package tayek.easy.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.*;

import p.shared.Delimiters;
import gnu.io.*;

public class SerialPortUtilities {
	static String receive(final DatagramSocket socket) throws IOException {
		final byte[] buf2 = new byte[256];
		final DatagramPacket packet = new DatagramPacket(buf2, buf2.length);
		socket.receive(packet);
		// display response
		final String received = new String(packet.getData(), packet.getOffset(), packet.getLength());
		return received;
	}

	static void send(final DatagramSocket socket, final int port, final String request) throws UnknownHostException, IOException {
		byte[] buf = request.getBytes();
		InetAddress address = InetAddress.getLocalHost();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		socket.send(packet);
	}

	public static SerialPort getSerialPort(String portName) { // call this connect?
		SerialPort serialPort = null;
		if (portName != null) {
			try {
				final CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
				if (portIdentifier.isCurrentlyOwned()) {
					staticLog.warn("Error: Port is currently in use");
				} else {
					final CommPort commPort = portIdentifier.open("owner id", 2000);
					if (commPort instanceof SerialPort) {
						serialPort = (SerialPort) commPort;
						serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					} else {
						staticLog.error(portName + " is not a serial port!");
						commPort.close();
					}
				}
			} catch (UnsupportedCommOperationException e) {
				staticLog.error(Thread.currentThread().getName() + e);
			} catch (PortInUseException e) {
				staticLog.error(Thread.currentThread().getName() + e);
			} catch (NoSuchPortException e) {
				staticLog.error(Thread.currentThread().getName() + e);
			}
		}
		return serialPort;
	}
	public static String fix(final String response, final Delimiters delimiters) {
		String temp = response;
		if (delimiters.delimitersIn(temp) > 1) {
			staticLog.error("more than one delimiter in a string: " + delimiters.replaceControlCharacters(temp));
		}
		if (temp.endsWith("" + Delimiters.lineFeed)) {
			//System.out.println("removing trailing linefeed from response: " + delimiters.replaceControlCharacters(temp));
			temp = temp.substring(0, temp.length() - 1);
		}
		if (temp.startsWith("" + Delimiters.lineFeed)) {
			staticLog.error("removing leading linefeed from response: " + delimiters.replaceControlCharacters(temp));
			temp = temp.substring(1, temp.length());
		}
		if (temp.contains("" + Delimiters.lineFeed)) {
			staticLog.error("removing linefeed from inside response: " + delimiters.replaceControlCharacters(temp));
			temp = temp.replace("" + Delimiters.lineFeed, "");
		}
		if (temp.length() > 0)
			if (temp.substring(0, temp.length() - 1).contains("" + Delimiters.carriageReturn)) {
				staticLog.error("response contains carriage return inside: " + delimiters.replaceControlCharacters(temp));
			}
		return temp;
	}

	public static void shutdown(final Thread thread) { // does not really have anything to do with a serial port
		if (thread != null) {
			staticLog.info("shutting down: " + thread + ", state=" + thread.getState());
			thread.interrupt();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				staticLog.info(Thread.currentThread().getName() + " was interrupted trying to sleep after interrupting" + thread.getName() + " " + e);
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

	private static final Log staticLog = LogFactory.getLog(SerialPortUtilities.class);

}
