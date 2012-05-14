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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import bingo.lang.Named;
import bingo.lang.exceptions.ReflectException;

public class ReflectMethod extends ReflectMember{
	
	private final int    index;
	private final Method javaMethod;
	private Parameter[]   parameters;
	
	protected ReflectMethod(ReflectClass<?> reflectClass,Method javaMethod) {
		super(reflectClass,javaMethod);
		
		this.javaMethod = javaMethod;
		this.index      = reflectClass.getAccessor().getMethodIndex(javaMethod);
		
		this.initialize();
	}
	
	public String getName() {
	    return javaMethod.getName();
    }

	public Method getJavaMethod(){
		return this.javaMethod;
	}
	
	public Parameter[] getParameters(){
		return parameters;
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
	
	private void initialize() {
		this.parameters = new Parameter[javaMethod.getParameterTypes().length];
		
		if(this.parameters.length > 0){
			String[] names = reflectClass.getMetadata().getParameterNames(javaMethod);

			if(null == names){
				names = createUnknowParameterNames(parameters.length);
			}

			for(int i=0;i<parameters.length;i++){
				Parameter p = new Parameter();
				
				p.index       = i+1;
				p.name        = names[i];
				p.type        = javaMethod.getParameterTypes()[i];
				p.genericType = javaMethod.getGenericParameterTypes()[i];
				p.annotations = javaMethod.getParameterAnnotations()[i];
				
				parameters[i] = p;
			}
		}
	}
	
	private static String[] createUnknowParameterNames(int length){
		String[] names = new String[length];
		
		for(int i=0;i<length;i++){
			names[i] = "arg" + (i+1);
		}
		
		return names;
	}
	
	@Override
    public String toString() {
		return javaMethod.toString();
    }	
	
	public static class Parameter implements Named {

		private int 		  index;
		private String 		  name;
		private Class<?> 	  type;
		private Type 		  genericType;
		private Annotation[] annotations;

		public final int getIndex() {
			return index;
		}

		public final String getName() {
			return name;
		}

		public final Class<?> getType() {
			return type;
		}
		
		public final Type getGenericType() {
        	return genericType;
        }

		public final Annotation[] getAnnotations() {
			return annotations;
		}
	}
}