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
package bingo.utils.http;

import org.junit.Test;

import bingo.lang.Enumerable;
import bingo.utils.http.HttpHeader.HeaderElement;

import static org.junit.Assert.*;

public class TestHttpHeader {

	@Test
    public void testAccept() throws Exception{
		String value = "text/html, application/xhtml+xml;charset=utf-8, */*";
		
    	HttpHeader accept = new HttpHeader(HttpHeaders.ACCEPT, value);
    	
    	Enumerable<HeaderElement> elements = accept.getElements();

    	assertEquals(3, elements.size());
    	
    	assertEquals("text/html", elements.get(0).getName());
    	assertNull(elements.get(0).getValue());
    	
    	assertEquals("application/xhtml+xml", elements.get(1).getName());
    	assertNull(elements.get(1).getValue());   	
    	assertEquals(1,elements.get(1).getParameters().size());
    	assertEquals("utf-8",elements.get(1).getParameter("charset")); 
    	
    	assertEquals("*/*", elements.get(2).getName());
    	assertNull(elements.get(2).getValue());
    }
	
}
