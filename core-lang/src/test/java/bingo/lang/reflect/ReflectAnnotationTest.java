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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.junit.Test;

import bingo.lang.Arrays;
import bingo.lang.reflect.ReflectAnnotation.AnnotationField;
import bingo.lang.testing.junit.ConcurrentTestCase;

public class ReflectAnnotationTest extends ConcurrentTestCase {

	@Retention(RetentionPolicy.RUNTIME)  
	@Target(ElementType.METHOD) 
	public static @interface A1 {
		
	}
	
	@Retention(RetentionPolicy.RUNTIME)  
	@Target(ElementType.METHOD) 
	public static @interface A2 {
		public String value();
	}
	
	@Retention(RetentionPolicy.RUNTIME)  
	@Target(ElementType.METHOD) 
	public static @interface A3 {
		public String value() default "value";
		
		public boolean bool1() default false;
		
		public boolean bool2() default true;
		
		boolean bool3() default true;
		
		abstract String ss();
	}
	
	@Test
	public void testNotFields(){
		ReflectAnnotation a = ReflectAnnotation.get(A1.class);
		assertNotNull(a);
		assertEquals(0, a.getFields().length);
		assertNull(a.getValueField());
	}
	
	@A2("xx")
	@Test
	public void testSingleValueField() throws Exception {
		ReflectAnnotation a = ReflectAnnotation.get(A2.class);
		assertNotNull(a);
		assertEquals(1, a.getFields().length);
		assertNotNull(a.getValueField());
		
		Method method = this.getClass().getDeclaredMethod("testSingleValueField", Arrays.EMPTY_CLASS_ARRAY);
		A2 a2 = method.getAnnotation(A2.class);
		
		assertEquals(a2.value(),a.getValueField().getValue(a2));
	}
	
	@A3(ss="ss")
	@Test
	public void testMultiFieldsWithDefaultValue() throws Exception {
		ReflectAnnotation a = ReflectAnnotation.get(A3.class);
		assertNotNull(a);
		assertEquals(5, a.getFields().length);
		assertNotNull(a.getValueField());
		
		Method method = this.getClass().getDeclaredMethod("testMultiFieldsWithDefaultValue", Arrays.EMPTY_CLASS_ARRAY);
		A3 a3 = method.getAnnotation(A3.class);
		
		for(AnnotationField field : a.getFields()){
			if(field.getName().equals("value")){
				assertEquals(a3.value(), field.getValue(a3));
			}else if(field.getName().equals("bool1")){
				assertEquals(a3.bool1(), field.getValue(a3));
			}else if(field.getName().equals("bool2")){
				assertEquals(a3.bool2(), field.getValue(a3));
			}else if(field.getName().equals("bool3")){
				assertEquals(a3.bool3(), field.getValue(a3));
			}else if(field.getName().equals("ss")){
				assertEquals(a3.ss(), field.getValue(a3));
			}
		}
	}
	
}