package com.tayek.util.log;
import static com.tayek.util.io.Print.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.*;
import com.tayek.util.core.Pair;
import com.tayek.util.net.Net;
public class LoggingHandler {
	public static class SocketHandlerCallable implements Runnable,java.util.concurrent.Callable<java.util.logging.SocketHandler> {
		public SocketHandlerCallable(String host,int service) {
			this.host=host;
			this.service=service;
		}
		@Override public void run() {
			Thread.currentThread().setName("SHC "+serialNumber+" "+host+":"+service);
			try {
				socketHandler=new SocketHandler(host,service);
				logger.info("got socket handler on: "+host+":"+service);
				socketHandler.setLevel(Level.ALL);
			} catch(IOException e) {
				logger.info("caught: '"+e+"' constructing socket handler on: "+host+":"+service);
			}
		}
		@Override public SocketHandler call() throws Exception {
			run();
			return socketHandler;
		}
		final Integer serialNumber=++serialNumbers;
		final String host;
		final int service;
		public SocketHandler socketHandler;
		static int serialNumbers;
	}
	public static void addMyHandlerAndSetLevel(Logger logger,Level level) {
		logger.setUseParentHandlers(false);
		JulLogging.addConsoleHandler(logger,myFormatter,Level.ALL);
		logger.setLevel(level);
	}
	public static void addFileHandler(Logger logger,File logFileDirectory,String prefix) {
		try {
			String pattern=prefix+".%u.%g.log";
			File logFile=new File(logFileDirectory,pattern);
			Handler handler=JulLogging.addFileHandler(logger,logFile.getPath(),50_000_000,10,false,null,Level.ALL);
			logger.warning("added file handler: "+handler);
		} catch(Exception e) {
			logger.warning("add file handler caught: "+e);
		}
	}
	// ---------- logger setup ----------
	public static void addSocketHandler(SocketHandler socketHandler) {
		if(socketHandler!=null) for(Logger logger:map.values())
			if(!Arrays.asList(logger.getHandlers()).contains(socketHandler)) logger.addHandler(socketHandler);
	}
	public static void removeSocketHandler(SocketHandler socketHandler) {
		if(socketHandler!=null) for(Logger logger:map.values())
			if(Arrays.asList(logger.getHandlers()).contains(socketHandler)) logger.removeHandler(socketHandler);
	}
	private static Map<Class<?>,Logger> makeMapAndSetLevels(Set<Class<?>> classes) {
		LoggingHandler.addMyHandlerAndSetLevel(Logger.getGlobal(),Level.ALL);
		Map<Class<?>,Logger> map=new LinkedHashMap<Class<?>,Logger>();
		for(Class<?> clazz:classes) {
			Logger logger=Logger.getLogger(clazz.getName());
			LoggingHandler.addMyHandlerAndSetLevel(logger,Level.OFF);
			map.put(clazz,logger);
		}
		return map;
	}
	public static void init() {
		init(null);
	}
	public static void init(File logFileDirectory) {
		if(!once) {
			map=makeMapAndSetLevels(loggers);
			once=true;
		}
	}
	public static void setLevel(Level level) {
		if(!once) init();
		for(Logger logger:map.values())
			logger.setLevel(level);
	}
	public static void initAndSetLevel(Level level) {
		init();
		setLevel(level);
	}
	// ---------- diagnostics ----------
	public static void main(String[] arguments) {
		Logger logger=Logger.getLogger("foo");
		logger.info("info");
		addMyHandlerAndSetLevel(logger,Level.INFO);
		logger.fine("fine");
		logger.info("info");
		logger.warning("warning");
	}
	public static void printlLoggers() {
		Enumeration<String> x=LogManager.getLogManager().getLoggerNames();
		for(;x.hasMoreElements();) {
			String name=x.nextElement();
			Logger logger=LogManager.getLogManager().getLogger(name);
			if(logger==null) p("logger: '"+name+" is null!");
			else {
				if(logger.getHandlers()==null) p("logger: '"+name+"', get handlers() returns null!");
				p("logger: '"+name+"' has: "+logger.getHandlers().length+" loggers.");
			}
		}
	}
	public static SocketHandler createSocketHandlerAndWait(String host,int service) {
		final SocketHandlerCallable task=new SocketHandlerCallable(host,service);
		task.run();
		return task.socketHandler;
	}
	static void startSocketHandler(Pair<String,Integer> pair) {
		try {
			SocketHandler socketHandler=createSocketHandlerAndWait(pair.first,pair.second);
			if(socketHandler!=null) {
				p("got socket handler: "+socketHandler+" to: "+pair);
				LoggingHandler.addSocketHandler(socketHandler);
				synchronized(logServerHosts) {
					logServerHosts.put(pair,socketHandler);
				}
				Logger global=Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
				global.addHandler(socketHandler);
				global.warning("global with socket handler.");
			} else p("could not start socket handler to: "+pair.first+':'+pair.second);
		} catch(Exception e) {
			p("caught: "+e);
		}
	}
	public static void stopSocketHandler(SocketHandler socketHandler) {
		if(socketHandler!=null) {
			logger.warning("closing: "+socketHandler);
			try {
				socketHandler.close();
			} catch(Exception e) {
				logger.warning("caught: "+e);
			}
		}
	}
	public static boolean areAnySockethandlersOn() {
		boolean areAnyOn=false;
		synchronized(logServerHosts) {
			for(Pair<String,Integer> pair:logServerHosts.keySet())
				if(logServerHosts.get(pair)!=null) {
					areAnyOn=true;
					break;
				}
		}
		return areAnyOn;
	}
	// these can't really be static if we are testing multiple tablets - fix!
	public static boolean stopSocketHandlers() {
		boolean wereAnyOn=false;
		synchronized(logServerHosts) {
			for(Pair<String,Integer> pair:logServerHosts.keySet())
				if(logServerHosts.get(pair)!=null) {
					p("stopping socket handler to: "+pair);
					stopSocketHandler(logServerHosts.get(pair));
					logServerHosts.put(pair,null);
					wereAnyOn=true;
				}
		}
		return wereAnyOn;
	}
	public static String socketHandlers() {
		StringBuffer sb=new StringBuffer();
		synchronized(logServerHosts) {
			for(Entry<Pair<String,Integer>,SocketHandler> entry:logServerHosts.entrySet())
				sb.append(entry.getKey()).append(':').append(entry.getValue()!=null).append(',');
		}
		return sb.toString();
	}
	public static void printSocketHandlers() {
		p("socket handlers:");
		synchronized(logServerHosts) {
			for(Entry<Pair<String,Integer>,SocketHandler> entry:logServerHosts.entrySet())
				p(entry.getKey()+";"+entry.getValue());
		}
	}
	public static void startSocketHandlers() {
		synchronized(logServerHosts) {
			for(final Pair<String,Integer> pair:logServerHosts.keySet())
				if(logServerHosts.get(pair)==null) {
					new Thread(new Runnable() {
						@Override public void run() {
							startSocketHandler(pair); // currently this waits,
														// so ...
						}
					},"start: "+pair).start();
				}
		}
	}
	public static void toggleSockethandlers() {
		boolean wereAnyOn=stopSocketHandlers();
		if(!wereAnyOn) startSocketHandlers();
	}
	public static boolean once;
	// public static SocketHandler socketHandler;
	// private static final Level levels[]=
	// {Level.SEVERE,Level.WARNING,Level.INFO,Level.CONFIG,Level.FINE,Level.FINER,Level.FINEST};
	static Map<Class<?>,Logger> map;
	public static final Set<Class<?>> loggers=new LinkedHashSet<>();
	static /* wow! */ {
		loggers.add(LoggingHandler.class);
		loggers.add(Net.class);
	}
	public static final int defaultLogServerService=5000;
	public static final int chainsawLogServerService=2222;
	public static final int lilithLogServerService=11020;
	public static final String defaultLogServerHost="localhost"; // was laptop
																	// today
	public static final Map<Pair<String,Integer>,SocketHandler> logServerHosts=new LinkedHashMap<>();
	static {
		for(Integer service:new Integer[] {defaultLogServerService,/*chainsawLogServerService,lilithLogServerService,*/}) {
			logServerHosts.put(new Pair<String,Integer>(defaultLogServerHost,service),null);
		}
	}
	public static final Logger logger=Logger.getLogger(LoggingHandler.class.getName());
	private static final MyFormatter myFormatter=new MyFormatter();
}
