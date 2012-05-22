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

/**
 * 反射类的构造器。
 * @param <T> 
 */
public class ReflectConstructor<T> extends ReflectMember {

	/**
	 * JDK中的 {@link Constructor}。
	 */
	private final Constructor<T> javaConstructor;
	
	/**
	 * 通过对应的反射类和JDK中的 {@link Constructor}实例初始化。
	 * @param reflectClass 所对应的反射类。
	 * @param javaConstructor JDK中的 {@link Constructor}实例。
	 */
	protected ReflectConstructor(ReflectClass<T> reflectClass, Constructor<T> javaConstructor){
		super(reflectClass,javaConstructor);

		this.javaConstructor = javaConstructor;
		
		this.initialize();
	}
	
	/**
	 * 获得此反射类的构造器的名称。
	 */
	public String getName() {
	    return javaConstructor.getName();
    }
	
	/**
	 * 获取此 {@link ReflectConstructor}所包裹的Java原生的 {@link Constructor}。
	 * @return 此 {@link ReflectConstructor}所包裹的Java原生的 {@link Constructor}。
	 */
	public Constructor<T> getJavaConstructor(){
		return this.javaConstructor;
	}
	
	/**
	 * 根据传入参数实例化 {@link ReflectConstructor}，
	 * 实际上调用了所包裹的原生的 {@link Constructor}的newInstance(Object...)方法。
	 * @param args 传入的用于实例化的参数。
	 * @return 实例化后的对象。
	 */
	public T newInstance(Object... args){
		try {
	        return javaConstructor.newInstance(args);
        } catch (Exception e) {
        	throw new ReflectException("error newInstance in constructor '{0}.{1}'", javaConstructor.getDeclaringClass().getName(), getName());
        }
	}
	
	/**
	 * 反射类的构造器的初始化模块。
	 */
	private void initialize(){
		this.setAccessiable();
	}
	
	/**
	 * 将构造器的可访问性设置为true。
	 */
	private void setAccessiable(){
		try {
	        this.javaConstructor.setAccessible(true);
        } catch (SecurityException e) {
        	;
        }
	}
	
	/**
	 * 重写。将返回所包裹的 {@link Constructor}的toString()的内容。
	 */
	@Override
    public String toString() {
		return javaConstructor.toString();
    }
}