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
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;

import org.junit.Ignore;
import org.junit.Test;

import bingo.lang.Reflects;
import bingo.lang.testing.Perf;
import bingo.lang.testing.junit.ConcurrentIgnore;
import bingo.lang.testing.junit.ConcurrentTestCase;

@SuppressWarnings("unused")
public class ReflectAccessorTest extends ConcurrentTestCase {
	
	@Test
	@ConcurrentIgnore
	public void testStaticField(){
		ReflectAccessor accessor = ReflectAccessor.createFor(Bean.class);
		
		int index = accessor.getFieldIndex("STATIC_FIELD");
		
		assertEquals(new Integer(0),accessor.getField(null, index));
		assertEquals(new Integer(0),accessor.getField(new Bean(), index));
		
		accessor.setField(null, index, 10);
		assertEquals(new Integer(10),accessor.getField(null, index));
		
		accessor.setField(new Bean(), index, 100);
		assertEquals(new Integer(100),accessor.getField(null, index));
	}
	
	@Test
	public void testInstanceField(){
		ReflectAccessor accessor = ReflectAccessor.createFor(Bean.class);
		
		int index = accessor.getFieldIndex("instanceField");
		
		Bean bean = new Bean();
		
		assertEquals(new Integer(0),accessor.getField(bean, index));
		
		accessor.setField(bean, index, 10);
		assertEquals(new Integer(10),accessor.getField(bean, index));
		
		accessor.setField(bean, index, 100);
		assertEquals(new Integer(100),accessor.getField(bean, index));
	}
	
	@Test
	@ConcurrentIgnore
	public void testGetArrayLengthObjectType(){
		final ReflectAccessor accessor = ReflectAccessor.createFor(Bean.class);
		
		final Bean[] array = new Bean[]{new Bean(),new Bean(),new Bean()};
		
		Perf.create("GetArrayLength(Object[])", 1000000)
		    .add("Accessor.getArrayLength", new Runnable() {
				public void run() {
					int i = accessor.getArrayLength(array);
				}
			})
		    .add("Handcode.getArrayLength", new Runnable() {
				public void run() {
					int i = array.length;
				}
			})
		    .add("Reflects.getLength", new Runnable() {
				public void run() {
					int i = ReflectClass.get(array.getClass().getComponentType()).getArrayLength(array);
				}
			})
		    .add("Array.getLength(native)", new Runnable() {
				public void run() {
					int i = Array.getLength(array);
				}
			})		
			.run();
	}
	
	@Test
	@ConcurrentIgnore
	public void testGetArrayLength(){
		final ReflectAccessor accessor = ReflectAccessor.createFor(Integer.TYPE);
		
		final int[] array = new int[]{1,2,3};
		
		Perf.create("GetArrayLength(int[])", 1000000)
		    .add("Accessor.getArrayLength", new Runnable() {
				public void run() {
					int i = accessor.getArrayLength(array);
				}
			})
		    .add("Handcode.getArrayLength", new Runnable() {
				public void run() {
					int i = array.length;
				}
			})
		    .add("Reflects.getLength", new Runnable() {
				public void run() {
					int i = ReflectClass.get(array.getClass().getComponentType()).getArrayLength(array);
				}
			})
		    .add("Array.getLength(native)", new Runnable() {
				public void run() {
					int i = Array.getLength(array);
				}
			})		
			.run();		
	}	
	
	@Test
	@ConcurrentIgnore
	public void testGetArrayItem(){
		final ReflectAccessor accessor = ReflectAccessor.createFor(Integer.TYPE);
		
		final int[] array = new int[]{1,2,3};
		
		Perf.create("GetArrayItem(int[])", 1000000)
		    .add("Accessor.getArrayItem", new Runnable() {
				public void run() {
					int i = (Integer)accessor.getArrayItem(array, 1);
				}
			})
		    .add("Handcode.getArrayItem", new Runnable() {
				public void run() {
					int i = array[1];
				}
			})
		    .add("Reflects.getArrayItem", new Runnable() {
				public void run() {
					int i = (Integer)ReflectClass.get(array.getClass().getComponentType()).getArrayItem(array,1);
				}
			})
		    .add("Array.get(native)", new Runnable() {
				public void run() {
					int i = (Integer)Array.get(array, 1);
				}
			})		
			.run();
	}
	
	@Test
	@ConcurrentIgnore
	public void testSetArrayItem(){
		final ReflectAccessor accessor = ReflectAccessor.createFor(Integer.TYPE);
		
		final int[] array = new int[]{1,2,3};
		
		Perf.create("SetArrayItem(int[])", 1000000)
		    .add("Accessor.setArrayItem", new Runnable() {
				public void run() {
					accessor.setArrayItem(array, 1, 1);
				}
			})
		    .add("Handcode.setArrayItem", new Runnable() {
				public void run() {
					array[1] = 1;
				}
			})
		    .add("Reflects.setArrayItem", new Runnable() {
				public void run() {
					ReflectClass.get(array.getClass().getComponentType()).setArrayItem(array, 1, 1);
				}
			})
		    .add("Array.set(native)", new Runnable() {
				public void run() {
					Array.set(array, 1, 1);
				}
			})		
			.run();
	}
	
	@Test
	@ConcurrentIgnore
	public void testNewArray(){
		final ReflectAccessor accessor = ReflectAccessor.createFor(Bean.class);
		
		assertNotNull(accessor.newArray(0));
		assertTrue(accessor.newArray(0).getClass().isArray());
		assertEquals(Bean.class,accessor.newArray(0).getClass().getComponentType());
		assertEquals(0,Array.getLength(accessor.newArray(0)));
		assertEquals(10,Array.getLength(accessor.newArray(10)));
		
		Perf.create("NewArray", 1000000)
		    .add("Accessor.newArray", new Runnable() {
				public void run() {
					Bean[] array = (Bean[])accessor.newArray(10);
				}
			})
		    .add("Handcode.newArray", new Runnable() {
				public void run() {
					Bean[] array = new Bean[10];
				}
			})
		    .add("Reflects.newArray", new Runnable() {
				public void run() {
					Bean[] array = Reflects.newArray(Bean.class, 10);
				}
			})
		    .add("Array.newInstance(native)", new Runnable() {
				public void run() {
					Bean[] array = (Bean[])Array.newInstance(Bean.class, 10);
				}
			})		
			.run();
	}
	
	@Test
	public void testNewArrayOfInterface(){
		testNewArray(IBean.class);
	}
	
	@Test
	public void testNewArrayOfPrimitives(){
		testNewArray(Integer.class);
		testNewArray(Integer.TYPE);
	}	
	
	private static void testNewArray(Class<?> type){
		ReflectAccessor accessor = ReflectAccessor.createFor(type);
		
		assertNotNull(accessor.newArray(0));
		assertTrue(accessor.newArray(0).getClass().isArray());
		assertEquals(type,accessor.newArray(0).getClass().getComponentType());
		assertEquals(0,Array.getLength(accessor.newArray(0)));
		assertEquals(10,Array.getLength(accessor.newArray(10)));
	}
	
	static class Bean {
		public static int STATIC_FIELD = 0;
		
		public int instanceField = 0;
	}
	
	static interface IBean {
		
	}
}
