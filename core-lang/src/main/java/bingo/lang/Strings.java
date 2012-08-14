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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * <code>null</code> safe {@link String} utility
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
	
	private static final char[] DEFAULT_SPLIT_CHARS = new char[]{','};

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
	
	/**
	 * return the fisrt not empty string
	 */
	public static String firstNotEmpty(String... strings){
		for(String string : strings){
			if(!isEmpty(string)){
				return string;
			}
		}
		return EMPTY;
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
	 * If the trimChars String is {@code null}, whitespace is trimmed as defined by {@link Character#isWhitespace(char)}
	 * . Alternatively use {@link #trim(String)}.
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
		if (null == trimChars) {
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
	 * If the trimChars String is {@code null}, whitespace is trimmed as defined by {@link Character#isWhitespace(char)}
	 * .
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
	 * If the trimChars String is {@code null}, whitespace is trimmed as defined by {@link Character#isWhitespace(char)}
	 * .
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
	
	/**
	 * 传入一个对象，如果不是 {@link String} 的实例，则直接返回该对象。<br>
	 * 如果是 {@link String} 的实例，则删除字符串两端的空格。删除两端空格后的字符串如果为空字符串，
	 * 即<code> "" </code>，则返回null，否则返回删除两端空格后的字符串。
	 * @param object 传入的对象，一般为 {@link String} 。
	 * @return 若传入的对象为 {@link String} 的实例，则返回删除两端空格后的字符串；
	 * 若不是，则原样返回该对象。
	 */
	public static Object trimToNull(Object object) {
		return null == object ? null : object instanceof String ? trimToNull((String)object) : object;
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
	 * Strings.split(null)       = []
	 * Strings.split("")         = []
	 * Strings.split("abc,def")  = ["abc", "def"]
	 * Strings.split("abc def")  = ["abc def"]
	 * Strings.split(" abc ")    = ["abc"]
	 * </pre>
	 * 
	 * @param string the String to parse, may be null
	 * 
	 * @return an array of parsed Strings, empty array [] if null String input
	 */
	public static String[] split(String string) {
		return splitWorker(string, Chars.COMMA, false, true, true);
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
	 * Strings.split(null, *)         = []
	 * Strings.split("", *)           = []
	 * Strings.split("a.b.c", '.')    = ["a", "b", "c"]
	 * Strings.split("a..b.c", '.')   = ["a", "b", "c"]
	 * Strings.split("a:b:c", '.')    = ["a:b:c"]
	 * Strings.split("a b c", ' ')    = ["a", "b", "c"]
	 * </pre>
	 * 
	 * @param string the String to parse, may be null
	 * @param separator the character used as the delimiter
	 * @return an array of parsed Strings, empty array [] if null String input
	 */
	public static String[] split(String string, char separator) {
		return splitWorker(string, separator, false, true, true);
	}
	
	/**
	 * <pre>
	 * Strings.split(null, *)         = []
	 * Strings.split("", *)           = []
	 * Strings.split("a.b,c", '.','.')    = ["a", "b", "c"]
	 * </pre>
	 */
	public static String[] split(String string, char... separators) {
		return splitWorker(string,-1,false, true, true, separators);
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
	 * Strings.split(null, *)         = []
	 * Strings.split("", *)           = []
	 * Strings.split("abc,def", null) = ["abc", "def"]
	 * Strings.split("abc def", " ")  = ["abc", "def"]
	 * Strings.split("abc  def", " ") = ["abc", "def"]
	 * Strings.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
	 * </pre>
	 * 
	 * @param string the String to parse, may be null
	 * 
	 * @param separator the characters used as the delimiters, {@code null} splits on ","
	 * 
	 * @return an array of parsed Strings, [] if null String input
	 */
	public static String[] split(String string, String separator) {
		return splitByWholeSeparatorWorker(string, separator, -1, false, true, true);
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
		return splitWorker(string, separator, false, trim, true);
	}
	
	public static String[] split(String string, char[] separators, boolean trim) {
		return splitWorker(string, -1, false, trim, true, separators);
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
	 * @param separator the characters used as the delimiters, {@code null} splits on ","
	 * 
	 * @param trim if true then trim all the elements string in array
	 * 
	 * @return an array of parsed Strings, [] if null String input
	 */
	public static String[] split(String string, String separator, boolean trim) {
		return splitByWholeSeparatorWorker(string, separator, -1, false, trim, true);
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
	 * @param separator the characters used as the delimiters, {@code null} splits on ","
	 * 
	 * @param trim if true then trim all the elements string in array
	 * 
	 * @param ignoreEmpty ignore if token is empty("")
	 * 
	 * @return an array of parsed Strings, [] if null String input
	 */
	public static String[] split(String string, String separator, boolean trim,boolean ignoreEmpty) {
		return splitByWholeSeparatorWorker(string, separator, -1, !ignoreEmpty, trim, ignoreEmpty);
	}
	
	public static String[] split(String string, char[] separators, boolean trim,boolean ignoreEmpty) {
		return splitWorker(string, -1, !ignoreEmpty, trim, ignoreEmpty,separators);
	}	

	// Substring
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 * 
	 * <p>
	 * A {@code null} String will return "". An empty ("") String will return "".
	 * </p>
	 * 
	 * <pre>
	 * Strings.substring(null, *)   = ""
	 * Strings.substring("", *)     = ""
	 * Strings.substring("abc", 0)  = "abc"
	 * Strings.substring("abc", 2)  = "c"
	 * </pre>
	 * 
	 * @param string the String to get the substring from, may be null
	 * @param start the position to start from
	 * @return substring from start position, "" if null String input
	 * 
	 * @exception IndexOutOfBoundsException
	 */
	public static String substring(String string, int start) throws IndexOutOfBoundsException{
		if (string == null) {
			return EMPTY;
		}

		return string.substring(start);
	}
	
	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 * 
	 * <pre>
	 * Strings.substring(null, *, *)    = ""
	 * Strings.substring("", * ,  *)    = "";
	 * Strings.substring("abc", 0, 2)   = "ab"
	 * Strings.substring("abc", 2, 4)   = "c"
	 * Strings.substring("abc", 4, 6)   = ""
	 * Strings.substring("abc", 2, 2)   = ""
	 * </pre>
	 * 
	 * @param string the String to get the substring from, may be null
	 * @param start the position to start from
	 * @param end the position to end at (exclusive)
	 * @return substring from start position to end position,"" if null String input
	 * 
	 * @exception IndexOutOfBoundsException
	 */
	public static String substring(String string, int start, int end) throws IndexOutOfBoundsException {
		if (string == null) {
			return EMPTY;
		}
		
		return string.substring(start, end);
	}
	
	public static String substring(char[] chars, int start, int end) throws IndexOutOfBoundsException {
		if(null == chars){
			return EMPTY;
		}
		
        if (start < 0) {
            throw new IndexOutOfBoundsException("Negative start index : " + start);
        }
        
        if (end > chars.length) {
            throw new IndexOutOfBoundsException("end index : " + end + " > length : " + chars.length);
        }
        
        if (start > end) { 
            throw new IndexOutOfBoundsException("start index : " + start + " > end index : " + end);
        }
        
        return new String(chars, start, end - start);
	}	
	
	public static String substringTrimmed(char[] chars, int start, int end) throws IndexOutOfBoundsException {
		if(null == chars){
			return EMPTY;
		}
		
        if (start < 0) {
            throw new IndexOutOfBoundsException("Negative start index : " + start);
        }
        
        if (end > chars.length) {
            throw new IndexOutOfBoundsException("end index : " + end + " > length : " + chars.length);
        }
        
        if (start > end) { 
            throw new IndexOutOfBoundsException("start index : " + start + " > end index : " + end);
        }
        
        while (start < end && Character.isWhitespace(chars[start])) {
            start++;
        }
        
        while (end > start && Character.isWhitespace(chars[end - 1])) {
            end--;
        }        
        
        return new String(chars, start, end - start);
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
	 * Strings.left(null, *)    = ""
	 * Strings.left(*, -ve)     = ""
	 * Strings.left("", *)      = ""
	 * Strings.left("abc", 0)   = ""
	 * Strings.left("abc", 2)   = "ab"
	 * Strings.left("abc", 4)   = "abc"
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
	 * Strings.right(null, *)    = ""
	 * Strings.right(*, -ve)     = ""
	 * Strings.right("", *)      = ""
	 * Strings.right("abc", 0)   = ""
	 * Strings.right("abc", 2)   = "bc"
	 * Strings.right("abc", 4)   = "abc"
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
	 * Strings.replaceChars(null, *, *)        = ""
	 * Strings.replaceChars("", *, *)          = ""
	 * Strings.replaceChars("abcba", 'b', 'y') = "aycya"
	 * Strings.replaceChars("abcba", 'z', 'y') = "abcba"
	 * </pre>
	 * 
	 * @param text String to replace characters in, may be null
	 * @param oldChar the character to search for, may be null
	 * @param newChar the character to replace, may be null
	 * @return modified String, "" if null string input
	 */
	public static String replace(String text, char oldChar, char newChar) {
		if (text == null) {
			return EMPTY;
		}
		return text.replace(oldChar, newChar);
	}

	// Case conversion
	//-----------------------------------------------------------------------
	public static String upperFirst(String string){
		if(null == string){
			return EMPTY;
		}
		
		if(string.length() > 1){
			return Character.toUpperCase(string.charAt(0)) + string.substring(1);
		}else{
			return string;
		}
	}
	
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
	 * Strings.lowerCase(null)  = ""
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
	
	/**
	 * <pre>
	 * Strings.lowerCamel(null) 	      = ""
	 * Strings.lowerCamel("")   	      = ""
	 * Strings.lowerCamel("hello_world",'_') = helloWorld
	 * </pre>
	 */
	public static String lowerCamel(String string, char seperator) {
		if(null == string){
			return EMPTY;
		}
		
		String[]	  parts = split(string,seperator);
		StringBuilder out   = new StringBuilder(string.length());
		for (String part : parts) {
			if (out.length() == 0) {
				out.append(part.toLowerCase());
			}else{
				out.append(part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase());
			}
		}
		return out.toString();
	}
	
	public static String lowerCamel(String... parts){
		StringBuilder out   = new StringBuilder();
		for (String part : parts) {
			if(null == part || part.length() == 0){
				continue;
			}
			if (out.length() == 0) {
				out.append(part.toLowerCase());
			}else {
				out.append(part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase());
			}
		}
		return out.toString();
	}
	
	/**
	 * <pre>
	 * Strings.upperCamel(null) 	      = ""
	 * Strings.upperCamel("")   	      = ""
	 * Strings.upperCamel("hello_world",'_') = HelloWorld
	 * </pre>
	 */
	public static String upperCamel(String string, char seperator) {
		if(null == string){
			return EMPTY;
		}
		
		String[] parts 	  = split(string,seperator);
		StringBuilder out = new StringBuilder();
		for (String part : parts) {
			out.append(part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase());
		}
		return out.toString();
	}

	public static String upperCamel(String... parts) {
		StringBuilder out = new StringBuilder();
		for (String part : parts) {
			if(null == part || part.length() == 0){
				continue;
			}
			out.append(part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase());
		}
		return out.toString();
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
	 * Strings.indexOf(null, *)         = -1
	 * Strings.indexOf("", *)           = -1
	 * Strings.indexOf("aabaabaa", 'a') = 0
	 * Strings.indexOf("aabaabaa", 'b') = 2
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
	 * Strings.indexOf(null, *, *)          = -1
	 * Strings.indexOf("", *, *)            = -1
	 * Strings.indexOf("aabaabaa", 'b', 0)  = 2
	 * Strings.indexOf("aabaabaa", 'b', 3)  = 5
	 * Strings.indexOf("aabaabaa", 'b', 9)  = -1
	 * Strings.indexOf("aabaabaa", 'b', -1) = 2
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
	 * Strings.indexOf(null, *)          = -1
	 * Strings.indexOf(*, null)          = -1
	 * Strings.indexOf("", "")           = 0
	 * Strings.indexOf("", *)            = -1 (except when * = "")
	 * Strings.indexOf("aabaabaa", "a")  = 0
	 * Strings.indexOf("aabaabaa", "b")  = 2
	 * Strings.indexOf("aabaabaa", "ab") = 1
	 * Strings.indexOf("aabaabaa", "")   = 0
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
	 * Strings.indexOf(null, *, *)          = -1
	 * Strings.indexOf(*, null, *)          = -1
	 * Strings.indexOf("", "", 0)           = 0
	 * Strings.indexOf("", *, 0)            = -1 (except when * = "")
	 * Strings.indexOf("aabaabaa", "a", 0)  = 0
	 * Strings.indexOf("aabaabaa", "b", 0)  = 2
	 * Strings.indexOf("aabaabaa", "ab", 0) = 1
	 * Strings.indexOf("aabaabaa", "b", 3)  = 5
	 * Strings.indexOf("aabaabaa", "b", 9)  = -1
	 * Strings.indexOf("aabaabaa", "b", -1) = 2
	 * Strings.indexOf("aabaabaa", "", 2)   = 2
	 * Strings.indexOf("abc", "", 9)        = 3
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
	 * Strings.indexOfIgnoreCase(null, *)          = -1
	 * Strings.indexOfIgnoreCase(*, null)          = -1
	 * Strings.indexOfIgnoreCase("", "")           = 0
	 * Strings.indexOfIgnoreCase("aabaabaa", "a")  = 0
	 * Strings.indexOfIgnoreCase("aabaabaa", "b")  = 2
	 * Strings.indexOfIgnoreCase("aabaabaa", "ab") = 1
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
	 * Strings.indexOfIgnoreCase(null, *, *)          = -1
	 * Strings.indexOfIgnoreCase(*, null, *)          = -1
	 * Strings.indexOfIgnoreCase("", "", 0)           = 0
	 * Strings.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * Strings.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
	 * Strings.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
	 * Strings.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
	 * Strings.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
	 * Strings.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
	 * Strings.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
	 * Strings.indexOfIgnoreCase("abc", "", 9)        = 3
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
	 * Strings.indexOfAny(null, *)                = -1
	 * Strings.indexOfAny("", *)                  = -1
	 * Strings.indexOfAny(*, null)                = -1
	 * Strings.indexOfAny(*, [])                  = -1
	 * Strings.indexOfAny("zzabyycdxx",['z','a']) = 0
	 * Strings.indexOfAny("zzabyycdxx",['b','y']) = 3
	 * Strings.indexOfAny("aba", ['z'])           = -1
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
	 * Strings.indexOfAny(null, *)            = -1
	 * Strings.indexOfAny("", *)              = -1
	 * Strings.indexOfAny(*, null)            = -1
	 * Strings.indexOfAny(*, "")              = -1
	 * Strings.indexOfAny("zzabyycdxx", "za") = 0
	 * Strings.indexOfAny("zzabyycdxx", "by") = 3
	 * Strings.indexOfAny("aba","z")          = -1
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
	 * Strings.lastIndexOf(null, *)         = -1
	 * Strings.lastIndexOf("", *)           = -1
	 * Strings.lastIndexOf("aabaabaa", 'a') = 7
	 * Strings.lastIndexOf("aabaabaa", 'b') = 5
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
	 * Strings.lastIndexOf(null, *, *)          = -1
	 * Strings.lastIndexOf("", *,  *)           = -1
	 * Strings.lastIndexOf("aabaabaa", 'b', 8)  = 5
	 * Strings.lastIndexOf("aabaabaa", 'b', 4)  = 2
	 * Strings.lastIndexOf("aabaabaa", 'b', 0)  = -1
	 * Strings.lastIndexOf("aabaabaa", 'b', 9)  = 5
	 * Strings.lastIndexOf("aabaabaa", 'b', -1) = -1
	 * Strings.lastIndexOf("aabaabaa", 'a', 0)  = 0
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
	 * Strings.lastIndexOf(null, *)          = -1
	 * Strings.lastIndexOf(*, null)          = -1
	 * Strings.lastIndexOf("", "")           = 0
	 * Strings.lastIndexOf("aabaabaa", "a")  = 7
	 * Strings.lastIndexOf("aabaabaa", "b")  = 5
	 * Strings.lastIndexOf("aabaabaa", "ab") = 4
	 * Strings.lastIndexOf("aabaabaa", "")   = 8
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
	 * Strings.lastIndexOf(null, *, *)          = -1
	 * Strings.lastIndexOf(*, null, *)          = -1
	 * Strings.lastIndexOf("aabaabaa", "a", 8)  = 7
	 * Strings.lastIndexOf("aabaabaa", "b", 8)  = 5
	 * Strings.lastIndexOf("aabaabaa", "ab", 8) = 4
	 * Strings.lastIndexOf("aabaabaa", "b", 9)  = 5
	 * Strings.lastIndexOf("aabaabaa", "b", -1) = -1
	 * Strings.lastIndexOf("aabaabaa", "a", 0)  = 0
	 * Strings.lastIndexOf("aabaabaa", "b", 0)  = -1
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
	 * Strings.lastIndexOfIgnoreCase(null, *)          = -1
	 * Strings.lastIndexOfIgnoreCase(*, null)          = -1
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "A")  = 7
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "B")  = 5
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "AB") = 4
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
	 * Strings.lastIndexOfIgnoreCase(null, *, *)          = -1
	 * Strings.lastIndexOfIgnoreCase(*, null, *)          = -1
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "A", 8)  = 7
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "B", 8)  = 5
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "AB", 8) = 4
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "B", 9)  = 5
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "B", -1) = -1
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * Strings.lastIndexOfIgnoreCase("aabaabaa", "B", 0)  = -1
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
	 * Strings.removeBlank(null)         = ""
	 * Strings.removeBlank("")           = ""
	 * Strings.removeBlank("abc")        = "abc"
	 * Strings.removeBlank("   ab  c  ") = "abc"
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

	// Get Bytes
	//-----------------------------------------------------------------------
	/**
	 * Encodes the given string into a sequence of bytes using the named charset, storing the result into a new byte
	 * array.
	 * <p>
	 * This method catches {@link UnsupportedEncodingException} and rethrows it as {@link IllegalStateException}, which
	 * should never happen for a required charset name. Use this method when the encoding is required to be in the JRE.
	 * </p>
	 * 
	 * @param string the String to encode, may be <code>null</code>
	 * @param charsetName The name of a required {@link java.nio.charset.Charset}
	 * @return encoded bytes, or <code>[]</code> if the input string was <code>null</code>
	 * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught, which should never
	 *             happen for a required charset name.
	 * @see String#getBytes(String)
	 */
	public static byte[] getBytes(String str, String charset) {
		if (isEmpty(str)) {
			return Arrays.EMPTY_BYTE_ARRAY;
		}

		try {
			return str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
	
	/**
	 * Encodes the given string into a sequence of bytes using the ISO-8859-1 charset, storing the result into a new
	 * byte array.
	 * 
	 * @param string the String to encode, may be <code>null</code>
	 * @return encoded bytes, or <code>null</code> if the input string was <code>null</code>
	 * @throws IllegalStateException Thrown when the charset is missing, which should be never according the the Java
	 *             specification.
	 * @see <a href="http://download.oracle.com/javase/1.5.0/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 * @see #getBytesUnchecked(String, String)
	 */
	public static byte[] getBytesIso8859_1(String string) {
		return getBytes(string, Encoding.ISO_8859_1.name());
	}

	/**
	 * Encodes the given string into a sequence of bytes using the US-ASCII charset, storing the result into a new byte
	 * array.
	 * 
	 * @param string the String to encode, may be <code>null</code>
	 * @return encoded bytes, or <code>null</code> if the input string was <code>null</code>
	 * @throws IllegalStateException Thrown when the charset is missing, which should be never according the the Java
	 *             specification.
	 * @see <a href="http://download.oracle.com/javase/1.5.0/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 * @see #getBytesUnchecked(String, String)
	 */
	public static byte[] getBytesUsAscii(String string) {
		return getBytes(string, Encoding.US_ASCII.name());
	}

	/**
	 * Encodes the given string into a sequence of bytes using the UTF-8 charset, storing the result into a new byte
	 * array.
	 * 
	 * @param string the String to encode, may be <code>null</code>
	 * @return encoded bytes, or <code>[]</code> if the input string was <code>null</code>
	 * @throws IllegalStateException Thrown when the charset is missing, which should be never according the the Java
	 *             specification.
	 * @see <a href="http://download.oracle.com/javase/1.5.0/docs/api/java/nio/charset/Charset.html">Standard
	 *      charsets</a>
	 * @see #getBytes(String, String)
	 */
	public static byte[] getBytesUtf8(String string) {
		return getBytes(string, Encoding.UTF_8.name());
	}

	// New String
	//-----------------------------------------------------------------------

	/**
	 * Constructs a new <code>String</code> by decoding the specified array of bytes using the given charset.
	 * <p>
	 * This method catches {@link UnsupportedEncodingException} and re-throws it as {@link IllegalStateException}, which
	 * should never happen for a required charset name. Use this method when the encoding is required to be in the JRE.
	 * </p>
	 * 
	 * @param bytes The bytes to be decoded into characters, may be <code>null</code>
	 * @param charsetName The name of a required {@link java.nio.charset.Charset}
	 * @return A new <code>String</code> decoded from the specified array of bytes using the given charset, or
	 *         <code>""</code> if the input byte array was <code>null</code>.
	 * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught, which should never
	 *             happen for a required charset name.
	 * @see String#String(byte[], String)
	 */
	public static String newString(byte[] bytes, String charsetName) {
		if (bytes == null) {
			return Strings.EMPTY;
		}

		try {
			return new String(bytes, charsetName);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}	
	
	/**
	 * Constructs a new <code>String</code> by decoding the specified array of bytes using the ISO-8859-1 charset.
	 * 
	 * @param bytes The bytes to be decoded into characters, may be <code>null</code>
	 * @return A new <code>String</code> decoded from the specified array of bytes using the ISO-8859-1 charset, or
	 *         <code>""</code> if the input byte array was <code>null</code>.
	 * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught, which should never
	 *             happen since the charset is required.
	 */
	public static String newStringIso8859_1(byte[] bytes) {
		return newString(bytes, Encoding.ISO_8859_1.name());
	}

	/**
	 * Constructs a new <code>String</code> by decoding the specified array of bytes using the US-ASCII charset.
	 * 
	 * @param bytes The bytes to be decoded into characters
	 * @return A new <code>String</code> decoded from the specified array of bytes using the US-ASCII charset, or
	 *         <code>""</code> if the input byte array was <code>null</code>.
	 * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught, which should never
	 *             happen since the charset is required.
	 */
	public static String newStringUsAscii(byte[] bytes) {
		return newString(bytes, Encoding.US_ASCII.name());
	}	
	
	/**
	 * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-8 charset.
	 * 
	 * @param bytes The bytes to be decoded into characters
	 * @return A new <code>String</code> decoded from the specified array of bytes using the UTF-8 charset, or
	 *         <code>""</code> if the input byte array was <code>null</code>.
	 * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught, which should never
	 *             happen since the charset is required.
	 */
	public static String newStringUtf8(byte[] bytes) {
		return newString(bytes, Encoding.UTF_8.name());
	}
	
	//isNumber
	//----------------------------------------------------------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if the CharSequence contains only Unicode digits. 
	 * A decimal point is not a Unicode digit and returns false.
	 * </p>
	 * 
	 * <p>
	 * {@code null} will return {@code false}. An empty CharSequence (length()=0) will return {@code false}.
	 * </p>
	 * 
	 * <pre>
	 * Strings.isDigits(null)   = false
	 * Strings.isDigits("")     = false
	 * Strings.isDigits("  ")   = false
	 * Strings.isDigits("123")  = true
	 * Strings.isDigits("12 3") = false
	 * Strings.isDigits("ab2c") = false
	 * Strings.isDigits("12-3") = false
	 * Strings.isDigits("12.3") = false
	 * </pre>
	 * 
	 * @param cs the CharSequence to check, may be null
	 * 
	 * @return {@code true} if only contains digits, and is non-null
	 */
	public static boolean isDigits(CharSequence cs) {
		if (cs == null || cs.length() == 0) {
			return false;
		}
		int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * <p>
	 * Checks whether the String a valid Java number.
	 * </p>
	 * 
	 * <p>
	 * Valid numbers include hexadecimal marked with the <code>0x</code> qualifier, scientific notation and numbers marked with a
	 * type qualifier (e.g. 123L).
	 * </p>
	 * 
	 * <p>
	 * <code>Null</code> and empty String will return <code>false</code>.
	 * </p>
	 * 
	 * @param str the <code>String</code> to check
	 * @return <code>true</code> if the string is a correctly formatted number
	 */
    public static boolean isNumber(String str) {
        if (isEmpty(str)) {
            return false;
        }
        char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        int start = (chars[0] == '-') ? 1 : 0;
        if (sz > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
            int i = start + 2;
            if (i == sz) {
                return false; // str == "0x"
            }
            // checking hex (it can't be anything else)
            for (; i < chars.length; i++) {
                if ((chars[i] < '0' || chars[i] > '9')
                    && (chars[i] < 'a' || chars[i] > 'f')
                    && (chars[i] < 'A' || chars[i] > 'F')) {
                    return false;
                }
            }
            return true;
        }
        sz--; // don't want to loop to the last char, check it afterwords
              // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent   
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                // single trailing decimal point after non-exponent is ok
                return foundDigit;
            }
            if (!allowSigns
                && (chars[i] == 'd'
                    || chars[i] == 'D'
                    || chars[i] == 'f'
                    || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l'
                || chars[i] == 'L') {
                // not allowing L with an exponent or decimal point
                return foundDigit && !hasExp && !hasDecPoint;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return !allowSigns && foundDigit;
    }	
    
    // Count matches
    //-----------------------------------------------------------------------
    /**
     * <p>Counts how many times the substring appears in the larger string.</p>
     *
     * <p>A {@code null} or empty ("") String input returns {@code 0}.</p>
     *
     * <pre>
     * Strings.countOccurrences(null, *)       = 0
     * Strings.countOccurrences("", *)         = 0
     * Strings.countOccurrences("abba", null)  = 0
     * Strings.countOccurrences("abba", "")    = 0
     * Strings.countOccurrences("abba", "a")   = 2
     * Strings.countOccurrences("abba", "ab")  = 1
     * Strings.countOccurrences("abba", "xxx") = 0
     * </pre>
     *
     * @param str  the CharSequence to check, may be null
     * @param sub  the substring to count, may be null
     * @return the number of occurrences, 0 if either CharSequence is {@code null}
     * @since 3.0 Changed signature from countMatches(String, String) to countMatches(CharSequence, CharSequence)
     */
    public static int countOccurrences(CharSequence str, CharSequence sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = indexOf(str, sub, idx)) != INDEX_NOT_FOUND) {
            count++;
            idx += sub.length();
        }
        return count;
    }    
    
    // Abbreviating
    //-----------------------------------------------------------------------
    /**
     * <p>Abbreviates a String using ellipses. This will turn
     * "Now is the time for all good men" into "Now is the time for..."</p>
     *
     * <p>Specifically:
     * <ul>
     *   <li>If {@code str} is less than {@code maxWidth} characters
     *       long, return it.</li>
     *   <li>Else abbreviate it to {@code (substring(str, 0, max-3) + "...")}.</li>
     *   <li>If {@code maxWidth} is less than {@code 4}, throw an
     *       {@code IllegalArgumentException}.</li>
     *   <li>In no case will it return a String of length greater than
     *       {@code maxWidth}.</li>
     * </ul>
     * </p>
     *
     * <pre>
     * Strings.abbreviate(null, *)      = null
     * Strings.abbreviate("", 4)        = ""
     * Strings.abbreviate("abcdefg", 6) = "abc..."
     * Strings.abbreviate("abcdefg", 7) = "abcdefg"
     * Strings.abbreviate("abcdefg", 8) = "abcdefg"
     * Strings.abbreviate("abcdefg", 4) = "a..."
     * Strings.abbreviate("abcdefg", 3) = IllegalArgumentException
     * </pre>
     *
     * @param str  the String to check, may be null
     * @param maxWidth  maximum length of result String, must be at least 4
     * @return abbreviated String, {@code null} if null String input
     * @throws IllegalArgumentException if the width is too small
     */
    public static String abbreviate(String str, int maxWidth) {
        return abbreviate(str, 0, maxWidth);
    }
    
    /**
     * <pre>
     * Strings.abbreviateMiddle(null,  0)      = ""
     * Strings.abbreviateMiddle("abc", 0)      = "abc"
     * Strings.abbreviateMiddle("abc", 0)      = "abc"
     * Strings.abbreviateMiddle("abc", ".", 3) = "abc"
     * Strings.abbreviateMiddle("abcdefghij", "...", 6)  = "ab...g"
     * </pre>
     */
    public static String abbreviateMiddle(String str, int maxWidth) {
        return abbreviateMiddle(str, "...", maxWidth);
    }

    /**
     * <p>Abbreviates a String using ellipses. This will turn
     * "Now is the time for all good men" into "...is the time for..."</p>
     *
     * <p>Works like {@code abbreviate(String, int)}, but allows you to specify
     * a "left edge" offset.  Note that this left edge is not necessarily going to
     * be the leftmost character in the result, or the first character following the
     * ellipses, but it will appear somewhere in the result.
     *
     * <p>In no case will it return a String of length greater than
     * {@code maxWidth}.</p>
     *
     * <pre>
     * Strings.abbreviate(null, *, *)                = null
     * Strings.abbreviate("", 0, 4)                  = ""
     * Strings.abbreviate("abcdefghijklmno", -1, 10) = "abcdefg..."
     * Strings.abbreviate("abcdefghijklmno", 0, 10)  = "abcdefg..."
     * Strings.abbreviate("abcdefghijklmno", 1, 10)  = "abcdefg..."
     * Strings.abbreviate("abcdefghijklmno", 4, 10)  = "abcdefg..."
     * Strings.abbreviate("abcdefghijklmno", 5, 10)  = "...fghi..."
     * Strings.abbreviate("abcdefghijklmno", 6, 10)  = "...ghij..."
     * Strings.abbreviate("abcdefghijklmno", 8, 10)  = "...ijklmno"
     * Strings.abbreviate("abcdefghijklmno", 10, 10) = "...ijklmno"
     * Strings.abbreviate("abcdefghijklmno", 12, 10) = "...ijklmno"
     * Strings.abbreviate("abcdefghij", 0, 3)        = IllegalArgumentException
     * Strings.abbreviate("abcdefghij", 5, 6)        = IllegalArgumentException
     * </pre>
     *
     * @param str  the String to check, may be null
     * @param offset  left edge of source String
     * @param maxWidth  maximum length of result String, must be at least 4
     * @return abbreviated String, {@code null} if null String input
     * @throws IllegalArgumentException if the width is too small
     */
    static String abbreviate(String str, int offset, int maxWidth) {
        if (str == null) {
            return null;
        }
        if (maxWidth < 4) {
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        }
        if (str.length() <= maxWidth) {
            return str;
        }
        if (offset > str.length()) {
            offset = str.length();
        }
        if (str.length() - offset < maxWidth - 3) {
            offset = str.length() - (maxWidth - 3);
        }
        final String abrevMarker = "...";
        if (offset <= 4) {
            return str.substring(0, maxWidth - 3) + abrevMarker;
        }
        if (maxWidth < 7) {
            throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
        }
        if (offset + maxWidth - 3 < str.length()) {
            return abrevMarker + abbreviate(str.substring(offset), maxWidth - 3);
        }
        return abrevMarker + str.substring(str.length() - (maxWidth - 3));
    }

    /**
     * <p>Abbreviates a String to the length passed, replacing the middle characters with the supplied
     * replacement String.</p>
     *
     * <p>This abbreviation only occurs if the following criteria is met:
     * <ul>
     * <li>Neither the String for abbreviation nor the replacement String are null or empty </li>
     * <li>The length to truncate to is less than the length of the supplied String</li>
     * <li>The length to truncate to is greater than 0</li>
     * <li>The abbreviated String will have enough room for the length supplied replacement String
     * and the first and last characters of the supplied String for abbreviation</li>
     * </ul>
     * Otherwise, the returned String will be the same as the supplied String for abbreviation.
     * </p>
     *
     * <pre>
     * Strings.abbreviateMiddle(null, null, 0)      = null
     * Strings.abbreviateMiddle("abc", null, 0)      = "abc"
     * Strings.abbreviateMiddle("abc", ".", 0)      = "abc"
     * Strings.abbreviateMiddle("abc", ".", 3)      = "abc"
     * Strings.abbreviateMiddle("abcdef", ".", 4)     = "ab.f"
     * </pre>
     *
     * @param str  the String to abbreviate, may be null
     * @param middle the String to replace the middle characters with, may be null
     * @param length the length to abbreviate {@code str} to.
     * @return the abbreviated String if the above criteria is met, or the original String supplied for abbreviation.
     */
    static String abbreviateMiddle(String str, String middle, int length) {
        if (isEmpty(str) || isEmpty(middle)) {
            return str;
        }

        if (length >= str.length() || length < middle.length()+2) {
            return str;
        }

        int targetSting = length-middle.length();
        int startOffset = targetSting/2+targetSting%2;
        int endOffset = str.length()-targetSting/2;

        StringBuilder builder = new StringBuilder(length);
        builder.append(str.substring(0,startOffset));
        builder.append(middle);
        builder.append(str.substring(endOffset));

        return builder.toString();
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
     * Performs the logic for the {@code splitByWholeSeparatorPreserveAllTokens} methods.
     *
     * @param str  the String to parse, may be {@code null}
     * @param separator  String containing the String to be used as a delimiter,
     *  {@code null} splits on whitespace
     * @param max  the maximum number of elements to include in the returned
     *  array. A zero or negative value implies no limit.
     * @param preserveAllTokens if {@code true}, adjacent separators are
     * treated as empty token separators; if {@code false}, adjacent
     * separators are treated as one separator.
     * @return an array of parsed Strings, {@code null} if null String input
     */
    private static String[] splitByWholeSeparatorWorker(String str, String separator, int max, boolean preserveAllTokens,
    													 boolean trimTokens,boolean ignoreEmptyTokens) {
        if (str == null) {
            return null;
        }

        int len = str.length();

        if (len == 0) {
            return Arrays.EMPTY_STRING_ARRAY;
        }

        if (separator == null || EMPTY.equals(separator)) {
            // Split on whitespace.
            return splitWorker(str, max, preserveAllTokens,trimTokens,ignoreEmptyTokens);
        }

        int separatorLength = separator.length();

        ArrayList<String> substrings = new ArrayList<String>();
        int numberOfSubstrings = 0;
        int beg = 0;
        int end = 0;
        
        while (end < len) {
            end = str.indexOf(separator, beg);

            String token = null;
            
            if (end > -1) {
                if (end > beg) {
                    numberOfSubstrings += 1;

                    if (numberOfSubstrings == max) {
                        end = len;
                        
                        token = str.substring(beg);
                    } else {
                        // The following is OK, because String.substring( beg, end ) excludes
                        // the character at the position 'end'.
                        token = str.substring(beg, end);

                        // Set the starting point for the next search.
                        // The following is equivalent to beg = end + (separatorLength - 1) + 1,
                        // which is the right calculation:
                        beg = end + separatorLength;
                    }
                } else {
                    // We found a consecutive occurrence of the separator, so skip it.
                    if (preserveAllTokens) {
                        numberOfSubstrings += 1;
                        if (numberOfSubstrings == max) {
                            end = len;
                            token = str.substring(beg);
                        } else{
                            token = EMPTY;
                        }
                    }
                    beg = end + separatorLength;
                }
            } else {
                // String.substring( beg ) goes from 'beg' to the end of the String.
                token = str.substring(beg);
                end = len;
            }
            
            if(null != token){
            	if(trimTokens){
            		token = trim(token);
            	}
            	
				if (!ignoreEmptyTokens || token.length() > 0) {
					substrings.add(token);
				}
            }
        }

        return substrings.toArray(new String[substrings.size()]);
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
	private static String[] splitWorker(String str,int max, boolean preserveAllTokens,boolean trimTokens,boolean ignoreEmptyTokens,char... chars) {
		// Performance tuned for 2.0 (JDK1.4)
		// Direct code is quicker than StringTokenizer.
		// Also, StringTokenizer uses isSpace() not isWhitespace()

		if (str == null) {
			return Arrays.EMPTY_STRING_ARRAY;
		}

		if (null == chars || chars.length == 0) {
			chars = DEFAULT_SPLIT_CHARS;
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
		if (chars.length == 1) {
			// Optimise 1 character case
			char sep = chars[0];
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
				if (Arrays.indexOf(chars, str.charAt(i)) >= 0) {
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
				token = trim(token);
			}

			if (!ignoreEmptyTokens || token.length() > 0) {
				list.add(token);
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
