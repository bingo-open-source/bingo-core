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

import static org.junit.Assert.*;

import org.junit.Test;

import bingo.lang.Reflects;
import bingo.lang.exceptions.ReflectException;

public class ReflectNewInstanceTest {

	@Test
	@SuppressWarnings("unused")
	public void testNonPublic(){
		class A
		{
			Object	o1, o2, o3, o4;
		}
		
		ReflectClass<A> clazz = ReflectClass.get(A.class);
		
		A a = clazz.newInstance();
		
		assertNotNull(a);
		
		class B {
			private final int i;
			
			private B(int i){
				this.i = i;
			}
		}
		
		
		B b = Reflects.newInstanceWithoutCallingConstructor(B.class);
		
		assertNotNull(b);
		
		try {
	        Reflects.newInstance(B.class);
	        
	        fail("should throw ReflectException");
        } catch (ReflectException e) {
        	assertTrue(e.getMessage().contains("no default constructor"));
        }
        
		try {
	        Reflects.newInstance(B.class);
	        
	        fail("should throw ReflectException");
        } catch (ReflectException e) {
        	assertTrue(e.getMessage().contains("no default constructor"));
        }
	}
	
	@Test
	public void testStaticInnerClass(){
		assertNotNull(Reflects.newInstance(PrivateStaticInnerClass.class));
		assertNotNull(Reflects.newInstance(DefaultStaticInnerClass.class));
		assertNotNull(Reflects.newInstance(PublicStaticInnerClass.class));
	}
	
	@Test
	public void testNonStaticInnerClass(){
		assertNotNull(Reflects.newInstance(PrivateInnerClass.class));
		assertNotNull(Reflects.newInstance(DefaultInnerClass.class));
		assertNotNull(Reflects.newInstance(PublicInnerClass.class));
	}	
	
	private static class PrivateStaticInnerClass {
		
	}
	
	static class DefaultStaticInnerClass {
		
	}
	
	public static class PublicStaticInnerClass {
		
	}
	
	private class PrivateInnerClass {
		
	}
	
	private class DefaultInnerClass {
		
	}
	
	private class PublicInnerClass {
		
	}
}
