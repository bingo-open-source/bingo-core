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

import bingo.lang.exceptions.ReflectException;

public class ReflectConstructor<T> extends ReflectMember<T> {

	private final Constructor<T>  javaConstructor;
	
	protected ReflectConstructor(ReflectClass<T> reflectClass, Constructor<T> javaConstructor){
		super(reflectClass,javaConstructor);

		this.javaConstructor = javaConstructor;
		
		this.create();
	}
	
	public String getName() {
	    return javaConstructor.getName();
    }
	
	public Constructor<T> getJavaConstructor(){
		return this.javaConstructor;
	}
	
	public T newInstance(Object... args){
		try {
	        return javaConstructor.newInstance(args);
        } catch (Exception e) {
        	throw new ReflectException("error newInstance in constructor '{0}.{1}'", javaConstructor.getDeclaringClass().getName(), getName());
        }
	}
	
	private void create(){
		this.setAccessiable();
	}
	
	private void setAccessiable(){
		try {
	        this.javaConstructor.setAccessible(true);
        } catch (SecurityException e) {
        	;
        }
	}
}