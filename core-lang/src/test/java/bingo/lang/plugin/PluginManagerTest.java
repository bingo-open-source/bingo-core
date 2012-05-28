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

import static org.junit.Assert.*;

public class PluginManagerTest {

	@Test
	public void testSimplePlugin(){
		
		PluginManager<SimpleProvider, Plugin<SimpleProvider>> manager = 
			new PluginManager<SimpleProvider, Plugin<SimpleProvider>>(SimpleProvider.class);
		
		Plugin<SimpleProvider>[] plugins = manager.load();
		
		assertEquals(3,plugins.length);
		
		Plugin<SimpleProvider> plugin = manager.getPlugin("Simple1");
		SimpleProvider1 	   bean   = (SimpleProvider1)plugin.getBean();
		
		assertNotNull(bean);
		assertEquals(100, bean.getInt1());
		assertEquals(new Integer(200), bean.getInt2());
		assertTrue(bean.isBool1());
		assertNotNull(bean.getBool2());
		assertNull(bean.getBool3());
		
		plugin = manager.getPlugin("Simple2");
		bean   = (SimpleProvider1)plugin.getBean();
		
		assertNotNull(bean);
		assertEquals(200, bean.getInt1());
		assertEquals(new Integer(200), bean.getInt2());
		assertTrue(bean.isBool1());
		assertNotNull(bean.getBool2());
		assertNull(bean.getBool3());		
		
		plugin = manager.getPlugin("Simple3");
		bean   = (SimpleProvider1)plugin.getBean();
		
		assertNotNull(bean);
		assertEquals(100, bean.getInt1());
		assertEquals(new Integer(200), bean.getInt2());
		assertTrue(bean.isBool1());
		assertNotNull(bean.getBool2());
		assertNull(bean.getBool3());
	}

}