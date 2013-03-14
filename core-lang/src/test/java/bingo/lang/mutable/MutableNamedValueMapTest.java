/*
 * Copyright 2013 the original author or authors.
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
package bingo.lang.mutable;

import org.junit.Test;

import junit.framework.Assert;
import bingo.lang.NamedEntry;
import bingo.lang.collections.MutableNamedValueMap;

public class MutableNamedValueMapTest extends Assert{

	@Test
	public void testSetAndGet(){
		MutableNamedValueMap<NamedEntry<Object>> map = new MutableNamedValueMap<NamedEntry<Object>>();
		
		map.put("a", "1");
		map.put("b", "2");

		assertEquals("1", map.get("a"));
		assertEquals("1", map.get("A"));
		assertEquals("2", map.get("b"));
		
		map.put("A", "2");
		assertEquals("2", map.get("a"));
	}
	
}
