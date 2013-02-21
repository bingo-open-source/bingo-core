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
package bingo.lang.uri;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import bingo.lang.testing.junit.ConcurrentTestCase;

public class UriPatternTest extends ConcurrentTestCase {

	@Test
	public void testSingleParam(){
		Map<String,String> params = new HashMap<String, String>();
		assertTrue(UriPattern.compile("/a/{p1}").matches("/a/b",params));
		assertEquals("b",params.get("p1"));
		
		assertTrue(UriPattern.compile("/a/{p1:.+}").matches("/a/b",params));
		assertEquals("b",params.get("p1"));
		
		assertTrue(UriPattern.compile("/a/{p1:[^/]+}/?.*").matches("/a/b",params));
		assertEquals("b",params.get("p1"));
		
		assertTrue(UriPattern.compile("/a/{p1:[^/]+}/?.*").matches("/a/b/",params));
		assertEquals("b",params.get("p1"));
		
		assertTrue(UriPattern.compile("/a/{p1:[^/]+}/?.*").matches("/a/b/xx",params));
		assertEquals("b",params.get("p1"));		
		
		assertTrue(UriPattern.compile("/a/{p1: [^/]+}/?.*").matches("/a/b/xx",params));
		assertEquals("b",params.get("p1"));	
		
		assertTrue(UriPattern.compile("/a/{p1}/?.*").matches("/a/b/xx",params));
		assertEquals("b",params.get("p1"));
		
		assertTrue(UriPattern.compile("/a/{p1:[a|a\\([^/]+\\)]+}/?.*").matches("/a/a(1)/xx",params));
		assertEquals("a(1)",params.get("p1"));		
	}
}
