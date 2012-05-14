/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bingo.lang.logging;

import java.util.logging.Level;

public class JdkLog implements Log {
	
	private final java.util.logging.Logger logger;
	
	JdkLog(java.util.logging.Logger log) {
		this.logger = log;
    }
	
	public boolean isTraceEnabled() {
		return logger.isLoggable(Level.FINEST);
    }
	
	public boolean isDebugEnabled() {
		return logger.isLoggable(Level.FINE);
    }
	
	public boolean isInfoEnabled() {
		return logger.isLoggable(Level.INFO);
    }
	
	public boolean isWarnEnabled() {
		return logger.isLoggable(Level.WARNING);
    }

	public boolean isErrorEnabled() {
		return logger.isLoggable(Level.SEVERE);
    }
	
	public void trace(String msg) {
		logger.finest(msg);
    }
	
	public void trace(String msg, Object... args) {
		logger.finest(Formatter.format(msg, args));
    }

	public void debug(String msg) {
		logger.fine(msg);		
    }
	
	public void debug(String msg, Object... args) {
		logger.fine(Formatter.format(msg, args));
    }
	
	public void info(String msg) {
		logger.info(msg);
    }
	
	public void info(String msg, Object... args) {
		logger.info(Formatter.format(msg, args));
    }
	
	public void warn(String msg) {
		logger.warning(msg);
    }
	
	public void warn(String msg, Object... args) {
	    logger.warning(Formatter.format(msg, args));
    }

	public void error(String msg) {
		logger.severe(msg);
    }

	public void error(String msg, Object... args) {
		logger.severe(Formatter.format(msg,args));
    }
}
