/**
 * This file created at May 8, 2012.
 *
 * Copyright (c) 2002-2012 Bingosoft, Inc. All rights reserved.
 */
package bingo.lang;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bingo.lang.exceptions.ParseException;

/**
 * <code>{@link DatesTest}</code>
 *
 * TODO : document me
 *
 * @author Calvin Chen
 */
public class DatesTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link bingo.lang.Dates#format(java.util.Date)}.
	 */
	@Test
	public void testFormatDate() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link bingo.lang.Dates#format(java.util.Date, java.lang.String)}.
	 */
	@Test
	public void testFormatDateString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link bingo.lang.Dates#parse(java.lang.String)}.
	 */
	@Test
	public void testParseString() {
		assertNotNull(Dates.parse("2012-05-08"));
		assertNotNull(Dates.parse("2000-01-01"));
		try {
			Dates.parse("2012-13-08");
			fail();
		} catch (ParseException e2) {
		}
		try {
			Dates.parse("2012-05-33");
			fail();
		} catch (ParseException e1) {
		}
		try {
			Dates.parse("0000-05-08");
			fail();
		} catch (ParseException e) {
		}
		assertNotNull(Dates.parse("2012-5-8"));
		try {
			Dates.parse("2012-05");
			fail();
		} catch (ParseException e) {
		}
		
//		assertNotNull(Dates.parse("11:16:01"));
//		assertNotNull(Dates.parse("01:01:01"));
		
//		assertNotNull(Dates.parse("2012-05-10 14:56:02"));
	}

	/**
	 * Test method for {@link bingo.lang.Dates#parseOrNull(java.lang.String)}.
	 */
	@Test
	public void testParseOrNullString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link bingo.lang.Dates#parse(java.lang.String, java.lang.String[])}.
	 */
	@Test
	public void testParseStringStringArray() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link bingo.lang.Dates#parseOrNull(java.lang.String, java.lang.String[])}.
	 */
	@Test
	public void testParseOrNullStringStringArray() {
		fail("Not yet implemented"); // TODO
	}

}
