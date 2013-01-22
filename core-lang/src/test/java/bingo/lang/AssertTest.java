/*
 * Copyright 2002-2006 the original author or authors.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import bingo.lang.testing.junit.ConcurrentTestCase;


/**
 * Unit tests for the {@link Assert} class.
 */
@SuppressWarnings("unchecked")
public class AssertTest extends ConcurrentTestCase {
	
	@Test
	public void testIsEquals(){
		Assert.isEquals("s", "s", "xxx");
		Assert.isEqualsIgnorecase("s", "S", "xxx");
		
		try{
			Assert.isEquals("s","S","xxx");
			fail();
		}catch(IllegalArgumentException e){
			
		}
		
		try{
			Assert.isEqualsIgnorecase("s","Sx","xxx");
			fail();
		}catch(IllegalArgumentException e){
			
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void instanceOf() {
		final Set set = new HashSet();
		Assert.isInstanceOf(HashSet.class, set);
		Assert.isInstanceOf(HashMap.class, set);
	}

	@Test
	public void isNullDoesNotThrowExceptionIfArgumentIsNullWithMessage() {
		Assert.isNull(null, "Bla");
	}

	@Test
	public void isNullDoesNotThrowExceptionIfArgumentIsNull() {
		Assert.isNull(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void isNullThrowsExceptionIfArgumentIsNotNull() {
		Assert.isNull(new Object());
	}

	@Test(expected = IllegalArgumentException.class)
	public void isTrueWithFalseExpressionThrowsException() throws Exception {
		Assert.isTrue(false);
	}

	@Test
	public void isTrueWithTrueExpressionSunnyDay() throws Exception {
		Assert.isTrue(true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHasLengthWithNullStringThrowsException() throws Exception {
		Assert.notEmpty((String)null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void hasLengthWithEmptyStringThrowsException() throws Exception {
		Assert.notEmpty("");
	}

	@Test
	public void hasLengthWithWhitespaceOnlyStringDoesNotThrowException() throws Exception {
		Assert.notEmpty("\t  ");
	}

	@Test
	public void hasLengthSunnyDay() throws Exception {
		Assert.notEmpty("I Heart ...");
	}

	@Test
	public void doesNotContainWithNullSearchStringDoesNotThrowException() throws Exception {
		Assert.notContains(null, "rod");
	}

	@Test
	public void doesNotContainWithNullSubstringDoesNotThrowException() throws Exception {
		Assert.notContains("A cool chick's name is Brod. ", null);
	}

	@Test
	public void doesNotContainWithEmptySubstringDoesNotThrowException() throws Exception {
		Assert.notContains("A cool chick's name is Brod. ", "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void assertNotEmptyWithNullCollectionThrowsException() throws Exception {
		Assert.notEmpty((Collection) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void assertNotEmptyWithEmptyCollectionThrowsException() throws Exception {
		Assert.notEmpty(new ArrayList());
	}

	@Test
	public void assertNotEmptyWithCollectionSunnyDay() throws Exception {
		List<String> collection = new ArrayList<String>();
		collection.add("");
		Assert.notEmpty(collection);
	}

	@Test(expected = IllegalArgumentException.class)
	public void assertNotEmptyWithNullMapThrowsException() throws Exception {
		Assert.notEmpty((Map) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void assertNotEmptyWithEmptyMapThrowsException() throws Exception {
		Assert.notEmpty(new HashMap());
	}

	@Test
	public void assertNotEmptyWithMapSunnyDay() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("", "");
		Assert.notEmpty(map);
	}

	@Test(expected = IllegalArgumentException.class)
	public void isInstanceofClassWithNullInstanceThrowsException() throws Exception {
		Assert.isInstanceOf(String.class, null);
	}

	@Test(expected = IllegalStateException.class)
	public void stateWithFalseExpressionThrowsException() throws Exception {
		Assert.isValidState(false);
	}

	@Test
	public void stateWithTrueExpressionSunnyDay() throws Exception {
		Assert.isValidState(true);
	}
}
