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
import java.lang.reflect.Type;

import bingo.lang.Named;

public class ReflectParameter implements Named {

	int 		  index;
	String 		  name;
	Class<?> 	  type;
	Type 		  genericType;
	Annotation[] annotations;

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
	
	@SuppressWarnings("unchecked")
	public final <T extends Annotation> T getAnnotation(Class<T> annotationType){
		if(null == annotations){
			return null;
		}
		
		for(Annotation a : annotations){
			if(a.annotationType().equals(annotationType)){
				return (T)a;
			}
		}
		
		return null;
	}
	
	public final boolean isAnnotationPresent(Class<? extends Annotation> annotationType){
		return getAnnotation(annotationType) != null;
	}
}