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
import java.util.ArrayList;
import java.util.List;

import bingo.lang.Arrays;
import bingo.lang.Assert;
import bingo.lang.Named;
import bingo.lang.exceptions.ReflectException;

public class ReflectAnnotation {

	public static ReflectAnnotation get(Class<?> annotationType){
		Assert.isTrue(annotationType.isAnnotation(),"not an annotation type");
		return new ReflectAnnotation(annotationType);
	}
	
	public static class AnnotationField implements Named {
		
		private final Method m;
		
		private AnnotationField(Method m){
			this.m = m;
		}
		
		public String getName(){
			return m.getName();
		}
		
		public Class<?> getType(){
			return m.getReturnType();
		}
		
		public Object getValue(Object annotation){
			try {
	            return m.invoke(annotation, Arrays.EMPTY_OBJECT_ARRAY);
            } catch (Exception e) {
            	throw new ReflectException(e.getMessage(),e);
            }
		}
	}
	
	private final Class<?>    type;
	private AnnotationField[] fields;
	
	private ReflectAnnotation(Class<?> annotationType){
		this.type = annotationType;
	}
	
	public AnnotationField[] getFields(){
		if(null == fields){
			List<AnnotationField> list = new ArrayList<AnnotationField>();

			for(Method m : type.getDeclaredMethods()){
				if(Modifier.isPublic(m.getModifiers()) && !Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length == 0){
					list.add(new AnnotationField(m));
				}
			}
			
			fields = list.toArray(new AnnotationField[list.size()]);
		}
		return fields;
	}
	
	public AnnotationField getValueField(){
		for(AnnotationField field : getFields()){
			if(field.getName().equals("value")){
				return field;
			}
		}
		return null;
	}
}
