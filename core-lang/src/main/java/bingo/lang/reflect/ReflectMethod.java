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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import bingo.lang.exceptions.ReflectException;

public class ReflectMethod extends ReflectMember{
	
	private final int    index;
	private final Method javaMethod;
	
	protected ReflectMethod(ReflectClass<?> reflectClass,Method javaMethod) {
		super(reflectClass,javaMethod);
		
		this.javaMethod = javaMethod;
		this.index      = reflectClass.getAccessor().getMethodIndex(javaMethod);
		
		this.create();
	}
	
	public String getName() {
	    return javaMethod.getName();
    }

	public Method getJavaMethod(){
		return this.javaMethod;
	}
	
	public boolean isStatic(){
		return Modifier.isStatic(javaMethod.getModifiers());
	}
	
	public Object invoke(Object instance,Object... args) {
		try {
	        if(index == -1){
	        	return javaMethod.invoke(instance, args);
	        }else{
	        	return reflectClass.getAccessor().invokeMethod(instance, index, args);
	        }
        } catch (Exception e) {
        	throw new ReflectException(e,"error invoking method '{0}'",getName());
        }
	}
	
	public Object invokeStatic(Object... args) {
		try {
	        if(index == -1){
	        	return javaMethod.invoke(null, args);
	        }else{
	        	return reflectClass.getAccessor().invokeMethod(null, index, args);
	        }
        } catch (Exception e) {
        	throw new ReflectException(e,"error invoking method '{0}'",getName());
        }
	}
	
	private void create() {
		
	}
}