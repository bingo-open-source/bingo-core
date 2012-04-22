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

import junit.framework.TestCase;

import static bingo.utils.StringsTest.*;

/**
 * {@link TestCase} of {@link Strings}
 * 
 * @author fenghm (fenghm@bingosoft.net)
 * 
 * @since 0.1
 */
public class StringsEqualsTest extends TestCase {

	public void testEquals() {
		assertEquals(true, Strings.equals(null, null));
		assertEquals(true, Strings.equals(FOO, FOO));
		assertEquals(true, Strings.equals(foo, new String(new char[] { 'f', 'o', 'o' })));
		assertEquals(false, Strings.equals(FOO, new String(new char[] { 'f', 'O', 'O' })));
		assertEquals(false, Strings.equals(FOO, BAR));
		assertEquals(false, Strings.equals(FOO, null));
		assertEquals(false, Strings.equals(null, FOO));
	}

	public void testEqualsIgnoreCase() {
		assertEquals(true, Strings.equalsIgnoreCase(null, null));
		assertEquals(true, Strings.equalsIgnoreCase(FOO, FOO));
		assertEquals(true, Strings.equalsIgnoreCase(FOO, new String(new char[] { 'f', 'o', 'o' })));
		assertEquals(true, Strings.equalsIgnoreCase(FOO, new String(new char[] { 'f', 'O', 'O' })));
		assertEquals(false, Strings.equalsIgnoreCase(FOO, BAR));
		assertEquals(false, Strings.equalsIgnoreCase(FOO, null));
		assertEquals(false, Strings.equalsIgnoreCase(null, FOO));
	}

}
