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
package bingo.lang.logging;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import bingo.lang.Strings;
import bingo.lang.testing.Perf;

@SuppressWarnings("deprecation")
public class FormatterTest {

    @Test
	public void testFormatsSimple(){
		
		assertEquals("", Formatter.format(null));
		assertEquals("", Formatter.format(null,"1","2"));
		
		assertEquals("Hello {}", Formatter.format("Hello {}"));
		assertEquals("Hello world", Formatter.format("Hello {}", "world"));
		assertEquals("Hello world", Formatter.format("Hello {}", "world","other"));
		
		assertEquals("Hello 'world'", Formatter.format("Hello '{}'","world"));
		assertEquals("Hello 'world'", Formatter.format("Hello '{}'","world","other"));
		
		assertEquals("Hello ''world''", Formatter.format("Hello ''{}''","world"));
		
		assertEquals("Hello {world}", Formatter.format("Hello {{}}","world"));
		assertEquals("Hello {{world}", Formatter.format("Hello {{{}}","world"));
		assertEquals("Hello {world}}", Formatter.format("Hello {{}}}","world"));
		assertEquals("Hello {{0,1}}", Formatter.format("Hello {{{},{}}}","0","1"));
	}
	
	@Test
	public void testFormatsMultiArgs(){
		assertEquals("Hello {},{}", Formatter.format("Hello {},{}"));
		assertEquals("Hello world1,world2", Formatter.format("Hello {},{}", "world1","world2"));
		
		assertEquals("Hello {world1,{world2", Formatter.format("Hello {{},{{}", "world1","world2"));
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testPerformanceCompares(){
		
		final String  arg1 = "name";
		final Integer arg2 = 100;
		
		int times = 100000;
		
		Perf.create("StringFormat", times)
		    .add("Formatter.format",new Runnable() {
				public void run() {
					String s = Formatter.format("argument '{}' must be large then {}",arg1,arg2);
				}
		    })	
		    .add("Strings.format",new Runnable() {
				public void run() {
					String s = Strings.format("argument '{0}' must be large then {1}",arg1,arg2);
				}
			})
			.run();	
	}
}
