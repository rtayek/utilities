package p;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.Consumer;

public class IO {
	static class Connection extends Thread {
		Connection(Socket socket, Consumer<String> stringConsumer, Consumer<Exception> exceptionConsumer,
				boolean outGoing) {
			this.socket = socket;
			this.stringConsumer = stringConsumer;
			this.exceptionConsumer = exceptionConsumer;
			setName("connection #" + serialNumber + (outGoing ? " to: " : " from: ") + socket);
			InputStream inputStream = null;
			try {
				inputStream = socket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			in = new BufferedReader(new InputStreamReader(inputStream));
			try {
				out = new OutputStreamWriter(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		boolean send(String string) {
			try {
				out.write(string + '\n');
				out.flush();
				sent++;
				return true;
			} catch (IOException e) {
				if (isClosing)
					;
				else
					e.printStackTrace();
				return false;
			}
		}

		@Override
		public void run() {
			try {
				while (true) {
					String string = in.readLine();
					if (stringConsumer != null)
						stringConsumer.accept(string);
					if (string != null)
						received++;
					else
						break;
				}
			} catch (Exception e) {
				if (isClosing)
					;
				else {
					p(this + " caught unexpected: " + e);
					// e.printStackTrace();
					unexpected++;
					// maybe keep the names of these in a list or ?
					if (exceptionConsumer != null)
						exceptionConsumer.accept(e);
				}
			}
		}

		public void close() {
			synchronized (isClosing) {
				isClosing = true;
				try {
					socket.close();
					// p(Thread.currentThread().getName()+" joining with:
					// "+this);
					try {
						this.join(0);
						// p("joined with: "+this);
						// p("thread: "+toS(this));
						if (this.getState().equals(Thread.State.TERMINATED) || this.getState().equals(Thread.State.NEW))
							;
						else
							p("thread has strange state after join: " + toS(this));
					} catch (InterruptedException e) {
						p("oops 1");
						e.printStackTrace();
					}
				} catch (IOException e) {
					p("oops 2");
					e.printStackTrace();
				}
			}
		}

		public static Socket connect(InetSocketAddress inetSocketAddress, int timeout,
				Consumer<Exception> exceptionConsumer) {
			Socket socket = new Socket();
			try {
				socket.connect(inetSocketAddress, timeout);
			} catch (Exception e) {
				if (exceptionConsumer != null)
					exceptionConsumer.accept(e);
				else
					p("connect to: " + inetSocketAddress + " caught: " + e);
				try {
					socket.close();
				} catch (IOException e2) {
					if (exceptionConsumer != null)
						exceptionConsumer.accept(e2);
					else
						p("caught: " + e2);
				}
				socket = null;
			}
			return socket;
		}

		final Consumer<String> stringConsumer;
		final Consumer<Exception> exceptionConsumer;
		final Socket socket;
		final BufferedReader in;
		final Writer out;
		volatile Boolean isClosing = false;
		int sent, received, unexpected; // mostly for testing
		final int serialNumber = ++serialNumbers;
		public static final String timeOutMessage = "connect timed out";
		static int serialNumbers = 0;
	}

	static class Acceptor extends Thread {
		protected Acceptor(ServerSocket serverSocket, Consumer<Socket> consumer) {
			this.serverSocket = serverSocket;
			this.consumer = consumer;
			setName("acceptor #" + serialNumber + " on: " + serverSocket);
		}

		@Override
		public void run() {
			while (!done)
				try {
					p("listening on " + serverSocket);
					Socket socket = serverSocket.accept();
					p("accepted connection from: " + socket);
					if (consumer != null)
						consumer.accept(socket);
				} catch (IOException e) {
					if (isClosing)
						;// p("closing, caught: "+e);
					else {
						p("unexpected, caught: " + e);
						e.printStackTrace();
					}
					done = true;
				}
		}

		public void close() {
			synchronized (isClosing) {
				isClosing = true;
				try {
					// p("closing server socket.");
					serverSocket.close();
					// p(Thread.currentThread().getName()+" joining with:
					// "+this);
					try {
						this.join(3); // check this magic number!
						// p("joined with: "+this);
						// p("thread: "+toS(this));
						if (!this.getState().equals(Thread.State.TERMINATED))
							p("2 thread has strange state after join: " + toS(this));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		static boolean bind(ServerSocket serverSocket, SocketAddress socketAddress) { // move to acceptor?
			if (serverSocket == null)
				try {
					serverSocket = new ServerSocket();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (serverSocket != null) {
				if (!serverSocket.isBound())
					try {
						if (socketAddress != null) {
							serverSocket.bind(socketAddress);
							return true;
						}
					} catch (IOException e) {
						p("bind caught: " + e);
					}
			}
			return false;
		}

		static Acceptor acceptor(SocketAddress socketAddress, Consumer<Socket> socketConsumer) {
			try {
				ServerSocket serverSocket = new ServerSocket();
				boolean ok = bind(serverSocket, socketAddress);
				if (ok)
					return new Acceptor(serverSocket, socketConsumer);
			} catch (IOException e) {
				System.err.println("acceptor() caught: " + e);
			}
			return null;
		}

		public final ServerSocket serverSocket;
		final Consumer<Socket> consumer;
		boolean done;
		volatile Boolean isClosing = false;
		final int serialNumber = ++serialNumbers;
		static int serialNumbers = 0;
	}

	public static void pn(PrintStream out, String string) {
		out.print(string);
		out.flush();
	}

	public static void pn(String string) {
		synchronized (System.out) {
			pn(System.out, string);
		}
	}

	public static void p(PrintStream out, String string) {
		synchronized (out) {
			pn(out, string);
			pn(out, System.getProperty("line.separator"));
		}
	}

	public static void p(String string) {
		p(System.out, string);
	}
	public static void pe(String string) {
		p(System.err, string);
	}

	public static String toS(Thread thread) {
		return "thread: name: " + thread.getName() + ", state: " + thread.getState() + ", is alive: " + thread.isAlive()
				+ ", is interrupted:  " + thread.isInterrupted();
	}

	public static String toS(ServerSocket serverSocket) {
		return serverSocket + ": isBound: " + serverSocket.isBound() + ", isClosed: " + serverSocket.isClosed();
	}

	public static String toS(InterfaceAddress interfaceAddress) {
		InetAddress a = interfaceAddress.getAddress();
		String s = "" + interfaceAddress.getAddress() + " " + interfaceAddress.getNetworkPrefixLength();
		s += " " + a.isSiteLocalAddress() + " " + a.isAnyLocalAddress() + " " + a.isLinkLocalAddress();
		return s;
	}

	public static void fromFile(final StringBuffer stringBuffer, final File file) {
		try {
			Reader r = new FileReader(file);
			int c = 0;
			while ((c = r.read()) != -1)
				stringBuffer.append((char) c);
			r.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Thread[] getThreads() {
		int big = 2 * Thread.activeCount();
		Thread[] threads = new Thread[big];
		Thread.enumerate(threads);
		return threads;
	}

	public static void printThreads(List<String> excluded) {
		Thread[] threads = getThreads();
		for (Thread thread : threads)
			if (thread != null && (excluded == null || !excluded.contains(thread.getName())))
				p(toS(thread));
	}

	public static void printThreads() {
		printThreads(Collections.<String>emptyList());
	}

	static String toHexString(byte[] bytes) {
		String theBytes = "";
		for (int i = 0; i < bytes.length; i++) {
			String theByte = Integer.toHexString(bytes[i] & 0xff);
			if (theByte.length() < 2)
				theByte = '0' + theByte;
			theBytes += theByte;
		}
		return theBytes;
	}

	public void run() throws IOException, InterruptedException {
		threads = Thread.activeCount();
		inetAddress = InetAddress.getByName(host);
		ServerSocket serverSocket = new ServerSocket(service);
		final Consumer<String> stringConsumer = new Consumer<String>() {
			@Override
			public void accept(String string) {
				IO.p("incoming received: " + string);
				if(!incoming.send("bar"))
					IO.p("send was not ok!");
			}
		};
		final Consumer<Exception> exceptionConsumer = new Consumer<Exception>() {
			@Override
			public void accept(Exception exception) {
				IO.p("incoming caught: " + exception);
			}
		};
		Consumer<Socket> consumer = new Consumer<Socket>() {
			@Override
			public void accept(Socket socket) {
				incoming = new IO.Connection(socket, stringConsumer, exceptionConsumer, false);
				incoming.start();
			}
		};
		acceptor = new IO.Acceptor(serverSocket, consumer);
		acceptor.start();
        Socket socket=new Socket(inetAddress,service);
        outgoing=new IO.Connection(socket,new Consumer<String>() {
            @Override public void accept(final String string) {
                IO.p("outgoing received: "+string);
                outgoing.send("quux");
            }
        },new Consumer<Exception>() {
            @Override public void accept(Exception exception) {
                IO.p("outgoing caught: "+exception);
            }
        },true);
        IO.p("incoming: "+incoming.socket);
        IO.p("outgoing: "+outgoing.socket);
        outgoing.start();
        outgoing.send("foo");
        
        Thread.sleep(10);
        incoming.send("baz");
		acceptor.close();
		if (outgoing != null)
			outgoing.close();
		if (incoming != null)
			incoming.close();
		// order of the above does not seem to matter.
		// but copy of IO in rubiks prohect fails!
		int active = Thread.activeCount();
		if (printExtraThreads) {
			if (active > threads) {
				IO.p("extra threads: " + (active - threads));
				IO.printThreads(excluded);
			}
		}

	}

	public static void main(String args[]) throws IOException, InterruptedException {
		new IO().run();
	}
	boolean done;
	final String host = "127.0.0.1";
	boolean printExtraThreads = true;
	int threads;
	InetAddress inetAddress;
	IO.Acceptor acceptor;
	IO.Connection incoming, outgoing;
	final Integer service = IO.testService++;
	static List<String> excluded = Arrays.asList(new String[] { "main", "ReaderThread" });
	public static Integer testService = 11223;
}
