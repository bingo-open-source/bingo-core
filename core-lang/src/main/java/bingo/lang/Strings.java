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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * <code>null</code> safe {@link String} utility
 * 
 * @author fenghm (fenghm@bingosoft.net)
 * 
 * @since 0.1
 */
//From apache commons-lang3, under Apache License 2.0
public class Strings {

	public static final String	EMPTY	        = "";
	public static final String	COMMA	        = ",";

	/**
	 * <p>
	 * The maximum size to which the padding constant(s) can expand.
	 * </p>
	 */
	private static final int	PADDING_LIMIT	= 8192;

	private static final int	INDEX_NOT_FOUND	= -1;

	protected Strings() {

	}

	/**
	 * <p>
	 * Checks if a String is null, Returns empty("") if the String is null.
	 * </p>
	 * 
	 * @param string the String to check , may be null
	 * 
	 * @return the input if not null, empty("") if null input.
	 */
	public static String safe(String string) {
		return null == string ? EMPTY : string;
	}

	/**
	 * Gets a CharSequence length or {@code 0} if the CharSequence is {@code null}.
	 * 
	 * @param string a CharSequence or {@code null}
	 * 
	 * @return CharSequence length or {@code 0} if the CharSequence is {@code null}.
	 */
	public static int length(CharSequence string) {
		return string == null ? 0 : string.length();
	}

	// Fomatting
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Formats a template string, and inserts dynamic values in placeholders.
	 * </p>
	 * 
	 * <pre>
	 * 
	 * Strings.format("Hello {0}","world") -> "Hello world"
	 * 
	 * </pre>
	 * 
	 * @param template
	 * @param args
	 * 
	 * @return formated string , empty("") if null input.
	 */
	public static String format(String template, Object... args) {
		if (isEmpty(template)) {
			return EMPTY;
		}

		char[] templateChars = template.toCharArray();

		int templateLength = templateChars.length;
		int length = 0;
		int tokenCount = args.length;
		for (int i = 0; i < tokenCount; i++) {
			Object sourceString = args[i];
			if (sourceString != null) {
				length += sourceString.toString().length();
			}
		}

		// The following buffer size is just an initial estimate. It is legal for
		// any given pattern, such as {0}, to occur more than once, in which case
		// the buffer size will expand automatically if need be.
		StringBuilder buffer = new StringBuilder(length + templateLength);

		int lastStart = 0;
		for (int i = 0; i < templateLength; i++) {
			char ch = templateChars[i];
			if (ch == '{') {
				// Only check for single digit patterns that have an associated token.
				if (i + 2 < templateLength && templateChars[i + 2] == '}') {
					int tokenIndex = templateChars[i + 1] - '0';
					if (tokenIndex >= 0 && tokenIndex < tokenCount) {
						buffer.append(templateChars, lastStart, i - lastStart);
						Object sourceString = args[tokenIndex];
						if (sourceString != null)
							buffer.append(sourceString.toString());

						i += 2;
						lastStart = i + 1;
					}
				}
			}
			// ELSE: Do nothing. The character will be added in later.
		}

		buffer.append(templateChars, lastStart, templateLength - lastStart);

		return new String(buffer);
	}

	// Empty checks
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if a CharSequence is empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isEmpty(null)      = true
	 * Strings.isEmpty("")        = true
	 * Strings.isEmpty(" ")       = false
	 * Strings.isEmpty("bob")     = false
	 * Strings.isEmpty("  bob  ") = false
	 * </pre>
	 * 
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer trims the CharSequence. That functionality is
	 * available in isBlank().
	 * </p>
	 * 
	 * @param string the CharSequence to check, may be null
	 * 
	 * @return {@code true} if the CharSequence is empty or null
	 */
	public static boolean isEmpty(CharSequence string) {
		return string == null || string.length() == 0;
	}

	/**
	 * TODO : document me
	 */
	public static boolean isEmpty(Object string) {
		if (null == string) {
			return true;
		} else if (string instanceof CharSequence) {
			return isEmpty((CharSequence) string);
		}
		return false;
	}

	/**
	 * <p>
	 * Checks if a CharSequence is not empty ("") and not null.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isNotEmpty(null)      = false
	 * Strings.isNotEmpty("")        = false
	 * Strings.isNotEmpty(" ")       = true
	 * Strings.isNotEmpty("bob")     = true
	 * Strings.isNotEmpty("  bob  ") = true
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * 
	 * @return {@code true} if the CharSequence is not empty and not null
	 */
	public static boolean isNotEmpty(CharSequence string) {
		return null != string && string.length() > 0;
	}

	/**
	 * TODO : document me
	 */
	public static boolean isNotEmpty(Object string) {
		if (null == string) {
			return false;
		} else if (string instanceof CharSequence) {
			return isNotEmpty((CharSequence) string);
		}
		return false;
	}

	/**
	 * <p>
	 * Checks if a CharSequence is whitespace, empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isBlank(null)      = true
	 * Strings.isBlank("")        = true
	 * Strings.isBlank(" ")       = true
	 * Strings.isBlank("bob")     = false
	 * Strings.isBlank("  bob  ") = false
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * 
	 * @return {@code true} if the CharSequence is null, empty or whitespace
	 */
	public static boolean isBlank(CharSequence string) {
		int strLen;
		if (string == null || (strLen = string.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(string.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * TODO : document me
	 */
	public static boolean isBlank(Object string) {
		if (null == string) {
			return true;
		} else if (string instanceof CharSequence) {
			return isBlank((CharSequence) string);
		}
		return false;
	}

	/**
	 * <p>
	 * Checks if a CharSequence is not empty (""), not null and not whitespace only.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isNotBlank(null)      = false
	 * Strings.isNotBlank("")        = false
	 * Strings.isNotBlank(" ")       = false
	 * Strings.isNotBlank("bob")     = true
	 * Strings.isNotBlank("  bob  ") = true
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * 
	 * @return {@code true} if the CharSequence is not empty and not null and not whitespace
	 */
	public static boolean isNotBlank(CharSequence string) {
		return !isBlank(string);
	}

	/**
	 * TODO : document me
	 */
	public static boolean isNotBlank(Object string) {
		if (null == string) {
			return false;
		} else if (string instanceof CharSequence) {
			return isNotBlank((CharSequence) string);
		}
		return false;
	}

	// Trim
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String returning an empty String ("") if the
	 * String is empty ("") after the trim or if it is {@code null}.
	 * 
	 * <pre>
	 * Strings.trim(null)          = ""
	 * Strings.trim("")            = ""
	 * Strings.trim("     ")       = ""
	 * Strings.trim("abc")         = "abc"
	 * Strings.trim("    abc    ") = "abc"
	 * </pre>
	 * 
	 * @param string the String to be trimmed, may be null
	 * @return the trimmed String, or an empty String if {@code null} input
	 */
	public static String trim(String string) {
		return string == null ? EMPTY : string.trim();
	}

	/**
	 * <p>
	 * Removes any of a set of characters from the start and end of a String. This is similar to {@link String#trim()}
	 * but allows the characters to be trimmed to be controlled.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} input String returns "". An empty string ("") input returns the empty string.
	 * </p>
	 * 
	 * <p>
	 * If the trimChars String is {@code null}, whitespace is trimmed as defined by
	 * {@link Character#isWhitespace(char)}. Alternatively use {@link #trim(String)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.trim(null, *)          = ""
	 * Strings.trim("", *)            = ""
	 * Strings.trim("abc", null)      = "abc"
	 * Strings.trim("  abc", null)    = "abc"
	 * Strings.trim("abc  ", null)    = "abc"
	 * Strings.trim(" abc ", null)    = "abc"
	 * Strings.trim("  abcyx", "xyz") = "  abc"
	 * </pre>
	 * 
	 * @param str the String to remove characters from, may be null
	 * @param trimChars the characters to remove, null treated as whitespace
	 * @return the trimmed String, "" if null String input
	 */
	public static String trim(String str, char... trimChars) {
		if(null == trimChars){
			return trim(str);
		}
		
		if (isEmpty(str)) {
			return EMPTY;
		}
		
		str = trimStart(str, trimChars);
		return trimEnd(str, trimChars);
	}

	/**
	 * <p>
	 * Trims any of a set of characters from the start of a String.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} input String returns "". An empty string ("") input returns the empty string.
	 * </p>
	 * 
	 * <p>
	 * If the trimChars String is {@code null}, whitespace is trimmed as defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.trimStart(null, *)          = ""
	 * Strings.trimStart("", *)            = ""
	 * Strings.trimStart("abc", "")        = "abc"
	 * Strings.trimStart("abc", null)      = "abc"
	 * Strings.trimStart("  abc", null)    = "abc"
	 * Strings.trimStart("abc  ", null)    = "abc  "
	 * Strings.trimStart(" abc ", null)    = "abc "
	 * Strings.trimStart("yxabc  ", "xyz") = "abc  "
	 * </pre>
	 * 
	 * @param str the String to remove characters from, may be null
	 * @param trimChars the characters to remove, null treated as whitespace
	 * @return the trimmed String, "" if null String input
	 */
	public static String trimStart(String str, char... trimChars) {
		int strLen;
		
		if (str == null || (strLen = str.length()) == 0) {
			return EMPTY;
		}
		
		int start = 0;
		if (trimChars == null) {
			while (start != strLen && Character.isWhitespace(str.charAt(start))) {
				start++;
			}
		} else if (trimChars.length == 0) {
			return str;
		} else {
			while (start != strLen && Arrays.indexOf(trimChars, str.charAt(start)) != INDEX_NOT_FOUND) {
				start++;
			}
		}
		return str.substring(start);
	}

	/**
	 * <p>
	 * Removes any of a set of characters from the end of a String.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} input String returns "". An empty string ("") input returns the empty string.
	 * </p>
	 * 
	 * <p>
	 * If the trimChars String is {@code null}, whitespace is trimmed as defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.trimEnd(null, *)          = ""
	 * Strings.trimEnd("", *)            = ""
	 * Strings.trimEnd("abc", "")        = "abc"
	 * Strings.trimEnd("abc", null)      = "abc"
	 * Strings.trimEnd("  abc", null)    = "  abc"
	 * Strings.trimEnd("abc  ", null)    = "abc"
	 * Strings.trimEnd(" abc ", null)    = " abc"
	 * Strings.trimEnd("  abcyx", "xyz") = "  abc"
	 * Strings.trimEnd("120.00", ".0")   = "12"
	 * </pre>
	 * 
	 * @param str the String to remove characters from, may be null
	 * @param trimChars the set of characters to remove, null treated as whitespace
	 * @return the trimmed String, "" if null String input
	 */
	public static String trimEnd(String str, char... trimChars) {
		int end;
		if (str == null || (end = str.length()) == 0) {
			return EMPTY;
		}

		if (trimChars == null) {
			while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
				end--;
			}
		} else if (trimChars.length == 0) {
			return str;
		} else {
			while (end != 0 && Arrays.indexOf(trimChars, str.charAt(end - 1)) != INDEX_NOT_FOUND) {
				end--;
			}
		}
		return str.substring(0, end);
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String, handling {@code null} by returning
	 * {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.trimOrNull(null)          = ""
	 * Strings.trimOrNull("")            = ""
	 * Strings.trimOrNull("     ")       = ""
	 * Strings.trimOrNull("abc")         = "abc"
	 * Strings.trimOrNull("    abc    ") = "abc"
	 * </pre>
	 * 
	 * @param string the String to be trimmed, may be null
	 * 
	 * @return the trimmed string, {@code null} if null String input
	 */
	public static String trimOrNull(String string) {
		return string == null ? null : string.trim();
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String returning {@code null} if the String is
	 * empty ("") after the trim or if it is {@code null}.
	 * 
	 * <p>
	 * The String is trimmed using {@link String#trim()}. Trim removes start and end characters &lt;= 32.
	 * </p>
	 * 
	 * <pre>
	 * Strings.trimToNull(null)          = null
	 * Strings.trimToNull("")            = null
	 * Strings.trimToNull("     ")       = null
	 * Strings.trimToNull("abc")         = "abc"
	 * Strings.trimToNull("    abc    ") = "abc"
	 * </pre>
	 * 
	 * @param string the String to be trimmed, may be null
	 * 
	 * @return the trimmed String, {@code null} if only whitespace characters, empty or null String input
	 */
	public static String trimToNull(String string) {
		String ts = trimOrNull(string);
		return isEmpty(ts) ? null : ts;
	}

	// Equals
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Compares two CharSequences, returning {@code true} if they are equal.
	 * </p>
	 * 
	 * <p>
	 * {@code null}s are handled without exceptions. Two {@code null} references are considered to be equal. The
	 * comparison is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * Strings.equals(null, null)   = true
	 * Strings.equals(null, "abc")  = false
	 * Strings.equals("abc", null)  = false
	 * Strings.equals("abc", "abc") = true
	 * Strings.equals("abc", "ABC") = false
	 * </pre>
	 * 
	 * @see java.lang.String#equals(Object)
	 * 
	 * @param string1 the first CharSequence, may be null
	 * 
	 * @param string2 the second CharSequence, may be null
	 * 
	 * @return {@code true} if the CharSequences are equal, case sensitive, or both {@code null}
	 */
	public static boolean equals(String string1, String string2) {
		return string1 == null ? string2 == null : string1.equals(string2);
	}

	/**
	 * <p>
	 * Compares two CharSequences, returning {@code true} if they are equal.
	 * </p>
	 * 
	 * <p>
	 * {@code null}s are handled without exceptions. Two {@code null} references are considered to be equal. The
	 * comparison is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * Strings.equals(null, null)   = true
	 * Strings.equals(null, "abc")  = false
	 * Strings.equals("abc", null)  = false
	 * Strings.equals("abc", "abc") = true
	 * Strings.equals("abc", "ABC") = false
	 * </pre>
	 * 
	 * @see java.lang.String#equals(Object)
	 * 
	 * @param string1 the first CharSequence, may be null
	 * 
	 * @param string2 the second CharSequence, may be null
	 * 
	 * @param ignoreCase is the compare ignore case
	 * 
	 * @return {@code true} if the CharSequences are equal, or both {@code null}
	 */
	public static boolean equals(String string1, String string2, boolean ignoreCase) {
		return string1 == null ? string2 == null : (ignoreCase ? string1.equalsIgnoreCase(string2) : string1.equals(string2));
	}

	/**
	 * <p>
	 * Compares two String, returning {@code true} if they are equal ignoring the case.
	 * </p>
	 * 
	 * <p>
	 * {@code null}s are handled without exceptions. Two {@code null} references are considered equal. Comparison is
	 * case insensitive.
	 * </p>
	 * 
	 * <pre>
	 * Strings.equalsIgnoreCase(null, null)   = true
	 * Strings.equalsIgnoreCase(null, "abc")  = false
	 * Strings.equalsIgnoreCase("abc", null)  = false
	 * Strings.equalsIgnoreCase("abc", "abc") = true
	 * Strings.equalsIgnoreCase("abc", "ABC") = true
	 * </pre>
	 * 
	 * @param string1 the first String, may be null
	 * @param string2 the second String, may be null
	 * @return {@code true} if the String are equal, case insensitive, or both {@code null}
	 */
	public static boolean equalsIgnoreCase(String string1, String string2) {
		if (string1 == null || string2 == null) {
			return string1 == string2;
		} else {
			return string1.equalsIgnoreCase(string2);
		}
	}

	// Joining
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of elements.
	 * </p>
	 * 
	 * <p>
	 * No separator is added to the joined String. Null objects or empty strings within the array are represented by
	 * empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null)            = ""
	 * Strings.join([])              = ""
	 * Strings.join([null])          = ""
	 * Strings.join(["a", "b", "c"]) = "a,b,c"
	 * Strings.join([null, "", "a"]) = "a"
	 * </pre>
	 * 
	 * @param <T> the specific type of values to join together
	 * 
	 * @param elements the values to join together, may be null
	 * 
	 * @return the joined String, empty string if null array input
	 */
	public static <T> String join(T... elements) {
		return join(elements, COMMA);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of elements.
	 * </p>
	 * 
	 * <p>
	 * No separator is added to the joined String. Null objects or empty strings within the array are represented by
	 * empty strings.
	 * </p>
	 * 
	 * <pre>
	 * See the examples here: {@link #join(Object...)}
	 * </pre>
	 * 
	 * @param <T> the specific type of values to join together
	 * 
	 * @param elements the values to join together, may be null
	 * 
	 * @return the joined String, empty string if null array input
	 */
	public static <T> String join(Iterable<?> elements) {
		return join(elements, COMMA);
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of elements.
	 * </p>
	 * 
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the array are represented by
	 * empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)               = ""
	 * Strings.join([], *)                 = ""
	 * Strings.join([null], *)             = ""
	 * Strings.join(["a", "b", "c"], ';')  = "a;b;c"
	 * Strings.join(["a", "b", "c"], null) = "abc"
	 * Strings.join([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * 
	 * @param separator the separator character to use
	 * 
	 * @return the joined String, empty string if null array input
	 */
	public static String join(Object[] array, char separator) {
		if (array == null) {
			return EMPTY;
		}

		int len = array.length;

		StringBuilder buf = new StringBuilder(len * 16);

		for (int i = 0; i < len; i++) {
			if (i > 0) {
				buf.append(separator);
			}

			if (array[i] != null) {
				buf.append(array[i]);
			}
		}

		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing the provided list of elements.
	 * </p>
	 * 
	 * <p>
	 * No delimiter is added before or after the list. A {@code null} separator is the same as an empty String ("").
	 * Null objects or empty strings within the array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * Strings.join(null, *)                = ""
	 * Strings.join([], *)                  = ""
	 * Strings.join([null], *)              = ""
	 * Strings.join(["a", "b", "c"], "--")  = "a--b--c"
	 * Strings.join(["a", "b", "c"], null)  = "abc"
	 * Strings.join(["a", "b", "c"], "")    = "abc"
	 * Strings.join([null, "", "a"], ',')   = ",,a"
	 * </pre>
	 * 
	 * @param array the array of values to join together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, empty string if null array input
	 */
	public static String join(Object[] array, String separator) {
		if (array == null) {
			return EMPTY;
		}

		int len = array.length;

		StringBuilder buf = new StringBuilder(len * 16);

		for (int i = 0; i < len; i++) {
			if (i > 0 && separator != null) {
				buf.append(separator);
			}

			if (array[i] != null) {
				buf.append(array[i]);
			}
		}

		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided {@code Iterator} into a single String containing the provided elements.
	 * </p>
	 * 
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the iteration are
	 * represented by empty strings.
	 * </p>
	 * 
	 * <p>
	 * See the examples here: {@link #join(Object[],char)}.
	 * </p>
	 * 
	 * @param iterator the {@code Iterator} of values to join together, may be null
	 * 
	 * @param separator the separator character to use
	 * 
	 * @return the joined String, empty string if null iterator input
	 */
	public static String join(Iterator<?> iterator, char separator) {

		// handle null, zero and one elements before building a buffer
		if (iterator == null) {
			return EMPTY;
		}

		if (!iterator.hasNext()) {
			return EMPTY;
		}

		Object first = iterator.next();
		if (!iterator.hasNext()) {
			return Objects.toString(first);
		}

		// two or more elements
		StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
		if (first != null) {
			buf.append(first);
		}

		while (iterator.hasNext()) {
			buf.append(separator);
			Object obj = iterator.next();
			if (obj != null) {
				buf.append(obj);
			}
		}

		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided {@code Iterator} into a single String containing the provided elements.
	 * </p>
	 * 
	 * <p>
	 * No delimiter is added before or after the list. A {@code null} separator is the same as an empty String ("").
	 * </p>
	 * 
	 * <p>
	 * See the examples here: {@link #join(Object[],String)}.
	 * </p>
	 * 
	 * @param iterator the {@code Iterator} of values to join together, may be null
	 * 
	 * @param separator the separator character to use, null treated as ""
	 * 
	 * @return the joined String, empty string if null iterator input
	 */
	public static String join(Iterator<?> iterator, String separator) {

		// handle null, zero and one elements before building a buffer
		if (iterator == null) {
			return EMPTY;
		}
		if (!iterator.hasNext()) {
			return EMPTY;
		}
		Object first = iterator.next();
		if (!iterator.hasNext()) {
			return Objects.toString(first);
		}

		// two or more elements
		StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
		if (first != null) {
			buf.append(first);
		}

		while (iterator.hasNext()) {
			if (separator != null) {
				buf.append(separator);
			}
			Object obj = iterator.next();
			if (obj != null) {
				buf.append(obj);
			}
		}
		return buf.toString();
	}

	/**
	 * <p>
	 * Joins the elements of the provided {@code Iterable} into a single String containing the provided elements.
	 * </p>
	 * 
	 * <p>
	 * No delimiter is added before or after the list. Null objects or empty strings within the iteration are
	 * represented by empty strings.
	 * </p>
	 * 
	 * <p>
	 * See the examples here: {@link #join(Object[],char)}.
	 * </p>
	 * 
	 * @param iterable the {@code Iterable} providing the values to join together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, empty string if null iterator input
	 */
	public static String join(Iterable<?> iterable, char separator) {
		if (iterable == null) {
			return EMPTY;
		}
		return join(iterable.iterator(), separator);
	}

	/**
	 * <p>
	 * Joins the elements of the provided {@code Iterable} into a single String containing the provided elements.
	 * </p>
	 * 
	 * <p>
	 * No delimiter is added before or after the list. A {@code null} separator is the same as an empty String ("").
	 * </p>
	 * 
	 * <p>
	 * See the examples here: {@link #join(Object[],String)}.
	 * </p>
	 * 
	 * @param iterable the {@code Iterable} providing the values to join together, may be null
	 * 
	 * @param separator the separator character to use, null treated as ""
	 * 
	 * @return the joined String, empty string if null iterator input
	 */
	public static String join(Iterable<?> iterable, String separator) {
		if (iterable == null) {
			return EMPTY;
		}
		return join(iterable.iterator(), separator);
	}

	// Splitting
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Splits the provided text into an array, using "," as the separator.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} input String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Stringss.split(null)       = []
	 * Stringss.split("")         = []
	 * Stringss.split("abc,def")  = ["abc", "def"]
	 * Stringss.split("abc def")  = ["abc def"]
	 * Stringss.split(" abc ")    = ["abc"]
	 * </pre>
	 * 
	 * @param string the String to parse, may be null
	 * 
	 * @return an array of parsed Strings, empty array [] if null String input
	 */
	public static String[] split(String string) {
		return splitWorker(string, Chars.COMMA, false, true, false);
	}

	/**
	 * <p>
	 * Splits the provided text into an array, separator specified. This is an alternative to using StringTokenizer.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} input String returns empty array [].
	 * </p>
	 * 
	 * <pre>
	 * Stringss.split(null, *)         = []
	 * Stringss.split("", *)           = []
	 * Stringss.split("a.b.c", '.')    = ["a", "b", "c"]
	 * Stringss.split("a..b.c", '.')   = ["a", "b", "c"]
	 * Stringss.split("a:b:c", '.')    = ["a:b:c"]
	 * Stringss.split("a b c", ' ')    = ["a", "b", "c"]
	 * </pre>
	 * 
	 * @param string the String to parse, may be null
	 * @param separator the character used as the delimiter
	 * @return an array of parsed Strings, empty array [] if null String input
	 */
	public static String[] split(String string, char separator) {
		return splitWorker(string, separator, false, true, false);
	}

	/**
	 * <p>
	 * Splits the provided text into an array, separator specified. This is an alternative to using StringTokenizer.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} input String returns empty array [].
	 * </p>
	 * 
	 * <pre>
	 * See the examples here: {@link #split(String, char)}
	 * </pre>
	 * 
	 * @param string the String to parse, may be null
	 * 
	 * @param separator the character used as the delimiter
	 * 
	 * @param trim if true then trim all the elements string in array
	 * 
	 * @return an array of parsed Strings, empty array [] if null String input
	 */
	public static String[] split(String string, char separator, boolean trim) {
		return splitWorker(string, separator, false, trim, false);
	}

	/**
	 * <p>
	 * Splits the provided text into an array, separators specified. This is an alternative to using StringTokenizer.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} input String returns empty array []. A {@code null} separatorChars splits on comma ",".
	 * </p>
	 * 
	 * <pre>
	 * See the examples here: {@link #split(String, String)}
	 * </pre>
	 * 
	 * @param string the String to parse, may be null
	 * 
	 * @param separator the characters used as the delimiters, {@code null} splits on whitespace
	 * 
	 * @param trim if true then trim all the elements string in array
	 * 
	 * @return an array of parsed Strings, [] if null String input
	 */
	public static String[] split(String string, String separator, boolean trim) {
		return splitWorker(string, separator, -1, false, trim, false);
	}

	/**
	 * <p>
	 * Splits the provided text into an array, separators specified. This is an alternative to using StringTokenizer.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} input String returns empty array []. A {@code null} separatorChars splits on comma ",".
	 * </p>
	 * 
	 * <pre>
	 * Stringss.split(null, *)         = []
	 * Stringss.split("", *)           = []
	 * Stringss.split("abc,def", null) = ["abc", "def"]
	 * Stringss.split("abc def", " ")  = ["abc", "def"]
	 * Stringss.split("abc  def", " ") = ["abc", "def"]
	 * Stringss.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
	 * </pre>
	 * 
	 * @param string the String to parse, may be null
	 * 
	 * @param separator the characters used as the delimiters, {@code null} splits on whitespace
	 * 
	 * @return an array of parsed Strings, [] if null String input
	 */
	public static String[] split(String string, String separator) {
		return splitWorker(string, separator, -1, false, true, false);
	}

	// Substring
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 * 
	 * <p>
	 * A negative start position can be used to start {@code n} characters from the end of the String.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} String will return "". An empty ("") String will return "".
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.substring(null, *)   = ""
	 * StringUtils.substring("", *)     = ""
	 * StringUtils.substring("abc", 0)  = "abc"
	 * StringUtils.substring("abc", 2)  = "c"
	 * StringUtils.substring("abc", 4)  = ""
	 * StringUtils.substring("abc", -2) = "bc"
	 * StringUtils.substring("abc", -4) = "abc"
	 * </pre>
	 * 
	 * @param string the String to get the substring from, may be null
	 * @param start the position to start from, negative means count back from the end of the String by this many
	 *            characters
	 * @return substring from start position, "" if null String input
	 */
	public static String substring(String string, int start) {
		if (string == null) {
			return EMPTY;
		}

		// handle negatives, which means last n characters
		if (start < 0) {
			start = string.length() + start; // remember start is negative
		}

		if (start < 0) {
			start = 0;
		}
		if (start > string.length()) {
			return EMPTY;
		}

		return string.substring(start);
	}

	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 * 
	 * <p>
	 * A negative start position can be used to start/end {@code n} characters from the end of the String.
	 * </p>
	 * 
	 * <p>
	 * The returned substring starts with the character in the {@code start} position and ends before the {@code end}
	 * position. All position counting is zero-based -- i.e., to start at the beginning of the string use {@code start =
	 * 0}. Negative start and end positions can be used to specify offsets relative to the end of the String.
	 * </p>
	 * 
	 * <p>
	 * If {@code start} is not strictly to the left of {@code end}, "" is returned.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.substring(null, *, *)    = ""
	 * StringUtils.substring("", * ,  *)    = "";
	 * StringUtils.substring("abc", 0, 2)   = "ab"
	 * StringUtils.substring("abc", 2, 0)   = ""
	 * StringUtils.substring("abc", 2, 4)   = "c"
	 * StringUtils.substring("abc", 4, 6)   = ""
	 * StringUtils.substring("abc", 2, 2)   = ""
	 * StringUtils.substring("abc", -2, -1) = "b"
	 * StringUtils.substring("abc", -4, 2)  = "ab"
	 * </pre>
	 * 
	 * @param string the String to get the substring from, may be null
	 * @param start the position to start from, negative means count back from the end of the String by this many
	 *            characters
	 * @param end the position to end at (exclusive), negative means count back from the end of the String by this many
	 *            characters
	 * @return substring from start position to end position, "" if null String input
	 */
	public static String substring(String string, int start, int end) {
		if (string == null) {
			return EMPTY;
		}

		// handle negatives
		if (end < 0) {
			end = string.length() + end; // remember end is negative
		}
		if (start < 0) {
			start = string.length() + start; // remember start is negative
		}

		// check length next
		if (end > string.length()) {
			end = string.length();
		}

		// if start is greater than end, return ""
		if (start > end) {
			return EMPTY;
		}

		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}

		return string.substring(start, end);
	}

	// Left/Right/Mid
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the leftmost {@code len} characters of a String.
	 * </p>
	 * 
	 * <p>
	 * If {@code len} characters are not available, or the String is {@code null}, the String will be returned without
	 * an exception. An empty String is returned if len is negative.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.left(null, *)    = ""
	 * StringUtils.left(*, -ve)     = ""
	 * StringUtils.left("", *)      = ""
	 * StringUtils.left("abc", 0)   = ""
	 * StringUtils.left("abc", 2)   = "ab"
	 * StringUtils.left("abc", 4)   = "abc"
	 * </pre>
	 * 
	 * @param string the String to get the leftmost characters from, may be null
	 * @param len the length of the required String
	 * @return the leftmost characters, "" if null String input
	 */
	public static String left(String string, int len) {
		if (string == null) {
			return EMPTY;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (string.length() <= len) {
			return string;
		}
		return string.substring(0, len);
	}

	/**
	 * <p>
	 * Gets the rightmost {@code len} characters of a String.
	 * </p>
	 * 
	 * <p>
	 * If {@code len} characters are not available, or the String is {@code null}, the String will be returned without
	 * an an exception. An empty String is returned if len is negative.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.right(null, *)    = ""
	 * StringUtils.right(*, -ve)     = ""
	 * StringUtils.right("", *)      = ""
	 * StringUtils.right("abc", 0)   = ""
	 * StringUtils.right("abc", 2)   = "bc"
	 * StringUtils.right("abc", 4)   = "abc"
	 * </pre>
	 * 
	 * @param string the String to get the rightmost characters from, may be null
	 * @param len the length of the required String
	 * @return the rightmost characters, "" if null String input
	 */
	public static String right(String string, int len) {
		if (string == null) {
			return EMPTY;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (string.length() <= len) {
			return string;
		}
		return string.substring(string.length() - len);
	}

	// Concat
	//-----------------------------------------------------------------------

	public static String concat(String... strings) {
		if (null == strings || strings.length == 0) {
			return EMPTY;
		}

		StringBuilder buf = new StringBuilder(256);

		for (String str : strings) {
			if (null != str) {
				buf.append(trim(str));
			}
		}

		return buf.toString();
	}

	// Replacing
	//-----------------------------------------------------------------------    

	/**
	 * <p>
	 * Replaces a String with another String inside a larger String, once.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} reference passed to this method is a no-op.
	 * </p>
	 * 
	 * <pre>
	 * Strings.replaceOnce(null, *, *)        = null
	 * Strings.replaceOnce("", *, *)          = ""
	 * Strings.replaceOnce("any", null, *)    = "any"
	 * Strings.replaceOnce("any", *, null)    = "any"
	 * Strings.replaceOnce("any", "", *)      = "any"
	 * Strings.replaceOnce("aba", "a", null)  = "aba"
	 * Strings.replaceOnce("aba", "a", "")    = "ba"
	 * Strings.replaceOnce("aba", "a", "z")   = "zba"
	 * </pre>
	 * 
	 * @param text text to search and replace in, may be null
	 * @param oldString the String to search for, may be null
	 * @param newString the String to replace with, may be null
	 * @return the text with any replacements processed, empty("") if null String input
	 */
	public static String replaceOnce(String text, String oldString, String newString) {
		return replace(text, oldString, newString, 1);
	}

	/**
	 * <p>
	 * Replaces all occurrences of a String within another String.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} reference passed to this method is a no-op.
	 * </p>
	 * 
	 * <pre>
	 * Strings.replace(null, *, *)        = null
	 * Strings.replace("", *, *)          = ""
	 * Strings.replace("any", null, *)    = "any"
	 * Strings.replace("any", *, null)    = "any"
	 * Strings.replace("any", "", *)      = "any"
	 * Strings.replace("aba", "a", null)  = "aba"
	 * Strings.replace("aba", "a", "")    = "b"
	 * Strings.replace("aba", "a", "z")   = "zbz"
	 * </pre>
	 * 
	 * @param text text to search and replace in, may be null
	 * @param oldString the String to search for, may be null
	 * @param newString the String to replace it with, may be null
	 * @return the text with any replacements processed, empty("") if null String input
	 */
	public static String replace(String text, String oldString, String newString) {
		return replace(text, oldString, newString, -1);
	}

	/**
	 * <p>
	 * Replaces all occurrences of a character in a String with another. This is a null-safe version of
	 * {@link String#replace(char, char)}.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} string input returns "".
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.replaceChars(null, *, *)        = ""
	 * StringUtils.replaceChars("", *, *)          = ""
	 * StringUtils.replaceChars("abcba", 'b', 'y') = "aycya"
	 * StringUtils.replaceChars("abcba", 'z', 'y') = "abcba"
	 * </pre>
	 * 
	 * @param string String to replace characters in, may be null
	 * @param oldChar the character to search for, may be null
	 * @param newChar the character to replace, may be null
	 * @return modified String, "" if null string input
	 */
	public static String replace(String string, char oldChar, char newChar) {
		if (string == null) {
			return EMPTY;
		}
		return string.replace(oldChar, newChar);
	}

	// Case conversion
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts a String to upper case as per {@link String#toUpperCase()}.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} input String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.upperCase(null)  = null
	 * Strings.upperCase("")    = ""
	 * Strings.upperCase("aBc") = "ABC"
	 * </pre>
	 * 
	 * <p>
	 * <strong>Note:</strong> As described in the documentation for {@link String#toUpperCase()}, the result of this
	 * method is affected by the current locale. For platform-independent case transformations, the method
	 * {@link #lowerCase(String, Locale)} should be used with a specific locale (e.g. {@link Locale#ENGLISH}).
	 * </p>
	 * 
	 * @param string the String to upper case, may be null
	 * @return the upper cased String, empty("") if null String input
	 */
	public static String upperCase(String string) {
		if (string == null) {
			return EMPTY;
		}
		return string.toUpperCase();
	}

	/**
	 * <p>
	 * Converts a String to lower case as per {@link String#toLowerCase()}.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} input String returns {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.lowerCase(null)  = null
	 * Strings.lowerCase("")    = ""
	 * Strings.lowerCase("aBc") = "abc"
	 * </pre>
	 * 
	 * <p>
	 * <strong>Note:</strong> As described in the documentation for {@link String#toLowerCase()}, the result of this
	 * method is affected by the current locale. For platform-independent case transformations, the method
	 * {@link #lowerCase(String, Locale)} should be used with a specific locale (e.g. {@link Locale#ENGLISH}).
	 * </p>
	 * 
	 * @param string the String to lower case, may be null
	 * @return the lower cased String, empty("") if null String input
	 */
	public static String lowerCase(String string) {
		if (string == null) {
			return EMPTY;
		}
		return string.toLowerCase();
	}

	// IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the first index within a CharSequence, handling {@code null}. This method uses
	 * {@link String#indexOf(int, int)} if possible.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} or empty ("") CharSequence will return {@code INDEX_NOT_FOUND (-1)}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.indexOf(null, *)         = -1
	 * StringUtils.indexOf("", *)           = -1
	 * StringUtils.indexOf("aabaabaa", 'a') = 0
	 * StringUtils.indexOf("aabaabaa", 'b') = 2
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param indexOf the character to find
	 * @return the first index of the search character, -1 if no match or {@code null} string input
	 */
	public static int indexOf(CharSequence string, char indexOf) {
		if (isEmpty(string)) {
			return INDEX_NOT_FOUND;
		}
		return indexOfWorker(string, indexOf, 0);
	}

	/**
	 * <p>
	 * Finds the first index within a CharSequence from a start position, handling {@code null}. This method uses
	 * {@link String#indexOf(int, int)} if possible.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} or empty ("") CharSequence will return {@code (INDEX_NOT_FOUND) -1}. A negative start position is
	 * treated as zero. A start position greater than the string length returns {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.indexOf(null, *, *)          = -1
	 * StringUtils.indexOf("", *, *)            = -1
	 * StringUtils.indexOf("aabaabaa", 'b', 0)  = 2
	 * StringUtils.indexOf("aabaabaa", 'b', 3)  = 5
	 * StringUtils.indexOf("aabaabaa", 'b', 9)  = -1
	 * StringUtils.indexOf("aabaabaa", 'b', -1) = 2
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param indexOf the character to find
	 * @param start the start position, negative treated as zero
	 * @return the first index of the search character, -1 if no match or {@code null} string input
	 */
	public static int indexOf(CharSequence string, char indexOf, int start) {
		if (isEmpty(string)) {
			return INDEX_NOT_FOUND;
		}
		return indexOf(string, indexOf, start);
	}

	/**
	 * <p>
	 * Finds the first index within a CharSequence, handling {@code null}. This method uses
	 * {@link String#indexOf(String, int)} if possible.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.indexOf(null, *)          = -1
	 * StringUtils.indexOf(*, null)          = -1
	 * StringUtils.indexOf("", "")           = 0
	 * StringUtils.indexOf("", *)            = -1 (except when * = "")
	 * StringUtils.indexOf("aabaabaa", "a")  = 0
	 * StringUtils.indexOf("aabaabaa", "b")  = 2
	 * StringUtils.indexOf("aabaabaa", "ab") = 1
	 * StringUtils.indexOf("aabaabaa", "")   = 0
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param indexOf the CharSequence to find, may be null
	 * @return the first index of the search CharSequence, -1 if no match or {@code null} string input
	 */
	public static int indexOf(CharSequence string, CharSequence indexOf) {
		if (string == null || indexOf == null) {
			return INDEX_NOT_FOUND;
		}
		return indexOfWorker(string, indexOf, 0);
	}

	/**
	 * <p>
	 * Finds the first index within a CharSequence, handling {@code null}. This method uses
	 * {@link String#indexOf(String, int)} if possible.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A negative start position is treated as zero. An empty ("")
	 * search CharSequence always matches. A start position greater than the string length only matches an empty search
	 * CharSequence.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.indexOf(null, *, *)          = -1
	 * StringUtils.indexOf(*, null, *)          = -1
	 * StringUtils.indexOf("", "", 0)           = 0
	 * StringUtils.indexOf("", *, 0)            = -1 (except when * = "")
	 * StringUtils.indexOf("aabaabaa", "a", 0)  = 0
	 * StringUtils.indexOf("aabaabaa", "b", 0)  = 2
	 * StringUtils.indexOf("aabaabaa", "ab", 0) = 1
	 * StringUtils.indexOf("aabaabaa", "b", 3)  = 5
	 * StringUtils.indexOf("aabaabaa", "b", 9)  = -1
	 * StringUtils.indexOf("aabaabaa", "b", -1) = 2
	 * StringUtils.indexOf("aabaabaa", "", 2)   = 2
	 * StringUtils.indexOf("abc", "", 9)        = 3
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param indexOf the CharSequence to find, may be null
	 * @param start the start position, negative treated as zero
	 * @return the first index of the search CharSequence, -1 if no match or {@code null} string input
	 */
	public static int indexOf(CharSequence string, CharSequence indexOf, int start) {
		if (string == null || indexOf == null) {
			return INDEX_NOT_FOUND;
		}
		return indexOfWorker(string, indexOf, start);
	}

	/**
	 * <p>
	 * Case in-sensitive find of the first index within a CharSequence.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A negative start position is treated as zero. An empty ("")
	 * search CharSequence always matches. A start position greater than the string length only matches an empty search
	 * CharSequence.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.indexOfIgnoreCase(null, *)          = -1
	 * StringUtils.indexOfIgnoreCase(*, null)          = -1
	 * StringUtils.indexOfIgnoreCase("", "")           = 0
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "a")  = 0
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "b")  = 2
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "ab") = 1
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param indexOf the CharSequence to find, may be null
	 * @return the first index of the search CharSequence, -1 if no match or {@code null} string input
	 */
	public static int indexOfIgnoreCase(CharSequence string, CharSequence indexOf) {
		return indexOfIgnoreCase(string, indexOf, 0);
	}

	/**
	 * <p>
	 * Case in-sensitive find of the first index within a CharSequence from the specified position.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A negative start position is treated as zero. An empty ("")
	 * search CharSequence always matches. A start position greater than the string length only matches an empty search
	 * CharSequence.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.indexOfIgnoreCase(null, *, *)          = -1
	 * StringUtils.indexOfIgnoreCase(*, null, *)          = -1
	 * StringUtils.indexOfIgnoreCase("", "", 0)           = 0
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
	 * StringUtils.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
	 * StringUtils.indexOfIgnoreCase("abc", "", 9)        = 3
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param indexOf the CharSequence to find, may be null
	 * @param start the start position, negative treated as zero
	 * @return the first index of the search CharSequence, -1 if no match or {@code null} string input
	 */
	public static int indexOfIgnoreCase(CharSequence string, CharSequence indexOf, int start) {
		if (string == null || indexOf == null) {
			return INDEX_NOT_FOUND;
		}
		if (start < 0) {
			start = 0;
		}
		int endLimit = string.length() - indexOf.length() + 1;
		if (start > endLimit) {
			return INDEX_NOT_FOUND;
		}
		if (indexOf.length() == 0) {
			return start;
		}
		for (int i = start; i < endLimit; i++) {
			if (regionMatches(string, true, i, indexOf, 0, indexOf.length())) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	// IndexOfAny chars
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Search a CharSequence to find the first index of any character in the given set of characters.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} String will return {@code -1}. A {@code null} or zero length search array will return {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.indexOfAny(null, *)                = -1
	 * StringUtils.indexOfAny("", *)                  = -1
	 * StringUtils.indexOfAny(*, null)                = -1
	 * StringUtils.indexOfAny(*, [])                  = -1
	 * StringUtils.indexOfAny("zzabyycdxx",['z','a']) = 0
	 * StringUtils.indexOfAny("zzabyycdxx",['b','y']) = 3
	 * StringUtils.indexOfAny("aba", ['z'])           = -1
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param anyChars the chars to search for, may be null
	 * @return the index of any of the chars, -1 if no match or null input
	 */
	public static int indexOfAny(CharSequence string, char... anyChars) {
		if (isEmpty(string) || Arrays.isEmpty(anyChars)) {
			return INDEX_NOT_FOUND;
		}
		int csLen = string.length();
		int csLast = csLen - 1;
		int searchLen = anyChars.length;
		int searchLast = searchLen - 1;
		for (int i = 0; i < csLen; i++) {
			char ch = string.charAt(i);
			for (int j = 0; j < searchLen; j++) {
				if (anyChars[j] == ch) {
					if (i < csLast && j < searchLast && Character.isHighSurrogate(ch)) {
						// ch is a supplementary character
						if (anyChars[j + 1] == string.charAt(i + 1)) {
							return i;
						}
					} else {
						return i;
					}
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Search a CharSequence to find the first index of any character in the given set of characters.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} String will return {@code -1}. A {@code null} search string will return {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.indexOfAny(null, *)            = -1
	 * StringUtils.indexOfAny("", *)              = -1
	 * StringUtils.indexOfAny(*, null)            = -1
	 * StringUtils.indexOfAny(*, "")              = -1
	 * StringUtils.indexOfAny("zzabyycdxx", "za") = 0
	 * StringUtils.indexOfAny("zzabyycdxx", "by") = 3
	 * StringUtils.indexOfAny("aba","z")          = -1
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param anyChars the chars to search for, may be null
	 * @return the index of any of the chars, -1 if no match or null input
	 */
	public static int indexOfAny(CharSequence string, String anyChars) {
		if (isEmpty(string) || isEmpty(anyChars)) {
			return INDEX_NOT_FOUND;
		}
		return indexOfAny(string, anyChars.toCharArray());
	}

	// IndexOfAnyBut chars
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Searches a CharSequence to find the first index of any character not in the given set of characters.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A {@code null} or zero length search array will return
	 * {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOfAnyBut(null, *)                              = -1
	 * Strings.indexOfAnyBut("", *)                                = -1
	 * Strings.indexOfAnyBut(*, null)                              = -1
	 * Strings.indexOfAnyBut(*, [])                                = -1
	 * Strings.indexOfAnyBut("zzabyycdxx", new char[] {'z', 'a'} ) = 3
	 * Strings.indexOfAnyBut("aba", new char[] {'z'} )             = 0
	 * Strings.indexOfAnyBut("aba", new char[] {'a', 'b'} )        = -1
	 * 
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param but the chars to search for, may be null
	 * @return the index of any of the chars, -1 if no match or null input
	 */
	public static int indexOfAnyBut(CharSequence string, char... but) {
		if (isEmpty(string) || Arrays.isEmpty(but)) {
			return INDEX_NOT_FOUND;
		}
		int csLen = string.length();
		int csLast = csLen - 1;
		int searchLen = but.length;
		int searchLast = searchLen - 1;
		outer: for (int i = 0; i < csLen; i++) {
			char ch = string.charAt(i);
			for (int j = 0; j < searchLen; j++) {
				if (but[j] == ch) {
					if (i < csLast && j < searchLast && Character.isHighSurrogate(ch)) {
						if (but[j + 1] == string.charAt(i + 1)) {
							continue outer;
						}
					} else {
						continue outer;
					}
				}
			}
			return i;
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Search a CharSequence to find the first index of any character not in the given set of characters.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A {@code null} or empty search string will return {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.indexOfAnyBut(null, *)            = -1
	 * Strings.indexOfAnyBut("", *)              = -1
	 * Strings.indexOfAnyBut(*, null)            = -1
	 * Strings.indexOfAnyBut(*, "")              = -1
	 * Strings.indexOfAnyBut("zzabyycdxx", "za") = 3
	 * Strings.indexOfAnyBut("zzabyycdxx", "")   = -1
	 * Strings.indexOfAnyBut("aba","ab")         = -1
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param but the chars to search for, may be null
	 * @return the index of any of the chars, -1 if no match or null input
	 */
	public static int indexOfAnyBut(CharSequence string, CharSequence but) {
		if (isEmpty(string) || isEmpty(but)) {
			return INDEX_NOT_FOUND;
		}
		int strLen = string.length();
		for (int i = 0; i < strLen; i++) {
			char ch = string.charAt(i);
			boolean chFound = indexOfWorker(but, ch, 0) >= 0;
			if (i + 1 < strLen && Character.isHighSurrogate(ch)) {
				char ch2 = string.charAt(i + 1);
				if (chFound && indexOfWorker(but, ch2, 0) < 0) {
					return i;
				}
			} else {
				if (!chFound) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	// LastIndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the last index within a CharSequence, handling {@code null}. This method uses
	 * {@link String#lastIndexOf(int)} if possible.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} or empty ("") CharSequence will return {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.lastIndexOf(null, *)         = -1
	 * StringUtils.lastIndexOf("", *)           = -1
	 * StringUtils.lastIndexOf("aabaabaa", 'a') = 7
	 * StringUtils.lastIndexOf("aabaabaa", 'b') = 5
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param indexOf the character to find
	 * @return the last index of the search character, -1 if no match or {@code null} string input
	 */
	public static int lastIndexOf(CharSequence string, int indexOf) {
		if (isEmpty(string)) {
			return INDEX_NOT_FOUND;
		}
		return lastIndexOfWorker(string, indexOf, string.length());
	}

	/**
	 * <p>
	 * Finds the last index within a CharSequence from a start position, handling {@code null}. This method uses
	 * {@link String#lastIndexOf(int, int)} if possible.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} or empty ("") CharSequence will return {@code -1}. A negative start position returns {@code -1}. A
	 * start position greater than the string length searches the whole string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.lastIndexOf(null, *, *)          = -1
	 * StringUtils.lastIndexOf("", *,  *)           = -1
	 * StringUtils.lastIndexOf("aabaabaa", 'b', 8)  = 5
	 * StringUtils.lastIndexOf("aabaabaa", 'b', 4)  = 2
	 * StringUtils.lastIndexOf("aabaabaa", 'b', 0)  = -1
	 * StringUtils.lastIndexOf("aabaabaa", 'b', 9)  = 5
	 * StringUtils.lastIndexOf("aabaabaa", 'b', -1) = -1
	 * StringUtils.lastIndexOf("aabaabaa", 'a', 0)  = 0
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param indexOf the character to find
	 * @param start the start position
	 * @return the last index of the search character, -1 if no match or {@code null} string input
	 */
	public static int lastIndexOf(CharSequence string, char indexOf, int start) {
		if (isEmpty(string)) {
			return INDEX_NOT_FOUND;
		}
		return lastIndexOfWorker(string, indexOf, start);
	}

	/**
	 * <p>
	 * Finds the last index within a CharSequence, handling {@code null}. This method uses
	 * {@link String#lastIndexOf(String)} if possible.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.lastIndexOf(null, *)          = -1
	 * StringUtils.lastIndexOf(*, null)          = -1
	 * StringUtils.lastIndexOf("", "")           = 0
	 * StringUtils.lastIndexOf("aabaabaa", "a")  = 7
	 * StringUtils.lastIndexOf("aabaabaa", "b")  = 5
	 * StringUtils.lastIndexOf("aabaabaa", "ab") = 4
	 * StringUtils.lastIndexOf("aabaabaa", "")   = 8
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param indexOf the CharSequence to find, may be null
	 * @return the last index of the search String, -1 if no match or {@code null} string input
	 */
	public static int lastIndexOf(CharSequence string, CharSequence indexOf) {
		if (string == null || indexOf == null) {
			return INDEX_NOT_FOUND;
		}
		return lastIndexOfWorker(string, indexOf, string.length());
	}

	/**
	 * <p>
	 * Finds the first index within a CharSequence, handling {@code null}. This method uses
	 * {@link String#lastIndexOf(String, int)} if possible.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A negative start position returns {@code -1}. An empty ("")
	 * search CharSequence always matches unless the start position is negative. A start position greater than the
	 * string length searches the whole string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.lastIndexOf(null, *, *)          = -1
	 * StringUtils.lastIndexOf(*, null, *)          = -1
	 * StringUtils.lastIndexOf("aabaabaa", "a", 8)  = 7
	 * StringUtils.lastIndexOf("aabaabaa", "b", 8)  = 5
	 * StringUtils.lastIndexOf("aabaabaa", "ab", 8) = 4
	 * StringUtils.lastIndexOf("aabaabaa", "b", 9)  = 5
	 * StringUtils.lastIndexOf("aabaabaa", "b", -1) = -1
	 * StringUtils.lastIndexOf("aabaabaa", "a", 0)  = 0
	 * StringUtils.lastIndexOf("aabaabaa", "b", 0)  = -1
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param indexOf the CharSequence to find, may be null
	 * @param start the start position, negative treated as zero
	 * @return the first index of the search CharSequence, -1 if no match or {@code null} string input
	 */
	public static int lastIndexOf(CharSequence string, CharSequence indexOf, int start) {
		if (string == null || indexOf == null) {
			return INDEX_NOT_FOUND;
		}
		return lastIndexOfWorker(string, indexOf, start);
	}

	/**
	 * <p>
	 * Case in-sensitive find of the last index within a CharSequence.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A negative start position returns {@code -1}. An empty ("")
	 * search CharSequence always matches unless the start position is negative. A start position greater than the
	 * string length searches the whole string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.lastIndexOfIgnoreCase(null, *)          = -1
	 * StringUtils.lastIndexOfIgnoreCase(*, null)          = -1
	 * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A")  = 7
	 * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B")  = 5
	 * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "AB") = 4
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param indexOf the CharSequence to find, may be null
	 * @return the first index of the search CharSequence, -1 if no match or {@code null} string input
	 * @since 2.5
	 * @since 3.0 Changed signature from lastIndexOfIgnoreCase(String, String) to lastIndexOfIgnoreCase(CharSequence,
	 *        CharSequence)
	 */
	public static int lastIndexOfIgnoreCase(CharSequence string, CharSequence indexOf) {
		if (string == null || indexOf == null) {
			return INDEX_NOT_FOUND;
		}
		return lastIndexOfIgnoreCase(string, indexOf, string.length());
	}

	/**
	 * <p>
	 * Case in-sensitive find of the last index within a CharSequence from the specified position.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} CharSequence will return {@code -1}. A negative start position returns {@code -1}. An empty ("")
	 * search CharSequence always matches unless the start position is negative. A start position greater than the
	 * string length searches the whole string.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.lastIndexOfIgnoreCase(null, *, *)          = -1
	 * StringUtils.lastIndexOfIgnoreCase(*, null, *)          = -1
	 * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A", 8)  = 7
	 * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 8)  = 5
	 * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "AB", 8) = 4
	 * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 9)  = 5
	 * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", -1) = -1
	 * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 0)  = -1
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param indexOf the CharSequence to find, may be null
	 * @param start the start position
	 * @return the first index of the search CharSequence, -1 if no match or {@code null} input
	 */
	public static int lastIndexOfIgnoreCase(CharSequence string, CharSequence indexOf, int start) {
		if (string == null || indexOf == null) {
			return INDEX_NOT_FOUND;
		}

		if (start > string.length() - indexOf.length()) {
			start = string.length() - indexOf.length();
		}

		if (start < 0) {
			return INDEX_NOT_FOUND;
		}

		if (indexOf.length() == 0) {
			return start;
		}

		for (int i = start; i >= 0; i--) {
			if (regionMatches(string, true, i, indexOf, 0, indexOf.length())) {
				return i;
			}
		}

		return INDEX_NOT_FOUND;
	}

	// Contains
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if String contains a search character, handling {@code null}. This method uses {@link String#indexOf(int)}
	 * if possible.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} or empty ("") String will return {@code false}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.contains(null, *)    = false
	 * Strings.contains("", *)      = false
	 * Strings.contains("abc", 'a') = true
	 * Strings.contains("abc", 'z') = false
	 * </pre>
	 * 
	 * @param string the String to check, may be null
	 * 
	 * @param contains the character to find
	 * 
	 * @return true if the String contains the search character, false if not or {@code null} string input
	 */
	public static boolean contains(CharSequence string, char contains) {
		if (isEmpty(string)) {
			return false;
		}
		return indexOfWorker(string, contains, 0) >= 0;
	}

	/**
	 * <p>
	 * Checks if String contains a search CharSequence, handling {@code null}. This method uses
	 * {@link String#indexOf(String)} if possible.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} String will return {@code false}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.contains(null, *)     = false
	 * Strings.contains(*, null)     = false
	 * Strings.contains("", "")      = true
	 * Strings.contains("abc", "")   = true
	 * Strings.contains("abc", "a")  = true
	 * Strings.contains("abc", "z")  = false
	 * </pre>
	 * 
	 * @param string the String to check, may be null
	 * 
	 * @param contains the String to find, may be null
	 * 
	 * @return true if the String contains the search String, false if not or {@code null} string input
	 */
	public static boolean contains(CharSequence string, CharSequence contains) {
		if (string == null || contains == null) {
			return false;
		}
		return indexOfWorker(string, contains, 0) >= 0;
	}

	/**
	 * <p>
	 * Checks if String contains a search String irrespective of case, handling {@code null}.
	 * 
	 * Case-insensitivity is defined as by {@link String#equalsIgnoreCase(String)}.
	 * 
	 * <p>
	 * A {@code null} String will return {@code false}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.contains(null, *) = false
	 * Strings.contains(*, null) = false
	 * Strings.contains("", "") = true
	 * Strings.contains("abc", "") = true
	 * Strings.contains("abc", "a") = true
	 * Strings.contains("abc", "z") = false
	 * Strings.contains("abc", "A") = true
	 * Strings.contains("abc", "Z") = false
	 * </pre>
	 * 
	 * @param string the String to check, may be null
	 * @param contains the String to find, may be null
	 * @return true if the String contains the search String irrespective of case or false if not or {@code null} string
	 *         input
	 */
	public static boolean containsIgnoreCase(CharSequence string, CharSequence contains) {
		if (string == null || contains == null) {
			return false;
		}
		int len = contains.length();
		int max = string.length() - len;
		for (int i = 0; i <= max; i++) {
			if (regionMatches(string, true, i, contains, 0, len)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given CharSequence contains any whitespace characters.
	 * 
	 * @param string the CharSequence to check (may be {@code null})
	 * 
	 * @return {@code true} if the CharSequence is not empty and contains at least 1 whitespace character
	 * 
	 * @see java.lang.Character#isWhitespace
	 */
	public static boolean containsWhitespace(CharSequence string) {
		if (isEmpty(string)) {
			return false;
		}
		int strLen = string.length();
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(string.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	// ContainsOnly
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if the CharSequence contains only certain characters.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} CharSequence will return {@code false}. A {@code null} valid character array will return {@code
	 * false}. An empty CharSequence (length()=0) always returns {@code true}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.containsOnly(null, *)       = false
	 * Strings.containsOnly(*, null)       = false
	 * Strings.containsOnly("", *)         = true
	 * Strings.containsOnly("ab", '')      = false
	 * Strings.containsOnly("abab", 'abc') = true
	 * Strings.containsOnly("ab1", 'abc')  = false
	 * Strings.containsOnly("abz", 'abc')  = false
	 * </pre>
	 * 
	 * @param string the String to check, may be null
	 * 
	 * @param contains an array of valid chars, may be null
	 * 
	 * @return true if it only contains valid chars and is non-null
	 */
	public static boolean containsOnly(CharSequence string, char... contains) {
		// All these pre-checks are to maintain API with an older version
		if (contains == null || string == null) {
			return false;
		}
		if (string.length() == 0) {
			return true;
		}
		if (contains.length == 0) {
			return false;
		}
		return indexOfAnyBut(string, contains) == INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only certain characters.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} CharSequence will return {@code false}. A {@code null} valid character String will return {@code
	 * false}. An empty String (length()=0) always returns {@code true}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.containsOnly(null, *)       = false
	 * Strings.containsOnly(*, null)       = false
	 * Strings.containsOnly("", *)         = true
	 * Strings.containsOnly("ab", "")      = false
	 * Strings.containsOnly("abab", "abc") = true
	 * Strings.containsOnly("ab1", "abc")  = false
	 * Strings.containsOnly("abz", "abc")  = false
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param contains a String of valid chars, may be null
	 * @return true if it only contains valid chars and is non-null
	 */
	public static boolean containsOnly(CharSequence string, String contains) {
		if (string == null || contains == null) {
			return false;
		}
		return containsOnly(string, contains.toCharArray());
	}

	// ContainsNone
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks that the CharSequence does not contain certain characters.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} CharSequence will return {@code true}. A {@code null} invalid character array will return {@code
	 * true}. An empty CharSequence (length()=0) always returns true.
	 * </p>
	 * 
	 * <pre>
	 * Strings.containsNone(null, *)       = true
	 * Strings.containsNone(*, null)       = true
	 * Strings.containsNone("", *)         = true
	 * Strings.containsNone("ab", '')      = true
	 * Strings.containsNone("abab", 'xyz') = true
	 * Strings.containsNone("ab1", 'xyz')  = true
	 * Strings.containsNone("abz", 'xyz')  = false
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param contains an array of invalid chars, may be null
	 * @return true if it contains none of the invalid chars, or is null
	 * @since 2.0
	 * @since 3.0 Changed signature from containsNone(String, char[]) to containsNone(CharSequence, char...)
	 */
	public static boolean containsNone(CharSequence string, char... contains) {
		if (string == null || contains == null) {
			return true;
		}
		int csLen = string.length();
		int csLast = csLen - 1;
		int searchLen = contains.length;
		int searchLast = searchLen - 1;
		for (int i = 0; i < csLen; i++) {
			char ch = string.charAt(i);
			for (int j = 0; j < searchLen; j++) {
				if (contains[j] == ch) {
					if (Character.isHighSurrogate(ch)) {
						if (j == searchLast) {
							// missing low surrogate, fine, like String.indexOf(String)
							return false;
						}
						if (i < csLast && contains[j + 1] == string.charAt(i + 1)) {
							return false;
						}
					} else {
						// ch is in the Basic Multilingual Plane
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks that the CharSequence does not contain certain characters.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} CharSequence will return {@code true}. A {@code null} invalid character array will return {@code
	 * true}. An empty String ("") always returns true.
	 * </p>
	 * 
	 * <pre>
	 * Strings.containsNone(null, *)       = true
	 * Strings.containsNone(*, null)       = true
	 * Strings.containsNone("", *)         = true
	 * Strings.containsNone("ab", "")      = true
	 * Strings.containsNone("abab", "xyz") = true
	 * Strings.containsNone("ab1", "xyz")  = true
	 * Strings.containsNone("abz", "xyz")  = false
	 * </pre>
	 * 
	 * @param string the CharSequence to check, may be null
	 * @param contains a String of invalid chars, may be null
	 * @return true if it contains none of the invalid chars, or is null
	 */
	public static boolean containsNone(CharSequence string, String contains) {
		if (string == null || contains == null) {
			return true;
		}
		return containsNone(string, contains.toCharArray());
	}

	// startsWith
	//-----------------------------------------------------------------------

	/**
	 * <p>
	 * Check if a String starts with a specified prefix.
	 * </p>
	 * 
	 * <p>
	 * {@code null}s are handled without exceptions. Two {@code null} references are considered to be equal. The
	 * comparison is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * Strings.startsWith(null, null)      = true
	 * Strings.startsWith(null, "abc")     = false
	 * Strings.startsWith("abcdef", null)  = false
	 * Strings.startsWith("abcdef", "abc") = true
	 * Strings.startsWith("ABCDEF", "abc") = false
	 * </pre>
	 * 
	 * @see java.lang.String#startsWith(String)
	 * 
	 * @param string the String to check, may be null
	 * 
	 * @param startsWith the prefix to find, may be null
	 * 
	 * @return {@code true} if the String starts with the prefix, case sensitive, or both {@code null}
	 */
	public static boolean startsWith(CharSequence string, CharSequence startsWith) {
		return startsWith(string, startsWith, false);
	}

	/**
	 * <p>
	 * Case insensitive check if a String starts with a specified prefix.
	 * </p>
	 * 
	 * <p>
	 * {@code null}s are handled without exceptions. Two {@code null} references are considered to be equal. The
	 * comparison is case insensitive.
	 * </p>
	 * 
	 * <pre>
	 * Strings.startsWithIgnoreCase(null, null)      = true
	 * Strings.startsWithIgnoreCase(null, "abc")     = false
	 * Strings.startsWithIgnoreCase("abcdef", null)  = false
	 * Strings.startsWithIgnoreCase("abcdef", "abc") = true
	 * Strings.startsWithIgnoreCase("ABCDEF", "abc") = true
	 * </pre>
	 * 
	 * @see java.lang.String#startsWith(String)
	 * 
	 * @param string the String to check, may be null
	 * 
	 * @param startsWith the prefix to find, may be null
	 * 
	 * @return {@code true} if the String starts with the prefix, case insensitive, or both {@code null}
	 */
	public static boolean startsWithIgnoreCase(CharSequence string, CharSequence startsWith) {
		return startsWith(string, startsWith, true);
	}

	// endsWith
	//-----------------------------------------------------------------------

	/**
	 * <p>
	 * Check if a String ends with a specified suffix.
	 * </p>
	 * 
	 * <p>
	 * {@code null}s are handled without exceptions. Two {@code null} references are considered to be equal. The
	 * comparison is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * Strings.endsWith(null, null)      = true
	 * Strings.endsWith(null, "def")     = false
	 * Strings.endsWith("abcdef", null)  = false
	 * Strings.endsWith("abcdef", "def") = true
	 * Strings.endsWith("ABCDEF", "def") = false
	 * Strings.endsWith("ABCDEF", "cde") = false
	 * </pre>
	 * 
	 * @see java.lang.String#endsWith(String)
	 * 
	 * @param string the String to check, may be null
	 * 
	 * @param endsWith the suffix to find, may be null
	 * 
	 * @return {@code true} if the String ends with the suffix, case sensitive, or both {@code null}
	 */
	public static boolean endsWith(CharSequence string, CharSequence endsWith) {
		return endsWith(string, endsWith, false);
	}

	/**
	 * <p>
	 * Case insensitive check if a String ends with a specified suffix.
	 * </p>
	 * 
	 * <p>
	 * {@code null}s are handled without exceptions. Two {@code null} references are considered to be equal. The
	 * comparison is case insensitive.
	 * </p>
	 * 
	 * <pre>
	 * Strings.endsWithIgnoreCase(null, null)      = true
	 * Strings.endsWithIgnoreCase(null, "def")     = false
	 * Strings.endsWithIgnoreCase("abcdef", null)  = false
	 * Strings.endsWithIgnoreCase("abcdef", "def") = true
	 * Strings.endsWithIgnoreCase("ABCDEF", "def") = true
	 * Strings.endsWithIgnoreCase("ABCDEF", "cde") = false
	 * </pre>
	 * 
	 * @see java.lang.String#endsWith(String)
	 * 
	 * @param string the String to check, may be null
	 * 
	 * @param endsWith the suffix to find, may be null
	 * 
	 * @return {@code true} if the String ends with the suffix, case insensitive, or both {@code null}
	 */
	public static boolean endsWithIgnoreCase(CharSequence string, CharSequence endsWith) {
		return endsWith(string, endsWith, true);
	}

	// Remove
	//-----------------------------------------------------------------------

	/**
	 * <p>
	 * Removes all occurrences of a substring from within the source string.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} search string will return the source string. A {@code null} source string will return the empty
	 * string. string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.remove(null, *)        = ""
	 * Strings.remove("", *)          = ""
	 * Strings.remove(*, null)        = *
	 * Strings.remove(*, "")          = *
	 * Strings.remove("queued", "ue") = "qd"
	 * Strings.remove("queued", "zz") = "queued"
	 * </pre>
	 * 
	 * @param string the source String to search, may be null
	 * 
	 * @param remove the String to search for and remove, may be null
	 * 
	 * @return the substring with the string removed if found, "" if null String input
	 */
	public static String remove(String string, String remove) {
		if (isEmpty(string)) {
			return EMPTY;
		}

		if (isEmpty(remove)) {
			return string;
		}

		return replace(string, remove, EMPTY, -1);
	}

	/**
	 * <p>
	 * Removes all occurrences of a character from within the source string.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} source string will return the empty string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.remove(null, *)       = null
	 * Strings.remove("", *)         = ""
	 * Strings.remove("queued", 'u') = "qeed"
	 * Strings.remove("queued", 'z') = "queued"
	 * </pre>
	 * 
	 * @param string the source String to search, may be null
	 * 
	 * @param remove the char to search for and remove, may be null
	 * 
	 * @return the substring with the char removed if found, "" if null String input
	 */
	public static String remove(String string, char remove) {
		if (isEmpty(string)) {
			return EMPTY;
		}

		if (string.indexOf(remove) == INDEX_NOT_FOUND) {
			return string;
		}

		char[] chars = string.toCharArray();
		int pos = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != remove) {
				chars[pos++] = chars[i];
			}
		}
		return new String(chars, 0, pos);
	}

	/**
	 * <p>
	 * Removes all blank string from a String as defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.removeBlank(null)         = ""
	 * StringUtils.removeBlank("")           = ""
	 * StringUtils.removeBlank("abc")        = "abc"
	 * StringUtils.removeBlank("   ab  c  ") = "abc"
	 * </pre>
	 * 
	 * @param str the String to delete whitespace from, may be null
	 * 
	 * @return the String without whitespaces, "" if null String input
	 */
	public static String removeBlank(String str) {
		if (isEmpty(str)) {
			return EMPTY;
		}
		int sz = str.length();
		char[] chs = new char[sz];
		int count = 0;
		for (int i = 0; i < sz; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				chs[count++] = str.charAt(i);
			}
		}
		if (count == sz) {
			return str;
		}
		return new String(chs, 0, count);
	}

	/**
	 * <p>
	 * Removes a substring only if it is at the beginning of a source string, otherwise returns the source string.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} search string will return the source string. A {@code null} source string will return the empty
	 * string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.removeStart(null, *)      			   = ""
	 * Strings.removeStart("", *)        			   = ""
	 * Strings.removeStart(*, null)      			   = *
	 * Strings.removeStart("www.domain.com", "www.")   = "domain.com"
	 * Strings.removeStart("domain.com", "www.")       = "domain.com"
	 * Strings.removeStart("www.domain.com", "domain") = "www.domain.com"
	 * Strings.removeStart("abc", "")    			   = "abc"
	 * </pre>
	 * 
	 * @param string the source String to search, may be null
	 * 
	 * @param remove the String to search for and remove, may be null
	 * 
	 * @return the substring with the string removed if found, "" if null String input
	 */
	public static String removeStart(String string, String remove) {
		if (isEmpty(string)) {
			return EMPTY;
		}

		if (isEmpty(remove)) {
			return string;
		}

		if (string.startsWith(remove)) {
			return string.substring(remove.length());
		}

		return string;
	}

	/**
	 * <p>
	 * Case insensitive removal of a substring if it is at the beginning of a source string, otherwise returns the
	 * source string.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} search string will return the source string. A {@code null} source string will return the empty
	 * string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.removeStartIgnoreCase(null, *)      				 = ""
	 * Strings.removeStartIgnoreCase("", *)        				 = ""
	 * Strings.removeStartIgnoreCase(*, null)      				 = *
	 * Strings.removeStartIgnoreCase("www.domain.com", "www.")   = "domain.com"
	 * Strings.removeStartIgnoreCase("www.domain.com", "WWW.")   = "domain.com"
	 * Strings.removeStartIgnoreCase("domain.com", "www.")       = "domain.com"
	 * Strings.removeStartIgnoreCase("www.domain.com", "domain") = "www.domain.com"
	 * Strings.removeStartIgnoreCase("abc", "")    				 = "abc"
	 * </pre>
	 * 
	 * @param string the source String to search, may be null
	 * 
	 * @param remove the String to search for (case insensitive) and remove, may be null
	 * 
	 * @return the substring with the string removed if found, "" if null String input
	 */
	public static String removeStartIgnoreCase(String string, String remove) {
		if (isEmpty(string)) {
			return EMPTY;
		}
		if (isEmpty(remove)) {
			return string;
		}
		if (startsWithIgnoreCase(string, remove)) {
			return string.substring(remove.length());
		}
		return string;
	}

	/**
	 * <p>
	 * Removes a substring only if it is at the end of a source string, otherwise returns the source string.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} search string will return the source string. A {@code null} source string will return the empty
	 * string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.removeEnd(null, *)      = ""
	 * Strings.removeEnd("", *)        = ""
	 * Strings.removeEnd(*, null)      = *
	 * Strings.removeEnd("www.domain.com", ".com.")  = "www.domain.com"
	 * Strings.removeEnd("www.domain.com", ".com")   = "www.domain"
	 * Strings.removeEnd("www.domain.com", "domain") = "www.domain.com"
	 * Strings.removeEnd("abc", "")    = "abc"
	 * </pre>
	 * 
	 * @param string the source String to search, may be null
	 * 
	 * @param remove the String to search for and remove, may be null
	 * 
	 * @return the substring with the string removed if found, "" if null String input
	 */
	public static String removeEnd(String string, String remove) {
		if (isEmpty(string)) {
			return EMPTY;
		}

		if (isEmpty(remove)) {
			return string;
		}

		if (string.endsWith(remove)) {
			return string.substring(0, string.length() - remove.length());
		}

		return string;
	}

	/**
	 * <p>
	 * Case insensitive removal of a substring if it is at the end of a source string, otherwise returns the source
	 * string.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} search string will return the source string. A {@code null} source string will return the empty
	 * string.
	 * </p>
	 * 
	 * <pre>
	 * Strings.removeEndIgnoreCase(null, *)      = ""
	 * Strings.removeEndIgnoreCase("", *)        = ""
	 * Strings.removeEndIgnoreCase(*, null)      = *
	 * Strings.removeEndIgnoreCase("www.domain.com", ".com.")  = "www.domain.com"
	 * Strings.removeEndIgnoreCase("www.domain.com", ".com")   = "www.domain"
	 * Strings.removeEndIgnoreCase("www.domain.com", "domain") = "www.domain.com"
	 * Strings.removeEndIgnoreCase("abc", "")    = "abc"
	 * Strings.removeEndIgnoreCase("www.domain.com", ".COM") = "www.domain")
	 * Strings.removeEndIgnoreCase("www.domain.COM", ".com") = "www.domain")
	 * </pre>
	 * 
	 * @param str the source String to search, may be null
	 * 
	 * @param remove the String to search for (case insensitive) and remove, may be null
	 * 
	 * @return the substring with the string removed if found, "" if null String input
	 */
	public static String removeEndIgnoreCase(String str, String remove) {
		if (isEmpty(str)) {
			return EMPTY;
		}

		if (isEmpty(remove)) {
			return str;
		}

		if (endsWithIgnoreCase(str, remove)) {
			return str.substring(0, str.length() - remove.length());
		}

		return str;
	}

	// Repeat & Padding
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Repeat a String {@code repeat} times to form a new String.
	 * </p>
	 * 
	 * <pre>
	 * Strings.repeat(null, 2) = ""
	 * Strings.repeat("", 0)   = ""
	 * Strings.repeat("", 2)   = ""
	 * Strings.repeat("a", 3)  = "aaa"
	 * Strings.repeat("ab", 2) = "abab"
	 * Strings.repeat("a", -2) = ""
	 * </pre>
	 * 
	 * @param repeat the String to repeat, may be null
	 * 
	 * @param length number of times to repeat str, negative treated as zero
	 * 
	 * @return a new String consisting of the original String repeated, "" if null or empty String input
	 */
	public static String repeat(String repeat, int length) {
		// Performance tuned for 2.0 (JDK1.4)

		if (isEmpty(repeat)) {
			return EMPTY;
		}

		if (length <= 0) {
			return EMPTY;
		}

		int inputLength = repeat.length();

		if (length == 1 || inputLength == 0) {
			return repeat;
		}

		if (inputLength == 1 && length <= PADDING_LIMIT) {
			return repeat(repeat.charAt(0), length);
		}

		int outputLength = inputLength * length;

		switch (inputLength) {
			case 1:
				return repeat(repeat.charAt(0), length);
			case 2:
				char ch0 = repeat.charAt(0);
				char ch1 = repeat.charAt(1);
				char[] output2 = new char[outputLength];
				for (int i = length * 2 - 2; i >= 0; i--, i--) {
					output2[i] = ch0;
					output2[i + 1] = ch1;
				}
				return new String(output2);
			default:
				StringBuilder buf = new StringBuilder(outputLength);
				for (int i = 0; i < length; i++) {
					buf.append(repeat);
				}
				return buf.toString();
		}
	}

	/**
	 * <p>
	 * Repeat a String {@code repeat} times to form a new String, with a String separator injected each time.
	 * </p>
	 * 
	 * <pre>
	 * Strings.repeat(null, null, 2) = ""
	 * Strings.repeat(null, "x", 2)  = ""
	 * Strings.repeat("", null, 0)   = ""
	 * Strings.repeat("", "", 2)     = ""
	 * Strings.repeat("", "x", 3)    = "xxx"
	 * Strings.repeat("?", ", ", 3)  = "?, ?, ?"
	 * </pre>
	 * 
	 * @param repeat the String to repeat, may be null
	 * 
	 * @param separator the String to inject, may be null
	 * 
	 * @param length number of times to repeat str, negative treated as zero
	 * 
	 * @return a new String consisting of the original String repeated, "" if null String input
	 */
	public static String repeat(String repeat, String separator, int length) {
		if (repeat == null || separator == null) {
			return repeat(repeat, length);
		} else {
			// given that repeat(String, int) is quite optimized, better to rely on it than try and splice this into it
			String result = repeat(repeat + separator, length);
			return removeEnd(result, separator);
		}
	}

	/**
	 * <p>
	 * Returns padding using the specified delimiter repeated to a given length.
	 * </p>
	 * 
	 * <pre>
	 * Strings.repeat(0, 'e')  = ""
	 * Strings.repeat(3, 'e')  = "eee"
	 * Strings.repeat(-2, 'e') = ""
	 * </pre>
	 * 
	 * <p>
	 * Note: this method doesn't not support padding with <a
	 * href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary Characters</a> as they
	 * require a pair of {@code char}s to be represented. If you are needing to support full I18N of your applications
	 * consider using {@link #repeat(String, int)} instead.
	 * </p>
	 * 
	 * @param repeat character to repeat
	 * 
	 * @param length number of times to repeat char, negative treated as zero
	 * 
	 * @return String with repeated character
	 * 
	 * @see #repeat(String, int)
	 */
	public static String repeat(char repeat, int length) {
		char[] buf = new char[length];
		for (int i = length - 1; i >= 0; i--) {
			buf[i] = repeat;
		}
		return new String(buf);
	}

	/**
	 * <p>
	 * Right pad a String with spaces (' ').
	 * </p>
	 * 
	 * <p>
	 * The String is padded to the size of {@code size}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.padRight(null, *)   = ""
	 * Strings.padRight("", 3)     = "   "
	 * Strings.padRight("bat", 3)  = "bat"
	 * Strings.padRight("bat", 5)  = "bat  "
	 * Strings.padRight("bat", 1)  = "bat"
	 * Strings.padRight("bat", -1) = "bat"
	 * </pre>
	 * 
	 * @param str the String to pad out, may be null
	 * 
	 * @param size the size to pad to
	 * 
	 * @return right padded String or original String if no padding is necessary, "" if null String input
	 */
	public static String padRight(String str, int size) {
		return padRight(str, ' ', size);
	}

	/**
	 * <p>
	 * Right pad a String with a specified character.
	 * </p>
	 * 
	 * <p>
	 * The String is padded to the size of {@code size}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.padRight(null, *, *)     = ""
	 * Strings.padRight("", 3, 'z')     = "zzz"
	 * Strings.padRight("bat", 3, 'z')  = "bat"
	 * Strings.padRight("bat", 5, 'z')  = "batzz"
	 * Strings.padRight("bat", 1, 'z')  = "bat"
	 * Strings.padRight("bat", -1, 'z') = "bat"
	 * </pre>
	 * 
	 * @param str the String to pad out, may be null
	 * 
	 * @param size the size to pad to
	 * 
	 * @param padding the character to pad with
	 * 
	 * @return right padded String or original String if no padding is necessary, "" if null String input
	 */
	public static String padRight(String str, char padding, int size) {
		if (str == null) {
			return EMPTY;
		}
		int pads = size - str.length();
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (pads > PADDING_LIMIT) {
			return padRight(str, String.valueOf(padding), size);
		}
		return str.concat(repeat(padding, pads));
	}

	/**
	 * <p>
	 * Right pad a String with a specified String.
	 * </p>
	 * 
	 * <p>
	 * The String is padded to the size of {@code size}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.padRight(null, *, *)      = ""
	 * Strings.padRight("", 3, "z")      = "zzz"
	 * Strings.padRight("bat", 3, "yz")  = "bat"
	 * Strings.padRight("bat", 5, "yz")  = "batyz"
	 * Strings.padRight("bat", 8, "yz")  = "batyzyzy"
	 * Strings.padRight("bat", 1, "yz")  = "bat"
	 * Strings.padRight("bat", -1, "yz") = "bat"
	 * Strings.padRight("bat", 5, null)  = "bat  "
	 * Strings.padRight("bat", 5, "")    = "bat  "
	 * </pre>
	 * 
	 * @param str the String to pad out, may be null
	 * 
	 * @param size the size to pad to
	 * 
	 * @param padding the String to pad with, null or empty treated as single space
	 * 
	 * @return right padded String or original String if no padding is necessary, "" if null String input
	 */
	public static String padRight(String str, String padding, int size) {
		if (str == null) {
			return EMPTY;
		}
		if (isEmpty(padding)) {
			padding = " ";
		}
		int padLen = padding.length();
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (padLen == 1 && pads <= PADDING_LIMIT) {
			return padRight(str, padding.charAt(0), size);
		}

		if (pads == padLen) {
			return str.concat(padding);
		} else if (pads < padLen) {
			return str.concat(padding.substring(0, pads));
		} else {
			char[] paddings = new char[pads];
			char[] padChars = padding.toCharArray();
			for (int i = 0; i < pads; i++) {
				paddings[i] = padChars[i % padLen];
			}
			return str.concat(new String(paddings));
		}
	}

	/**
	 * <p>
	 * Left pad a String with spaces (' ').
	 * </p>
	 * 
	 * <p>
	 * The String is padded to the size of {@code size}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.padLeft(null, *)   = ""
	 * Strings.padLeft("", 3)     = "   "
	 * Strings.padLeft("bat", 3)  = "bat"
	 * Strings.padLeft("bat", 5)  = "  bat"
	 * Strings.padLeft("bat", 1)  = "bat"
	 * Strings.padLeft("bat", -1) = "bat"
	 * </pre>
	 * 
	 * @param str the String to pad out, may be null
	 * 
	 * @param size the size to pad to
	 * 
	 * @return left padded String or original String if no padding is necessary, "" if null String input
	 */
	public static String padLeft(String str, int size) {
		return padLeft(str, ' ', size);
	}

	/**
	 * <p>
	 * Left pad a String with a specified character.
	 * </p>
	 * 
	 * <p>
	 * Pad to a size of {@code size}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.padLeft(null, *, *)     = ""
	 * Strings.padLeft("", 3, 'z')     = "zzz"
	 * Strings.padLeft("bat", 3, 'z')  = "bat"
	 * Strings.padLeft("bat", 5, 'z')  = "zzbat"
	 * Strings.padLeft("bat", 1, 'z')  = "bat"
	 * Strings.padLeft("bat", -1, 'z') = "bat"
	 * </pre>
	 * 
	 * @param str the String to pad out, may be null
	 * 
	 * @param padding the character to pad with
	 * 
	 * @param size the size to pad to
	 * 
	 * @return left padded String or original String if no padding is necessary, "" if null String input
	 */
	public static String padLeft(String str, char padding, int size) {
		if (str == null) {
			return EMPTY;
		}
		int pads = size - str.length();
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (pads > PADDING_LIMIT) {
			return padLeft(str, String.valueOf(padding), size);
		}
		return repeat(padding, pads).concat(str);
	}

	/**
	 * <p>
	 * Left pad a String with a specified String.
	 * </p>
	 * 
	 * <p>
	 * Pad to a size of {@code size}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.padLeft(null, *, *)      = ""
	 * Strings.padLeft("", 3, "z")      = "zzz"
	 * Strings.padLeft("bat", 3, "yz")  = "bat"
	 * Strings.padLeft("bat", 5, "yz")  = "yzbat"
	 * Strings.padLeft("bat", 8, "yz")  = "yzyzybat"
	 * Strings.padLeft("bat", 1, "yz")  = "bat"
	 * Strings.padLeft("bat", -1, "yz") = "bat"
	 * Strings.padLeft("bat", 5, null)  = "  bat"
	 * Strings.padLeft("bat", 5, "")    = "  bat"
	 * </pre>
	 * 
	 * @param str the String to pad out, may be null
	 * 
	 * @param size the size to pad to
	 * 
	 * @param padding the String to pad with, null or empty treated as single space
	 * 
	 * @return left padded String or original String if no padding is necessary, "" if null String input
	 */
	public static String padLeft(String str, String padding, int size) {
		if (null == str) {
			return EMPTY;
		}

		if (isEmpty(padding)) {
			padding = " ";
		}

		int padLen = padding.length();
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (padLen == 1 && pads <= PADDING_LIMIT) {
			return padLeft(str, padding.charAt(0), size);
		}

		if (pads == padLen) {
			return padding.concat(str);
		} else if (pads < padLen) {
			return padding.substring(0, pads).concat(str);
		} else {
			char[] paddings = new char[pads];
			char[] padChars = padding.toCharArray();
			for (int i = 0; i < pads; i++) {
				paddings[i] = padChars[i % padLen];
			}
			return new String(paddings).concat(str);
		}
	}

	// Reversing
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Reverses a String as per {@link StringBuilder#reverse()}.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} String returns "".
	 * </p>
	 * 
	 * <pre>
	 * Strings.reverse(null)  = ""
	 * Strings.reverse("")    = ""
	 * Strings.reverse("bat") = "tab"
	 * </pre>
	 * 
	 * @param str the String to reverse, may be null
	 * 
	 * @return the reversed String, "" if null String input
	 */
	public static String reverse(String str) {
		if (str == null) {
			return EMPTY;
		}
		return new StringBuilder(str).reverse().toString();
	}

	//private methods
	//----------------------------------------------------------------------------------------------------------------------

	/**
	 * <p>
	 * Check if a String starts with a specified prefix (optionally case insensitive).
	 * </p>
	 * 
	 * @see java.lang.String#startsWith(String)
	 * 
	 * @param str the String to check, may be null
	 * 
	 * @param prefix the prefix to find, may be null
	 * 
	 * @param ignoreCase indicates whether the compare should ignore case (case insensitive) or not.
	 * 
	 * @return {@code true} if the String starts with the prefix or both {@code null}
	 */
	private static boolean startsWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
		if (str == null || prefix == null) {
			return str == null && prefix == null;
		}
		if (prefix.length() > str.length()) {
			return false;
		}
		return regionMatches(str, ignoreCase, 0, prefix, 0, prefix.length());
	}

	/**
	 * <p>
	 * Check if a String ends with a specified suffix (optionally case insensitive).
	 * </p>
	 * 
	 * @see java.lang.String#endsWith(String)
	 * 
	 * @param str the String to check, may be null
	 * 
	 * @param suffix the suffix to find, may be null
	 * 
	 * @param ignoreCase indicates whether the compare should ignore case (case insensitive) or not.
	 * 
	 * @return {@code true} if the String starts with the prefix or both {@code null}
	 */
	private static boolean endsWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
		if (str == null || suffix == null) {
			return str == null && suffix == null;
		}
		if (suffix.length() > str.length()) {
			return false;
		}
		int strOffset = str.length() - suffix.length();

		return regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
	}

	//	private static boolean regionMatches(String string, boolean ignoreCase, int thisStart, String substring, int start, int length) {
	//		return string.regionMatches(ignoreCase, thisStart, substring, start, length);
	//	}

	/**
	 * <p>
	 * Replaces a String with another String inside a larger String, for the first {@code max} values of the search
	 * String.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} reference passed to this method is a no-op.
	 * </p>
	 * 
	 * <pre>
	 * Strings.replace(null, *, *, *)         = null
	 * Strings.replace("", *, *, *)           = ""
	 * Strings.replace("any", null, *, *)     = "any"
	 * Strings.replace("any", *, null, *)     = "any"
	 * Strings.replace("any", "", *, *)       = "any"
	 * Strings.replace("any", *, *, 0)        = "any"
	 * Strings.replace("abaa", "a", null, -1) = "abaa"
	 * Strings.replace("abaa", "a", "", -1)   = "b"
	 * Strings.replace("abaa", "a", "z", 0)   = "abaa"
	 * Strings.replace("abaa", "a", "z", 1)   = "zbaa"
	 * Strings.replace("abaa", "a", "z", 2)   = "zbza"
	 * Strings.replace("abaa", "a", "z", -1)  = "zbzz"
	 * </pre>
	 * 
	 * @param text text to search and replace in, may be null
	 * @param oldString the String to search for, may be null
	 * @param newString the String to replace it with, may be null
	 * @param max maximum number of values to replace, or {@code -1} if no maximum
	 * @return the text with any replacements processed, empty("") if null String input
	 */
	private static String replace(String text, String oldString, String newString, int max) {
		if (isEmpty(text) || isEmpty(oldString) || newString == null || max == 0) {
			return safe(text);
		}
		int start = 0;
		int end = text.indexOf(oldString, start);
		if (end == -1) {
			return text;
		}
		int replLength = oldString.length();
		int increase = newString.length() - replLength;
		increase = increase < 0 ? 0 : increase;
		increase *= max < 0 ? 16 : max > 64 ? 64 : max;
		StringBuilder buf = new StringBuilder(text.length() + increase);
		while (end != -1) {
			buf.append(text.substring(start, end)).append(newString);
			start = end + replLength;
			if (--max == 0) {
				break;
			}
			end = text.indexOf(oldString, start);
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	/**
	 * Performs the logic for the {@code split} and {@code splitPreserveAllTokens} methods that do not return a maximum
	 * array length.
	 * 
	 * @param str the String to parse, may be {@code null}
	 * @param separatorChar the separate character
	 * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty token separators; if {@code
	 *            false}, adjacent separators are treated as one separator.
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens, boolean trimTokens, boolean ignoreEmptyTokens) {
		// Performance tuned for 2.0 (JDK1.4)

		if (str == null) {
			return Arrays.EMPTY_STRING_ARRAY;
		}
		int len = str.length();
		if (len == 0) {
			return Arrays.EMPTY_STRING_ARRAY;
		}
		List<String> list = new ArrayList<String>();
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		while (i < len) {
			if (str.charAt(i) == separatorChar) {
				if (match || preserveAllTokens) {

					String token = str.substring(start, i);

					if (trimTokens) {
						token = trim(token);
					}

					if (!ignoreEmptyTokens || token.length() > 0) {
						list.add(token);
					}

					match = false;
					lastMatch = true;
				}
				start = ++i;
				continue;
			}
			lastMatch = false;
			match = true;
			i++;
		}
		if (match || preserveAllTokens && lastMatch) {
			String token = str.substring(start, i);

			if (trimTokens) {
				token = trim(token);
			}

			if (!ignoreEmptyTokens || token.length() > 0) {
				list.add(token);
			}
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Performs the logic for the {@code split} and {@code splitPreserveAllTokens} methods that return a maximum array
	 * length.
	 * 
	 * @param str the String to parse, may be {@code null}
	 * @param separatorChars the separate character
	 * @param max the maximum number of elements to include in the array. A zero or negative value implies no limit.
	 * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty token separators; if {@code
	 *            false}, adjacent separators are treated as one separator.
	 * @return an array of parsed Strings, {@code null} if null String input
	 */
	private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens, boolean trimTokens,
	        boolean ignoreEmptyTokens) {
		// Performance tuned for 2.0 (JDK1.4)
		// Direct code is quicker than StringTokenizer.
		// Also, StringTokenizer uses isSpace() not isWhitespace()

		if (str == null) {
			return Arrays.EMPTY_STRING_ARRAY;
		}

		if (null == separatorChars) {
			separatorChars = COMMA;
		}

		int len = str.length();
		if (len == 0) {
			return Arrays.EMPTY_STRING_ARRAY;
		}

		List<String> list = new ArrayList<String>();
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;

		//		if (separatorChars == null) {
		//			// Null separator means use whitespace
		//			while (i < len) {
		//				if (Character.isWhitespace(str.charAt(i))) {
		//					if (match || preserveAllTokens) {
		//						lastMatch = true;
		//						if (sizePlus1++ == max) {
		//							i = len;
		//							lastMatch = false;
		//						}
		//						
		//						String token = str.substring(start, i);
		//						
		//						if(trimTokens){
		//							token = trim(token);
		//						}
		//						
		//						if(!ignoreEmptyTokens || token.length() > 0){
		//							list.add(token);
		//						}
		//						
		//						match = false;
		//					}
		//					start = ++i;
		//					continue;
		//				}
		//				lastMatch = false;
		//				match = true;
		//				i++;
		//			}
		//		} else
		if (separatorChars.length() == 1) {
			// Optimise 1 character case
			char sep = separatorChars.charAt(0);
			while (i < len) {
				if (str.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						String token = str.substring(start, i);

						if (trimTokens) {
							token = trim(token);
						}

						if (!ignoreEmptyTokens || token.length() > 0) {
							list.add(token);
						}
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else {
			// standard case
			while (i < len) {
				if (separatorChars.indexOf(str.charAt(i)) >= 0) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						String token = str.substring(start, i);

						if (trimTokens) {
							token = trim(token);
						}

						if (!ignoreEmptyTokens || token.length() > 0) {
							list.add(token);
						}
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		if (match || preserveAllTokens && lastMatch) {
			String token = str.substring(start, i);

			if (trimTokens) {
				token = trim(str);
			}

			if (!ignoreEmptyTokens || token.length() > 0) {
				list.add(str.substring(start, i));
			}
		}
		return list.toArray(new String[list.size()]);
	}

	//CharSequence methods
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Returns a new {@code CharSequence} that is a subsequence of this sequence starting with the {@code char} value at
	 * the specified index.
	 * </p>
	 * 
	 * <p>
	 * This provides the {@code CharSequence} equivalent to {@link String#substring(int)}. The length (in {@code char})
	 * of the returned sequence is {@code length() - start}, so if {@code start == end} then an empty sequence is
	 * returned.
	 * </p>
	 * 
	 * @param cs the specified subsequence, null returns null
	 * @param start the start index, inclusive, valid
	 * @return a new subsequence, may be null
	 * @throws IndexOutOfBoundsException if {@code start} is negative or if {@code start} is greater than {@code
	 *             length()}
	 */
	static CharSequence subSequence(CharSequence cs, int start) {
		return cs == null ? null : cs.subSequence(start, cs.length());
	}

	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the first index in the {@code CharSequence} that matches the specified character.
	 * </p>
	 * 
	 * @param cs the {@code CharSequence} to be processed, not null
	 * @param searchChar the char to be searched for
	 * @param start the start index, negative starts at the string start
	 * @return the index where the search char was found, -1 if not found
	 */
	static int indexOfWorker(CharSequence cs, int searchChar, int start) {
		if (cs instanceof String) {
			return ((String) cs).indexOf(searchChar, start);
		} else {
			int sz = cs.length();
			if (start < 0) {
				start = 0;
			}
			for (int i = start; i < sz; i++) {
				if (cs.charAt(i) == searchChar) {
					return i;
				}
			}
			return -1;
		}
	}

	/**
	 * Used by the indexOf(CharSequence methods) as a green implementation of indexOf.
	 * 
	 * @param cs the {@code CharSequence} to be processed
	 * @param searchChar the {@code CharSequence} to be searched for
	 * @param start the start index
	 * @return the index where the search sequence was found
	 */
	static int indexOfWorker(CharSequence cs, CharSequence searchChar, int start) {
		return cs.toString().indexOf(searchChar.toString(), start);
		//        if (cs instanceof String && searchChar instanceof String) {
		//            // TODO: Do we assume searchChar is usually relatively small;
		//            //       If so then calling toString() on it is better than reverting to
		//            //       the green implementation in the else block
		//            return ((String) cs).indexOf((String) searchChar, start);
		//        } else {
		//            // TODO: Implement rather than convert to String
		//            return cs.toString().indexOf(searchChar.toString(), start);
		//        }
	}

	/**
	 * <p>
	 * Finds the last index in the {@code CharSequence} that matches the specified character.
	 * </p>
	 * 
	 * @param cs the {@code CharSequence} to be processed
	 * @param searchChar the char to be searched for
	 * @param start the start index, negative returns -1, beyond length starts at end
	 * @return the index where the search char was found, -1 if not found
	 */
	static int lastIndexOfWorker(CharSequence cs, int searchChar, int start) {
		if (cs instanceof String) {
			return ((String) cs).lastIndexOf(searchChar, start);
		} else {
			int sz = cs.length();
			if (start < 0) {
				return -1;
			}
			if (start >= sz) {
				start = sz - 1;
			}
			for (int i = start; i >= 0; --i) {
				if (cs.charAt(i) == searchChar) {
					return i;
				}
			}
			return -1;
		}
	}

	/**
	 * Used by the lastIndexOf(CharSequence methods) as a green implementation of lastIndexOf
	 * 
	 * @param cs the {@code CharSequence} to be processed
	 * @param searchChar the {@code CharSequence} to be searched for
	 * @param start the start index
	 * @return the index where the search sequence was found
	 */
	static int lastIndexOfWorker(CharSequence cs, CharSequence searchChar, int start) {
		return cs.toString().lastIndexOf(searchChar.toString(), start);
		//        if (cs instanceof String && searchChar instanceof String) {
		//            // TODO: Do we assume searchChar is usually relatively small;
		//            //       If so then calling toString() on it is better than reverting to
		//            //       the green implementation in the else block
		//            return ((String) cs).lastIndexOf((String) searchChar, start);
		//        } else {
		//            // TODO: Implement rather than convert to String
		//            return cs.toString().lastIndexOf(searchChar.toString(), start);
		//        }
	}

	/**
	 * Green implementation of toCharArray.
	 * 
	 * @param cs the {@code CharSequence} to be processed
	 * @return the resulting char array
	 */
	static char[] toCharArrayWorker(CharSequence cs) {
		if (cs instanceof String) {
			return ((String) cs).toCharArray();
		} else {
			int sz = cs.length();
			char[] array = new char[cs.length()];
			for (int i = 0; i < sz; i++) {
				array[i] = cs.charAt(i);
			}
			return array;
		}
	}

	/**
	 * Green implementation of regionMatches.
	 * 
	 * @param cs the {@code CharSequence} to be processed
	 * @param ignoreCase whether or not to be case insensitive
	 * @param thisStart the index to start on the {@code cs} CharSequence
	 * @param substring the {@code CharSequence} to be looked for
	 * @param start the index to start on the {@code substring} CharSequence
	 * @param length character length of the region
	 * @return whether the region matched
	 */
	static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length) {
		if (cs instanceof String && substring instanceof String) {
			return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
		} else {
			// TODO: Implement rather than convert to String
			return cs.toString().regionMatches(ignoreCase, thisStart, substring.toString(), start, length);
		}
	}
}