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

import bingo.lang.Classes;

public final class LoggerFactory {
	
	private static final boolean slf4j;
	
	static {
		slf4j = Classes.forNameOrNull("org.slf4j.Logger") != null;
	}

	public static Logger getLogger(String name) {
		return slf4j ? getSlf4jLogger(name) : new JdkLogger(java.util.logging.Logger.getLogger(name));
	}
	
	public static Logger getLogger(Class<?> clazz) {
		return slf4j ? getSlf4jLogger(clazz) : new JdkLogger(java.util.logging.Logger.getLogger(clazz.getName()));
	}
	
	private static Logger getSlf4jLogger(String name){
		return new SlfLogger(org.slf4j.LoggerFactory.getLogger(name));
	}
	
	private static Logger getSlf4jLogger(Class<?> clazz){
		return new SlfLogger(org.slf4j.LoggerFactory.getLogger(clazz));
	}
}