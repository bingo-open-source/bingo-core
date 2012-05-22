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

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

import bingo.lang.Named;

/**
 * 反射类的成员变量
 */
public abstract class ReflectMember implements Named {

	/**
	 * 所对应的反射类
	 */
	protected final ReflectClass<?> reflectClass;
	
	/**
	 * 是否在所在反射类中被声明
	 */
	protected final boolean	      isDeclared;
	
	/**
	 * 是否为公共变量
	 */
	protected final boolean		  isPublic;
	
	/**
	 * 初始化成员变量。
	 * @param reflectClass 所对应的反射类
	 * @param member jdk中的成员变量类
	 */
	protected ReflectMember(ReflectClass<?> reflectClass,Member member){
		this.reflectClass = reflectClass;
		this.isDeclared   = member.getDeclaringClass() == reflectClass.getJavaClass();
		this.isPublic     = Modifier.isPublic(member.getModifiers());
	}

	/**
	 * 获得此成员变量对应的反射类。
	 * @return 此成员变量对应的反射类。
	 */
	public ReflectClass<?> getReflectClass() {
    	return reflectClass;
    }
	
	/**
	 * 是否在所在反射类中被声明。
	 * @return 如果在所在反射类中被声明，返回true，否则返回false。
	 */
	public boolean isDeclared() {
    	return isDeclared;
    }
	
	/**
	 * 是否为公共成员变量。
	 * @return 如果为公共成员变量，返回true，否则返回false。
	 */
	public boolean isPublic(){
		return isPublic;
	}
}