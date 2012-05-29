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
package bingo.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import bingo.lang.testing.junit.ConcurrentTestCase;

/**
 * {@link TestCase} of {@link Arrays}
 */
public class ArraysTest extends ConcurrentTestCase {
	
	@Test
	public void testConstructor(){
		new Arrays();
		new SubArrays();
		
        Constructor<?>[] cons = Arrays.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertEquals(true, Modifier.isProtected(cons[0].getModifiers()));
        assertEquals(true, Modifier.isPublic(Arrays.class.getModifiers()));
        assertEquals(false, Modifier.isFinal(Arrays.class.getModifiers()));		
	}
	
	@Test
	public void testToList(){
		List<?> list;
		
		list = Arrays.toList((Object[])null);
		assertNotNull(list);
		assertEquals(0, list.size());
		
		Object[] array = new Object[]{1,"s",new Object()};
		
		list = Arrays.toList(array);
		assertNotNull(list);
		assertEquals(array.length, list.size());
		
		Assert.assertArrayEquals(array, list.toArray());
	}
	
	@Test
	public void testToStringArray(){
		String[] strings;
		
		strings = Arrays.toStringArray((Object[])null);
		
		assertNotNull(strings);
		assertEquals(0, strings.length);
		
		Object[] array = new Object[]{1,"s",new Object()};
		
		strings = Arrays.toStringArray(array);
		assertNotNull(strings);
		assertEquals(array.length, strings.length);
		assertEquals("1", strings[0]);
		assertEquals("s", strings[1]);
		assertEquals(array[2].toString(), strings[2]);
	}
	
	@Test
	public void testConcat(){
		Integer[] a = new Integer[]{};
		Integer[] b = new Integer[]{};
		Integer[] c = null;
		
		assertEquals(0, Arrays.concat(a, b).length);
		
		a = new Integer[]{1,2,3};
		b = new Integer[]{};	
		
		assertEquals(3, Arrays.concat(a, b).length);
		assertTrue(Arrays.equals(a,Arrays.concat(a, b)));
		
		a = new Integer[]{};
		b = new Integer[]{1,2,3};
		
		assertEquals(3, Arrays.concat(a, b).length);
		assertTrue(Arrays.equals(b,Arrays.concat(a, b)));	
		
		a = new Integer[]{1,2,3};
		b = new Integer[]{4,5,6};
		c = new Integer[]{1,2,3,4,5,6};
		
		assertEquals(6, Arrays.concat(a, b).length);
		assertTrue(Arrays.equals(c,Arrays.concat(a, b)));
	}
	
	private static class SubArrays extends Arrays {
		public SubArrays(){
			super();
		}
	}
}
