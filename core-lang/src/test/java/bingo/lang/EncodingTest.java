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

import static junit.framework.Assert.*;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * {@link TestCase} of {@link Encoding}
 */
public class EncodingTest {
	
	private static final String STR0 = "01xxbbcc_&$#@!*))~中 测";
	private static final String STR1 = "01xxbbcc_&$#@!*))~xxxkk";

	@Test
	public void testGetEncoding() {
		
		assertNotNull(Encoding.ISO_8859_1);
		assertNotNull(Encoding.US_ASCII);
		assertNotNull(Encoding.UTF_16);
		assertNotNull(Encoding.UTF_8);
		
		assertFalse(Strings.isEmpty(Encoding.ISO_8859_1.name()));
		assertFalse(Strings.isEmpty(Encoding.US_ASCII.name()));
		assertFalse(Strings.isEmpty(Encoding.UTF_16.name()));
		assertFalse(Strings.isEmpty(Encoding.UTF_8.name()));
		
		assertNotNull(Encoding.ISO_8859_1.charset());
		assertNotNull(Encoding.US_ASCII.charset());
		assertNotNull(Encoding.UTF_16.charset());
		assertNotNull(Encoding.UTF_8.charset());
		
		assertEquals(Encoding.ISO_8859_1.name(), Encoding.ISO_8859_1.charset().name());
		assertEquals(Encoding.US_ASCII.name(), Encoding.US_ASCII.charset().name());
		assertEquals(Encoding.UTF_16.name(), Encoding.UTF_16.charset().name());
		assertEquals(Encoding.UTF_8.name(), Encoding.UTF_8.charset().name());
	}
	
	@Test
	public void tetGetBytes() throws Exception {
		assertTrue(java.util.Arrays.equals(STR0.getBytes(Encoding.ISO_8859_1.name()),Encoding.ISO_8859_1.getBytes(STR0)));
		assertTrue(java.util.Arrays.equals(STR0.getBytes(Encoding.US_ASCII.name()),Encoding.US_ASCII.getBytes(STR0)));
		assertTrue(java.util.Arrays.equals(STR0.getBytes(Encoding.UTF_16.name()),Encoding.UTF_16.getBytes(STR0)));
		assertTrue(java.util.Arrays.equals(STR0.getBytes(Encoding.UTF_8.name()),Encoding.UTF_8.getBytes(STR0)));
	}
	
	@Test
	public void tetGetString() throws Exception {
		assertEquals(STR1, Encoding.ISO_8859_1.getString(STR1.getBytes(Encoding.ISO_8859_1.name())));
		assertEquals(STR1, Encoding.US_ASCII.getString(STR1.getBytes(Encoding.US_ASCII.name())));
		assertEquals(STR0, Encoding.UTF_16.getString(STR0.getBytes(Encoding.UTF_16.name())));
		assertEquals(STR0, Encoding.UTF_8.getString(STR0.getBytes(Encoding.UTF_8.name())));
	}	
}
