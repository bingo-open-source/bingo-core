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

import static junit.framework.Assert.assertEquals;

import java.text.MessageFormat;

import junit.framework.TestCase;

import org.junit.Test;

import bingo.lang.testing.Perf;

/**
 * {@link TestCase} of {@link Strings#format(String, Object...)}
 */
public class StringsFormatTest {

	@Test
	public void testFormatsSimple(){
		
		assertEquals("", Strings.format(null));
		assertEquals("", Strings.format(null,"1","2"));
		
		assertEquals("Hello {0}", Strings.format("Hello {0}"));
		assertEquals("Hello world", Strings.format("Hello {0}", "world"));
		assertEquals("Hello world", Strings.format("Hello {0}", "world","other"));
		
		assertEquals("Hello 'world'", Strings.format("Hello '{0}'","world"));
		assertEquals("Hello 'world'", Strings.format("Hello '{0}'","world","other"));
		
		assertEquals("Hello ''world''", Strings.format("Hello ''{0}''","world"));
		
		assertEquals("Hello {world}", Strings.format("Hello {{0}}","world"));
		assertEquals("Hello {{world}", Strings.format("Hello {{{0}}","world"));
		assertEquals("Hello {world}}", Strings.format("Hello {{0}}}","world"));
		assertEquals("Hello {{0,1}}", Strings.format("Hello {{{0},{1}}}","0","1"));
	}
	
	@Test
	public void testFormatsMultiArgs(){
		
		assertEquals("Hello {0},{1}", Strings.format("Hello {0},{1}"));
		assertEquals("Hello world1,world2", Strings.format("Hello {0},{1}", "world1","world2"));
		assertEquals("Hello world2,world1", Strings.format("Hello {1},{0}", "world1","world2"));
		
		assertEquals("Hello {world1,{world2", Strings.format("Hello {{0},{{1}", "world1","world2"));
		assertEquals("Hello {world2},}world1{", Strings.format("Hello {{1}},}{0}{", "world1","world2"));		
	}	
	
	@SuppressWarnings("unused")
	@Test
	public void testPerformanceCompares(){
		
		final String  arg1 = "name";
		final Integer arg2 = 100;
		
		int times = 100000;
		
		Perf.run("String +",new Action() {
			public void execute() {
				String s ="argument '" + arg1 + "' must be large then " + arg2;
			}
		},times);
		
		Perf.run("MessageFormat.format",new Action() {
			public void execute() {
				String s = MessageFormat.format("argument '{0}' must be large then {1}",arg1,arg2);
			}
		},times);	
		
		Perf.run("Strings.format",new Action() {
			public void execute() {
				String s = Strings.format("argument '{0}' must be large then {1}",arg1,arg2);
			}
		},times);			
		
	}
}
