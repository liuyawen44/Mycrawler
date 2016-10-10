package com.yawen.Util;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
public class LogProperty {
	static Logger root = (Logger) LoggerFactory
	        .getLogger(Logger.ROOT_LOGGER_NAME);

	static {
	    root.setLevel(Level.INFO);
	}
	public LogProperty(){
		
	};
}
