/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Date;

import bingo.lang.exceptions.ParseException;
import bingo.lang.time.DateFormats;

//from apache commons-lang3

/**
 * <p>
 * A suite of utilities surrounding the use of the {@link java.util.Calendar} and {@link java.util.Date} object.
 * </p>
 */
public class Dates {

	/**
	 * Number of milliseconds in a standard second.
	 */
	public static final long	MILLIS_PER_SECOND	= 1000;

	/**
	 * Number of milliseconds in a standard minute.
	 */
	public static final long	MILLIS_PER_MINUTE	= 60 * MILLIS_PER_SECOND;

	/**
	 * Number of milliseconds in a standard hour.
	 */
	public static final long	MILLIS_PER_HOUR	  = 60 * MILLIS_PER_MINUTE;

	/**
	 * Number of milliseconds in a standard day.
	 */
	public static final long	MILLIS_PER_DAY	  = 24 * MILLIS_PER_HOUR;

	protected Dates() {

	}
	
	public static String format(Date date) {
		return DateFormats.getFormat(date.getClass()).format(date);
	}
	
	public static String format(Date date,String pattern) {
		return DateFormats.getFormat(pattern).format(date);
	}	
	
	public static Date parse(String string) throws ParseException {
		return parse(string,DateFormats.DEFAULT_PATTERNS,true,false);
	}
	
	public static Date parseOrNull(String string) {
        return parse(string,DateFormats.DEFAULT_PATTERNS,true,true);
	}
	
	public static Date parse(String string,String... patterns) throws ParseException {
		return parse(string,patterns,true,false);
	}
	
	public static Date parseOrNull(String string,String... patterns) {
		return parse(string,patterns,true,true);
	}

	/**
	 * <p>
	 * Parses a string representing a date by trying a variety of different parsers.
	 * </p>
	 * 
	 * <p>
	 * The parse will try each parse pattern in turn. A parse is only deemed successful if it parses the whole of the
	 * input string. If no parse patterns match, a ParseException is thrown.
	 * </p>
	 * 
	 * @param string the date to parse, not null
	 * @param patterns the date format patterns to use, see SimpleDateFormat, not null
	 * @param lenient Specify whether or not date/time parsing is to be lenient.
	 * @return the parsed date
	 * @throws IllegalArgumentException if the date string or pattern array is null
	 * @throws ParseException if none of the date patterns were suitable
	 * @see java.util.Calender#isLenient()
	 */
	private static Date parse(String string, String[] patterns, boolean lenient,boolean returnNull) throws ParseException {
		if (string == null || patterns == null) {
			throw new IllegalArgumentException("Date and Patterns must not be null");
		}

		DateFormat parser = null; 
		ParsePosition pos = new ParsePosition(0);
		for (String parsePattern : patterns) {

			String pattern = parsePattern;

			// LANG-530 - need to make sure 'ZZ' output doesn't get passed to SimpleDateFormat
			if (parsePattern.endsWith("ZZ")) {
				pattern = pattern.substring(0, pattern.length() - 1);
			}
			
			parser = DateFormats.getFormat(pattern);

			//parser.applyPattern(pattern);
			pos.setIndex(0);

			String str2 = string;
			// LANG-530 - need to make sure 'ZZ' output doesn't hit SimpleDateFormat as it will ParseException
			if (parsePattern.endsWith("ZZ")) {
				str2 = string.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2");
			}

			Date date = parser.parse(str2, pos);
			if (date != null && pos.getIndex() == str2.length()) {
				return date;
			}
		}
		
		if(!returnNull){
			throw new bingo.lang.exceptions.ParseException("Unable to parse the date: {0}",string);	
		}
		
		return null;
	}
}