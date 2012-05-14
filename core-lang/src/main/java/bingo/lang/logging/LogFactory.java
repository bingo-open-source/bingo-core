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


public final class LogFactory {
	
	private static final boolean slf4j;
	
	static {
		slf4j = forName("org.slf4j.Logger");
	}

	public static Log get(String name) {
		return slf4j ? getSlf4jLogger(name) : new JdkLog(java.util.logging.Logger.getLogger(name));
	}
	
	public static Log get(Class<?> clazz) {
		return slf4j ? getSlf4jLogger(clazz) : new JdkLog(java.util.logging.Logger.getLogger(clazz.getName()));
	}
	
	private static Log getSlf4jLogger(String name){
		return new SlfLog(org.slf4j.LoggerFactory.getLogger(name));
	}
	
	private static Log getSlf4jLogger(Class<?> clazz){
		return new SlfLog(org.slf4j.LoggerFactory.getLogger(clazz));
	}
	
	private static boolean forName(String className) {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
		}
		if (cl == null) {
			cl = LogFactory.class.getClassLoader();
		}
		
		try {
	        return null != cl.loadClass(className);
        } catch (Throwable e) {
        	return false;
        }
	}
}