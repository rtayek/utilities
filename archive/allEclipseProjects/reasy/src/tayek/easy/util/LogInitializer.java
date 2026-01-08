package tayek.easy.util;

import org.apache.commons.logging.*;
import org.apache.commons.logging.impl.*;
import org.apache.log4j.BasicConfigurator;
import java.util.*;

public class LogInitializer {
	private LogInitializer() {}

	public static Object getLevel(Log commonsLog) {
		if (commonsLog instanceof Jdk14Logger) {
			final Jdk14Logger commonsLogger = (Jdk14Logger) commonsLog;
			final java.util.logging.Logger logger = commonsLogger.getLogger();
			final java.util.logging.Level level = logger.getLevel();
			return level;
		} else if (commonsLog instanceof Log4JLogger) {
			final Log4JLogger commonsLogger = (Log4JLogger) commonsLog;
			final org.apache.log4j.Logger logger = commonsLogger.getLogger();
			final org.apache.log4j.Level level = logger.getLevel();
			if (level == null)
				if (loggersThatGetLevelReturnsNullFor.add(logger.getName()))
					log.warn(logger.getName() + ".getLevel() returns null!"); // side effect!
			return level;
		} else {
			commonsLog.info("it's a: " + commonsLog);
			throw new RuntimeException("don't know how to deal with commons log: " + commonsLog);
		}
	}

	static final Set<String> loggersThatGetLevelReturnsNullFor = new HashSet<String>();

	public static void setLevel(Log commonsLog, Object level) {
		if (commonsLog instanceof Jdk14Logger) {
			Jdk14Logger commonsLogger = (Jdk14Logger) commonsLog;
			java.util.logging.Logger logger = commonsLogger.getLogger();
			logger.setLevel((java.util.logging.Level) level);
		} else if (commonsLog instanceof Log4JLogger) {
			Log4JLogger commonsLogger = (Log4JLogger) commonsLog;
			org.apache.log4j.Logger logger = commonsLogger.getLogger();
			logger.setLevel((org.apache.log4j.Level) level);
		} else {
			commonsLog.info("it's a: " + commonsLog);
			throw new RuntimeException("don't know how to deal with commons log: " + commonsLog);
		}
	}

	private static boolean once;

	public static void main(String[] args) throws Exception {
		once();
		setLevel(log, org.apache.log4j.Level.DEBUG);
		log.warn(getLevel(log) + " warn");
		log.info(getLevel(log) + " info");
		log.debug(getLevel(log) + " debug");
		log.error(getLevel(log) + " error");
		setLevel(log, org.apache.log4j.Level.INFO);
		log.warn(getLevel(log) + " warn");
		log.info(getLevel(log) + " info");
		log.debug(getLevel(log) + " debug");
		log.error(getLevel(log) + " error");
		setLevel(log, org.apache.log4j.Level.WARN);
		log.warn(getLevel(log) + " warn");
		log.info(getLevel(log) + " info");
		log.debug(getLevel(log) + " debug");
		log.error(getLevel(log) + " error");
		setLevel(log, org.apache.log4j.Level.ERROR);
		log.warn(getLevel(log) + " warn");
		log.info(getLevel(log) + " info");
		log.debug(getLevel(log) + " debug");
		log.error(getLevel(log) + " error");
	}

	static final String logfilename = "log4j.properties";

	public static void once() {
		if (!once) {
			once = true;
			if (LogInitializer.class.getResource(logfilename) == null) {
				org.apache.log4j.Logger l = org.apache.log4j.Logger.getRootLogger();
				BasicConfigurator.configure();
				// org.apache.log4j.Logger l=org.apache.log4j.Logger.getRootLogger();
				System.out.println("initializing logging");
				l.setLevel(org.apache.log4j.Level.INFO);
				//l.setLevel(org.apache.log4j.Level.DEBUG);
				System.out.println("setting logging to level to " + l.getLevel());
			} else
				System.out.println("using " + logfilename);
		}
	}

	static final Log log = LogFactory.getLog(LogInitializer.class);
}
