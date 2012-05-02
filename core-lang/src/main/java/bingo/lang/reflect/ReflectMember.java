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

public abstract class ReflectMember<T> implements Named {

	protected final ReflectClass<T> reflectClass;
	protected final boolean	      isDeclared;
	protected final boolean		  isPublic;
	
	protected ReflectMember(ReflectClass<T> reflectClass,Member member){
		this.reflectClass = reflectClass;
		this.isDeclared   = member.getDeclaringClass() == reflectClass.getJavaClass();
		this.isPublic     = Modifier.isPublic(member.getModifiers());
	}

	public ReflectClass<T> getReflectClass() {
    	return reflectClass;
    }
	
	public boolean isDeclared() {
    	return isDeclared;
    }
	
	public boolean isPublic(){
		return isPublic;
	}
}