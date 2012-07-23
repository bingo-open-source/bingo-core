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
package bingo.lang.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

public class SimpleLRUMapTest {

	@Test
	public void testSimple(){
		SimpleLRUMap<Object,Object> map = new SimpleLRUMap<Object, Object>(2);
		
		Object k1 = new Object();
		Object k2 = new Object();
		Object k3 = new Object();
		
		map.put(k1, Boolean.TRUE);
		map.put(k2, Boolean.TRUE);
		
		assertEquals(2,map.size());
		assertTrue(map.containsKey(k1));
		assertTrue(map.containsKey(k2));
		assertFalse(map.containsKey(k3));
		
		map.put(k3, Boolean.TRUE);
		assertEquals(2,map.size());
		assertFalse(map.containsKey(k1));
		assertTrue(map.containsKey(k2));
		assertTrue(map.containsKey(k3));
	}
	
	@Test
	public void testSynchronized(){
		Map<Object, Object> map = java.util.Collections.synchronizedMap(new SimpleLRUMap<Object, Object>(2));
		
		Object k1 = new Object();
		Object k2 = new Object();
		Object k3 = new Object();
		
		map.put(k1, Boolean.TRUE);
		map.put(k2, Boolean.TRUE);
		
		assertEquals(2,map.size());
		assertTrue(map.containsKey(k1));
		assertTrue(map.containsKey(k2));
		assertFalse(map.containsKey(k3));
		
		map.put(k3, Boolean.TRUE);
		assertEquals(2,map.size());
		assertFalse(map.containsKey(k1));
		assertTrue(map.containsKey(k2));
		assertTrue(map.containsKey(k3));
	}
}
