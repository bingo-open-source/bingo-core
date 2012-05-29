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

/**
 * 反射类的方法。
 */
public class ReflectMethod extends ReflectMember{
	
	/**
	 * 该方法在类中的索引。
	 */
	private final int    index;
	
	/**
	 * 包裹的原生的java的 {@link Method}。
	 */
	private final Method javaMethod;
	
	/**
	 * 该方法的参数列表。
	 */
	private ReflectParameter[]   parameters;
	
	/**
	 * 指定反射类reflectClass和原生javaMethod的构造方法。
	 * @param reflectClass 指定的反射类。
	 * @param javaMethod 指定的原生javaMethod。
	 */
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
	
	public ReflectParameter[] getParameters(){
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
        	throw new ReflectException("error invoking method '{0}'",getName(),e);
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
        	throw new ReflectException("error invoking method '{0}'",getName(),e);
        }
	}
	
	/**
	 * 反射类的方法的初始化模块，由构造方法调用。
	 */
	private void initialize() {
		this.parameters = new ReflectParameter[javaMethod.getParameterTypes().length];
		
		if(this.parameters.length > 0){
			String[] names = reflectClass.getMetadata().getParameterNames(javaMethod);

			if(null == names){
				names = createUnknowParameterNames(parameters.length);
			}

			for(int i=0;i<parameters.length;i++){
				ReflectParameter p = new ReflectParameter();
				
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
}
