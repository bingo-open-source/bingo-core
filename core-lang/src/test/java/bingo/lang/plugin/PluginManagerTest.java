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
package bingo.lang.plugin;

import org.junit.Test;

import bingo.lang.Arrays;
import bingo.lang.testing.junit.ConcurrentTestCase;

import static org.junit.Assert.*;

public class PluginManagerTest extends ConcurrentTestCase {

	@Test
	public void testSimplePlugin(){
		PluginManager<SimplePlugin> manager = new PluginManager<SimplePlugin>(SimplePlugin.class);
		
		Plugin[] plugins = manager.load();
		
		assertEquals(3,plugins.length);
		
		SimplePlugin1 plugin = (SimplePlugin1)manager.getPlugin("Simple1");
		
		assertNotNull(plugin);
		assertEquals(100, plugin.getInt1());
		assertEquals(new Integer(200), plugin.getInt2());
		assertTrue(plugin.isBool1());
		assertNotNull(plugin.getBool2());
		assertNull(plugin.getBool3());
		assertTrue(Arrays.equals(new int[]{1,2,3}, plugin.getArray1()));
		assertNotNull(plugin.getMap1());
		assertEquals(2, plugin.getMap1().size());
		assertEquals("v1", plugin.getMap1().get("k1"));
		
		plugin = (SimplePlugin1)manager.getPlugin("Simple2");
		
		assertNotNull(plugin);
		assertEquals(200, plugin.getInt1());
		assertEquals(new Integer(200), plugin.getInt2());
		assertTrue(plugin.isBool1());
		assertNotNull(plugin.getBool2());
		assertNull(plugin.getBool3());		
		
		plugin = (SimplePlugin1)manager.getPlugin("Simple3");
		
		assertNotNull(plugin);
		assertEquals(100, plugin.getInt1());
		assertEquals(new Integer(200), plugin.getInt2());
		assertTrue(plugin.isBool1());
		assertNotNull(plugin.getBool2());
		assertNull(plugin.getBool3());
	}
}