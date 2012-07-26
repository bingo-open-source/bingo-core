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
package bingo.lang.reflect;

import java.lang.reflect.Constructor;

import bingo.lang.Classes;
import bingo.lang.exceptions.ReflectException;

abstract class ReflectInstantiator<T> {
	
	private static boolean foundSunReflectionFactory;
	
	static {
		try {
	        foundSunReflectionFactory = Classes.forNameOrNull("sun.reflect.ReflectionFactory") != null;
        } catch (Throwable e) {
        	foundSunReflectionFactory = false;
        }
	}
	
	static <T> ReflectInstantiator<T> forType(Class<T> type) {
		return foundSunReflectionFactory ? new SunReflectionFactoryInstantiator<T>(type) : null;
	}

	public abstract T newInstance();

	@SuppressWarnings({"restriction","rawtypes","unchecked"})
	static final class SunReflectionFactoryInstantiator<T> extends ReflectInstantiator<T> {

		private final Constructor mungedConstructor;

		SunReflectionFactoryInstantiator(Class<T> type) {
			sun.reflect.ReflectionFactory reflectionFactory = sun.reflect.ReflectionFactory.getReflectionFactory();
			Constructor javaLangObjectConstructor;

			try {
				javaLangObjectConstructor = Object.class.getConstructor((Class[]) null);
			} catch (NoSuchMethodException e) {
				throw new Error("Cannot find constructor for java.lang.Object!");
			}
			
			mungedConstructor = reflectionFactory.newConstructorForSerialization(type, javaLangObjectConstructor);
			mungedConstructor.setAccessible(true);
		}

		@Override
		public T newInstance() {
			try {
				return (T)mungedConstructor.newInstance((Object[]) null);
			} catch (Throwable e) {
				throw new ReflectException(e);
			}
		}
	}
}