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
package bingo.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;

import org.junit.Assert;

import junit.framework.TestCase;

/**
 * {@link TestCase} of {@link Arrays}
 * 
 * @author fenghm (fenghm@bingosoft.net)
 * 
 * @since 0.1
 */
public class ArraysTest extends TestCase {

	public void testConstructor(){
		new Arrays();
		new SubArrays();
		
        Constructor<?>[] cons = Arrays.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertEquals(true, Modifier.isProtected(cons[0].getModifiers()));
        assertEquals(true, Modifier.isPublic(Arrays.class.getModifiers()));
        assertEquals(false, Modifier.isFinal(Arrays.class.getModifiers()));		
	}
	
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
	
	private static class SubArrays extends Arrays {
		public SubArrays(){
			super();
		}
	}
}
