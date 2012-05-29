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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import bingo.lang.Reflects;
import bingo.lang.Types;
import bingo.lang.testing.junit.ConcurrentTestCase;

public class ReflectMethodTest extends ConcurrentTestCase {

	@Test
	public void testGetParameters(){
		
		ReflectClass<GetParameterBean> clazz = Reflects.forType(GetParameterBean.class);
		
		ReflectMethod m1 = clazz.getMethod("staticMethod1");
		
		assertNotNull(m1.getParameters());
		assertEquals(0, m1.getParameters().length);
		
		ReflectMethod m2 = clazz.getMethod("staticMethod2");
		
		assertEquals(3, m2.getParameters().length);
		
		assertEquals(1, m2.getParameters()[0].getIndex());
		assertEquals("p1", m2.getParameters()[0].getName());
		
		assertEquals(2, m2.getParameters()[1].getIndex());
		assertEquals("xxx", m2.getParameters()[1].getName());
		
		assertEquals(3, m2.getParameters()[2].getIndex());
		assertEquals("list", m2.getParameters()[2].getName());
		assertEquals(String.class, Types.getActualTypeArgument(m2.getParameters()[2].getGenericType()));
		
		ReflectMethod m3 = clazz.getMethod("test1");
		assertEquals(0, m3.getParameters().length);
		
		ReflectMethod m4 = clazz.getMethod("test2");
		
		assertEquals(2, m4.getParameters().length);
		assertEquals("p", m4.getParameters()[0].getName());
		assertEquals("clazz", m4.getParameters()[1].getName());
	}
	
	@SuppressWarnings("unused")
	private static class GetParameterBean {
		
		private static void staticMethod1() {
			
		}
		
		private static void staticMethod2(int p1,String xxx,List<String> list){
			
		}
		
		public int test1(){
			return 0;
		}
		
		public int test2(String p,Class<?> clazz){
			return 0;
		}
	}
}
