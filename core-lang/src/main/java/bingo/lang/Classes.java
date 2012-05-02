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

import java.util.HashMap;
import java.util.Map;

//from apache commons-lang3

/**
 * <code>null</code> safe {@link Class} utility.
 */
public class Classes {
	
	/**
	 * Maps a primitive class name to its corresponding abbreviation used in array class names.
	 */
	private static final Map<String, String>	abbreviationMap	= new HashMap<String, String>();

	/**
	 * Maps an abbreviation used in array class names to corresponding primitive class name.
	 */
	private static final Map<String, String>	reverseAbbreviationMap	= new HashMap<String, String>();

	/**
	 * <p>
	 * The package separator character: <code>'&#x2e;' == {@value}</code>.
	 * </p>
	 */
	public static final char PACKAGE_SEPARATOR_CHAR = '.';

	/**
	 * <p>
	 * The package separator String: {@code "&#x2e;"}.
	 * </p>
	 */
	public static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);

	/**
	 * <p>
	 * The inner class separator character: <code>'$' == {@value}</code>.
	 * </p>
	 */
	public static final char	INNER_CLASS_SEPARATOR_CHAR	= '$';

	/**
	 * <p>
	 * The inner class separator String: {@code "$"}.
	 * </p>
	 */
	public static final String INNER_CLASS_SEPARATOR = String.valueOf(INNER_CLASS_SEPARATOR_CHAR);

	/**
	 * Add primitive type abbreviation to maps of abbreviations.
	 * 
	 * @param primitive Canonical name of primitive type
	 * @param abbreviation Corresponding abbreviation of primitive type
	 */
	private static void addAbbreviation(String primitive, String abbreviation) {
		abbreviationMap.put(primitive, abbreviation);
		reverseAbbreviationMap.put(abbreviation, primitive);
	}

	/**
	 * Feed abbreviation maps
	 */
	static {
		addAbbreviation("int", "I");
		addAbbreviation("boolean", "Z");
		addAbbreviation("float", "F");
		addAbbreviation("long", "J");
		addAbbreviation("short", "S");
		addAbbreviation("byte", "B");
		addAbbreviation("double", "D");
		addAbbreviation("char", "C");
	}

	protected Classes() {

	}

	// Short class name
	// ----------------------------------------------------------------------	
	/**
	 * <p>
	 * Gets the class name minus the package name from a {@code Class}.
	 * </p>
	 * 
	 * <p>
	 * Consider using the Java 5 API {@link Class#getSimpleName()} instead. The one known difference is that this code
	 * will return {@code "Map.Entry"} while the {@code java.lang.Class} variant will simply return {@code "Entry"}.
	 * </p>
	 * 
	 * @param cls the class to get the short name for.
	 * 
	 * @return the class name without the package name or an empty string
	 */
	public static String getShortName(Class<?> cls) {
		if (cls == null) {
			return Strings.EMPTY;
		}
		return getShortName(cls.getName());
	}

	/**
	 * <p>
	 * Gets the class name minus the package name from a String.
	 * </p>
	 * 
	 * <p>
	 * The string passed in is assumed to be a class name - it is not checked.
	 * </p>
	 * 
	 * <p>
	 * Note that this method differs from Class.getSimpleName() in that this will return {@code "Map.Entry"} whilst the
	 * {@code java.lang.Class} variant will simply return {@code "Entry"}.
	 * </p>
	 * 
	 * @param className the className to get the short name for
	 * @return the class name of the class without the package name or an empty string
	 */
	public static String getShortName(String className) {
		if (className == null) {
			return Strings.EMPTY;
		}
		if (className.length() == 0) {
			return Strings.EMPTY;
		}

		StringBuilder arrayPrefix = new StringBuilder();

		// Handle array encoding
		if (className.startsWith("[")) {
			while (className.charAt(0) == '[') {
				className = className.substring(1);
				arrayPrefix.append("[]");
			}
			// Strip Object type encoding
			if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
				className = className.substring(1, className.length() - 1);
			}
		}

		if (reverseAbbreviationMap.containsKey(className)) {
			className = reverseAbbreviationMap.get(className);
		}

		int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
		int innerIdx = className.indexOf(INNER_CLASS_SEPARATOR_CHAR, lastDotIdx == -1 ? 0 : lastDotIdx + 1);
		String out = className.substring(lastDotIdx + 1);
		if (innerIdx != -1) {
			out = out.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
		}
		return out + arrayPrefix;
	}

	// Class loading
	// ----------------------------------------------------------------------
	/**
	 * Returns the (initialized) class represented by {@code className} using the current thread's context class loader.
	 * This implementation supports the syntaxes "{@code java.util.Map.Entry[]}", "{@code java.util.Map$Entry[]}", "
	 * {@code [Ljava.util.Map.Entry;}", and "{@code [Ljava.util.Map$Entry;}".
	 * 
	 * @param className the class name
	 * @return the class represented by {@code className} using the current thread's context class loader
	 * 
	 * @throws ClassNotFoundException if the class is not found
	 */
	public static Class<?> forName(String className) throws ClassNotFoundException {
		return forName(className, true);
	}
	
	public static Class<?> forNameOrNull(String className) {
		try {
	        return forName(className, true);
        } catch (ClassNotFoundException e) {
        	return null;
        }
	}
	
	/**
	 * Returns the class represented by {@code className} using the current thread's context class loader. This
	 * implementation supports the syntaxes "{@code java.util.Map.Entry[]}", "{@code java.util.Map$Entry[]}", "{@code
	 * [Ljava.util.Map.Entry;}", and "{@code [Ljava.util.Map$Entry;}".
	 * 
	 * @param className the class name
	 * @param initialize whether the class must be initialized
	 * @return the class represented by {@code className} using the current thread's context class loader
	 * @throws ClassNotFoundException if the class is not found
	 */
	static Class<?> forName(String className, boolean initialize) throws ClassNotFoundException {
		ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
		ClassLoader loader = contextCL == null ? Classes.class.getClassLoader() : contextCL;
		return forName(loader, className, initialize);
	}	
	
	/**
	 * Returns the (initialized) class represented by {@code className} using the {@code classLoader}. This
	 * implementation supports the syntaxes "{@code java.util.Map.Entry[]}", "{@code java.util.Map$Entry[]}", "{@code
	 * [Ljava.util.Map.Entry;}", and "{@code [Ljava.util.Map$Entry;}".
	 * 
	 * @param classLoader the class loader to use to load the class
	 * @param className the class name
	 * @return the class represented by {@code className} using the {@code classLoader}
	 * @throws ClassNotFoundException if the class is not found
	 */
	public static Class<?> forName(ClassLoader classLoader, String className) throws ClassNotFoundException {
		return forName(classLoader, className, true);
	}
	
	public static Class<?> forNameOrNull(ClassLoader classLoader, String className) {
		try {
	        return forName(classLoader, className, true);
        } catch (ClassNotFoundException e) {
        	return null;
        }
	}	
	
	/**
	 * Returns the class represented by {@code className} using the {@code classLoader}. This implementation supports
	 * the syntaxes "{@code java.util.Map.Entry[]}", "{@code java.util.Map$Entry[]}", "{@code [Ljava.util.Map.Entry;}
	 * ", and "{@code [Ljava.util.Map$Entry;}".
	 * 
	 * @param classLoader the class loader to use to load the class
	 * @param className the class name
	 * @param initialize whether the class must be initialized
	 * @return the class represented by {@code className} using the {@code classLoader}
	 * @throws ClassNotFoundException if the class is not found
	 */
	static Class<?> forName(ClassLoader classLoader, String className, boolean initialize) throws ClassNotFoundException {
		try {
			Class<?> clazz;
			if (abbreviationMap.containsKey(className)) {
				String clsName = "[" + abbreviationMap.get(className);
				clazz = Class.forName(clsName, initialize, classLoader).getComponentType();
			} else {
				clazz = Class.forName(toCanonicalName(className), initialize, classLoader);
			}
			return clazz;
		} catch (ClassNotFoundException ex) {
			// allow path separators (.) as inner class name separators
			int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);

			if (lastDotIndex != -1) {
				try {
					return forName(classLoader, className.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR_CHAR
					        + className.substring(lastDotIndex + 1), initialize);
				} catch (ClassNotFoundException ex2) { // NOPMD
					// ignore exception
				}
			}

			throw ex;
		}
	}

	// Private Methods
	// ----------------------------------------------------------------------
	/**
	 * Converts a class name to a JLS style class name.
	 * 
	 * @param className the class name
	 * @return the converted name
	 */
	private static String toCanonicalName(String className) {
		className = Strings.trim(className);

		if (className.endsWith("[]")) {
			StringBuilder classNameBuffer = new StringBuilder();
			while (className.endsWith("[]")) {
				className = className.substring(0, className.length() - 2);
				classNameBuffer.append("[");
			}
			String abbreviation = abbreviationMap.get(className);
			if (abbreviation != null) {
				classNameBuffer.append(abbreviation);
			} else {
				classNameBuffer.append("L").append(className).append(";");
			}
			className = classNameBuffer.toString();
		}
		return className;
	}
}
