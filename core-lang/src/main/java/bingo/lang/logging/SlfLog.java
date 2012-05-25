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

import bingo.lang.Strings;

public class SlfLog implements Log {
	
	private final org.slf4j.Logger logger;
	
	public SlfLog(org.slf4j.Logger logger) {
		this.logger = logger;
    }
	
	public boolean isTraceEnabled() {
	    return logger.isTraceEnabled();
    }
	
	public boolean isDebugEnabled() {
	    return logger.isDebugEnabled();
    }
	
	public boolean isInfoEnabled() {
	    return logger.isInfoEnabled();
    }

	public boolean isWarnEnabled() {
	    return logger.isWarnEnabled();
    }

	public boolean isErrorEnabled() {
	    return logger.isErrorEnabled();
    }
	
	public void trace(String msg) {
	    logger.trace(msg);
    }
	
	public void trace(String msg, Object... args) {
		logger.trace(Strings.format(msg, args));
    }

	public void debug(String msg) {
		logger.debug(msg);
    }
	
	public void debug(String msg, Object... args) {
		logger.debug(Strings.format(msg, args));
    }
	
	public void info(String msg) {
		logger.info(msg);
    }
	
	public void info(String msg, Object... args) {
		logger.info(Strings.format(msg, args));
    }
	
	public void warn(String msg) {
		logger.warn(msg);
    }
	
	public void warn(String msg, Object... args) {
	    logger.warn(Strings.format(msg, args));
    }

	public void error(String msg) {
		logger.error(msg);
    }
	
	public void error(String msg, Object... args) {
	    logger.error(Strings.format(msg, args));
    }
}