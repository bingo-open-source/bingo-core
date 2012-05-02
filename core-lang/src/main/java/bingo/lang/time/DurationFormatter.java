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
package bingo.lang.time;

import java.util.ArrayList;

import bingo.lang.Dates;
import bingo.lang.Strings;

//from apache commons-lang

/**
 * <p>
 * Duration formatting utilities and constants. The following table describes the tokens used in the pattern language
 * for formatting.
 * </p>
 * 
 * <table border="1">
 *  <tr><th>character</th><th>duration element</th></tr>
 *  <tr><td>y</td><td>years</td></tr>
 *  <tr><td>M</td><td>months</td></tr>
 *  <tr><td>d</td><td>days</td></tr>
 *  <tr><td>H</td><td>hours</td></tr>
 *  <tr><td>m</td><td>minutes</td></tr>
 *  <tr><td>s</td><td>seconds</td></tr>
 *  <tr><td>S</td><td>milliseconds</td></tr>
 * </table>
 */
public class DurationFormatter {
	
	private static final Object	y	= "y";
	private static final Object	M	= "M";
	private static final Object	d	= "d";
	private static final Object	H	= "H";
	private static final Object	m	= "m";
	private static final Object	s	= "s";
	private static final Object	S	= "S";

	/**
	 * <p>
	 * DurationFormatUtils instances should NOT be constructed in standard programming.
	 * </p>
	 * 
	 * <p>
	 * This constructor is public to permit tools that require a JavaBean instance to operate.
	 * </p>
	 */
	protected DurationFormatter() {
		super();
	}

	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Formats the time gap as a string.
	 * </p>
	 * 
	 * <p>
	 * The format used is ISO8601-like: <i>H</i>:<i>m</i>:<i>s</i>.<i>S</i>.
	 * </p>
	 * 
	 * @param durationMillis the duration to format
	 * @return the formatted duration, not null
	 */
	public static String formatHMS(long durationMillis) {
		return format(durationMillis, "H:mm:ss.SSS");
	}

	/**
	 * <p>
	 * Formats the time gap as a string, using the specified format, and padding with zeros and using the default
	 * timezone.
	 * </p>
	 * 
	 * <p>
	 * This method formats durations using the days and lower fields of the format pattern. Months and larger are not
	 * used.
	 * </p>
	 * 
	 * @param durationMillis the duration to format
	 * 
	 * @param format the way in which to format the duration, not null
	 * 
	 * @return the formatted duration, not null
	 */
	public static String format(long durationMillis, String format) {
		return format(durationMillis, format, true);
	}

	/**
	 * <p>
	 * Formats the time gap as a string, using the specified format. Padding the left hand side of numbers with zeroes
	 * is optional and the timezone may be specified.
	 * </p>
	 * 
	 * <p>
	 * This method formats durations using the days and lower fields of the format pattern. Months and larger are not
	 * used.
	 * </p>
	 * 
	 * @param durationMillis the duration to format
	 * 
	 * @param format the way in which to format the duration, not null
	 * 
	 * @param padWithZeros whether to pad the left hand side of numbers with 0's
	 * 
	 * @return the formatted duration, not null
	 */
	static String format(long durationMillis, String format, boolean padWithZeros) {

		Token[] tokens = lexx(format);

		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		int milliseconds = 0;

		if (Token.containsTokenWithValue(tokens, d)) {
			days = (int) (durationMillis / Dates.MILLIS_PER_DAY);
			durationMillis = durationMillis - (days * Dates.MILLIS_PER_DAY);
		}
		if (Token.containsTokenWithValue(tokens, H)) {
			hours = (int) (durationMillis / Dates.MILLIS_PER_HOUR);
			durationMillis = durationMillis - (hours * Dates.MILLIS_PER_HOUR);
		}
		if (Token.containsTokenWithValue(tokens, m)) {
			minutes = (int) (durationMillis / Dates.MILLIS_PER_MINUTE);
			durationMillis = durationMillis - (minutes * Dates.MILLIS_PER_MINUTE);
		}
		if (Token.containsTokenWithValue(tokens, s)) {
			seconds = (int) (durationMillis / Dates.MILLIS_PER_SECOND);
			durationMillis = durationMillis - (seconds * Dates.MILLIS_PER_SECOND);
		}
		if (Token.containsTokenWithValue(tokens, S)) {
			milliseconds = (int) durationMillis;
		}

		return format(tokens, 0, 0, days, hours, minutes, seconds, milliseconds, padWithZeros);
	}

	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * The internal method to do the formatting.
	 * </p>
	 * 
	 * @param tokens the tokens
	 * @param years the number of years
	 * @param months the number of months
	 * @param days the number of days
	 * @param hours the number of hours
	 * @param minutes the number of minutes
	 * @param seconds the number of seconds
	 * @param milliseconds the number of millis
	 * @param padWithZeros whether to pad
	 * @return the formatted string
	 */
	static String format(Token[] tokens, int years, int months, int days, int hours, int minutes, int seconds, int milliseconds, boolean padWithZeros) {
		StringBuffer buffer = new StringBuffer();
		boolean lastOutputSeconds = false;
		int sz = tokens.length;
		for (int i = 0; i < sz; i++) {
			Token token = tokens[i];
			Object value = token.getValue();
			int count = token.getCount();
			if (value instanceof StringBuffer) {
				buffer.append(value.toString());
			} else {
				if (value == y) {
					buffer.append(padWithZeros ? Strings.padLeft(Integer.toString(years), '0', count) : Integer.toString(years));
					lastOutputSeconds = false;
				} else if (value == M) {
					buffer.append(padWithZeros ? Strings.padLeft(Integer.toString(months), '0', count) : Integer.toString(months));
					lastOutputSeconds = false;
				} else if (value == d) {
					buffer.append(padWithZeros ? Strings.padLeft(Integer.toString(days), '0', count) : Integer.toString(days));
					lastOutputSeconds = false;
				} else if (value == H) {
					buffer.append(padWithZeros ? Strings.padLeft(Integer.toString(hours), '0', count) : Integer.toString(hours));
					lastOutputSeconds = false;
				} else if (value == m) {
					buffer.append(padWithZeros ? Strings.padLeft(Integer.toString(minutes), '0', count) : Integer.toString(minutes));
					lastOutputSeconds = false;
				} else if (value == s) {
					buffer.append(padWithZeros ? Strings.padLeft(Integer.toString(seconds), '0', count) : Integer.toString(seconds));
					lastOutputSeconds = true;
				} else if (value == S) {
					if (lastOutputSeconds) {
						milliseconds += 1000;
						String str = padWithZeros ? Strings.padLeft(Integer.toString(milliseconds), '0', count) : Integer.toString(milliseconds);
						buffer.append(str.substring(1));
					} else {
						buffer.append(padWithZeros ? Strings.padLeft(Integer.toString(milliseconds), '0', count) : Integer.toString(milliseconds));
					}
					lastOutputSeconds = false;
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * Parses a classic date format string into Tokens
	 * 
	 * @param format the format to parse, not null
	 * 
	 * @return array of Token[]
	 */
	static Token[] lexx(String format) {
		char[] array = format.toCharArray();
		ArrayList<Token> list = new ArrayList<Token>(array.length);

		boolean inLiteral = false;
		StringBuffer buffer = null;
		Token previous = null;
		int sz = array.length;
		for (int i = 0; i < sz; i++) {
			char ch = array[i];
			if (inLiteral && ch != '\'') {
				buffer.append(ch); // buffer can't be null if inLiteral is true
				continue;
			}
			Object value = null;
			switch (ch) {
				// TODO: Need to handle escaping of '
				case '\'':
					if (inLiteral) {
						buffer = null;
						inLiteral = false;
					} else {
						buffer = new StringBuffer();
						list.add(new Token(buffer));
						inLiteral = true;
					}
					break;
				case 'y':
					value = y;
					break;
				case 'M':
					value = M;
					break;
				case 'd':
					value = d;
					break;
				case 'H':
					value = H;
					break;
				case 'm':
					value = m;
					break;
				case 's':
					value = s;
					break;
				case 'S':
					value = S;
					break;
				default:
					if (buffer == null) {
						buffer = new StringBuffer();
						list.add(new Token(buffer));
					}
					buffer.append(ch);
			}

			if (value != null) {
				if (previous != null && previous.getValue() == value) {
					previous.increment();
				} else {
					Token token = new Token(value);
					list.add(token);
					previous = token;
				}
				buffer = null;
			}
		}
		return list.toArray(new Token[list.size()]);
	}

	//-----------------------------------------------------------------------
	/**
	 * Element that is parsed from the format pattern.
	 */
	static class Token {

		/**
		 * Helper method to determine if a set of tokens contain a value
		 * 
		 * @param tokens set to look in
		 * @param value to look for
		 * @return boolean <code>true</code> if contained
		 */
		static boolean containsTokenWithValue(Token[] tokens, Object value) {
			int sz = tokens.length;
			for (int i = 0; i < sz; i++) {
				if (tokens[i].getValue() == value) {
					return true;
				}
			}
			return false;
		}

		private final Object	value;
		private int		     count;

		/**
		 * Wraps a token around a value. A value would be something like a 'Y'.
		 * 
		 * @param value to wrap
		 */
		Token(Object value) {
			this.value = value;
			this.count = 1;
		}

		/**
		 * Wraps a token around a repeated number of a value, for example it would store 'yyyy' as a value for y and a
		 * count of 4.
		 * 
		 * @param value to wrap
		 * @param count to wrap
		 */
		Token(Object value, int count) {
			this.value = value;
			this.count = count;
		}

		/**
		 * Adds another one of the value
		 */
		void increment() {
			count++;
		}

		/**
		 * Gets the current number of values represented
		 * 
		 * @return int number of values represented
		 */
		int getCount() {
			return count;
		}

		/**
		 * Gets the particular value this token represents.
		 * 
		 * @return Object value
		 */
		Object getValue() {
			return value;
		}

		/**
		 * Supports equality of this Token to another Token.
		 * 
		 * @param obj2 Object to consider equality of
		 * @return boolean <code>true</code> if equal
		 */
		@Override
		public boolean equals(Object obj2) {
			if (obj2 instanceof Token) {
				Token tok2 = (Token) obj2;
				if (this.value.getClass() != tok2.value.getClass()) {
					return false;
				}
				if (this.count != tok2.count) {
					return false;
				}
				if (this.value instanceof StringBuffer) {
					return this.value.toString().equals(tok2.value.toString());
				} else if (this.value instanceof Number) {
					return this.value.equals(tok2.value);
				} else {
					return this.value == tok2.value;
				}
			}
			return false;
		}

		/**
		 * Returns a hash code for the token equal to the hash code for the token's value. Thus 'TT' and 'TTTT' will
		 * have the same hash code.
		 * 
		 * @return The hash code for the token
		 */
		@Override
		public int hashCode() {
			return this.value.hashCode();
		}

		/**
		 * Represents this token as a String.
		 * 
		 * @return String representation of the token
		 */
		@Override
		public String toString() {
			return Strings.repeat(this.value.toString(), this.count);
		}
	}
}
