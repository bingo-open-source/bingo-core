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

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import bingo.lang.exceptions.NotFoundException;
import bingo.lang.exceptions.ReflectException;
import bingo.lang.exceptions.UncheckedIOException;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.lang.resource.Resource;
import bingo.lang.resource.Resources;

//from apache commons-lang3

/**
 * <code>null</code> safe {@link Class} utility.
 */
public class Classes {
	
	private static final Log log = LogFactory.get(Classes.class);
	
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
	
	/** The ".class" file suffix */
	public static final String CLASS_FILE_SUFFIX = ".class";	

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
	//new instance
	//----------------------------------------------------------------------
	public static Object newInstance(String className) throws NotFoundException,ReflectException {
		return Reflects.newInstance(forName(className));
	}
	
	public static Object newInstance(Class<?> loaderClass, String className) throws NotFoundException,ReflectException {
		return Reflects.newInstance(forName(loaderClass,className));
	}

	public static Object newInstance(Class<?> clazz) throws ReflectException {
		return Reflects.newInstance(clazz);
	}
	
	//Class scan
	//-----------------------------------------------------------------------
	public static Set<Class<?>> scan(String basePackage,String classpathPattern) throws UncheckedIOException {
		Assert.notEmpty(basePackage,	 "basePackage must not be empty");
		Assert.notEmpty(classpathPattern,"classLocationPattern must not be empty");
		
		String basePath = basePackage.replace('.', '/');
		String scanPath = Resources.CLASSPATH_ALL_URL_PREFIX + basePath + "/" + classpathPattern + ".class";
		
		StopWatch sw = StopWatch.startNew();
		
		Resource[] resources = Resources.scan(scanPath);
		
		Set<Class<?>> classes = new HashSet<Class<?>>();
		
		try {
	        for(Resource resource : resources) {
	        	if(resource.isReadable()){
	        		classes.add(classForResource(resource,basePath));
	        	}
	        }
        } catch (IOException e) {
        	Exceptions.uncheck(e,"Error scanning package '{0}'",basePackage);
        }
        
        sw.stop();
        
        log.debug("scan {0} classes in package '{1}' used {2}ms",classes.size(),basePackage,sw.getElapsedMilliseconds());
		
		return classes;
	}
	
	//Class Loader
	//-----------------------------------------------------------------------
	/**
	 * Return the default ClassLoader to use: typically the thread context ClassLoader, if available; the ClassLoader
	 * that loaded the Classes class will be used as fallback.
	 * <p>
	 * Call this method if you intend to use the thread context ClassLoader in a scenario where you absolutely need a
	 * non-null ClassLoader reference: for example, for class path resource loading (but not necessarily for
	 * <code>Class.forName</code>, which accepts a <code>null</code> ClassLoader reference as well).
	 * 
	 * @return the default ClassLoader (never <code>null</code>)
	 * @see java.lang.Thread#getContextClassLoader()
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = Classes.class.getClassLoader();
		}
		return cl;
	}
	
	public static ClassLoader getClassLoader(Class<?> clazz) {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = clazz.getClassLoader();
		}
		return cl;
	}

	// class & package name 
	// ----------------------------------------------------------------------	
	/**
	 * Determine the name of the class file, relative to the containing
	 * package: e.g. "String.class"
	 * @param clazz the class
	 * @return the file name of the ".class" file
	 */
	public static String getFileName(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		String className = clazz.getName();
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
		return className.substring(lastDotIndex + 1) + CLASS_FILE_SUFFIX;
	}
	
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
	
	/**
	 * Given an input class object, return a string which consists of the
	 * class's package name as a pathname, i.e., all dots ('.') are replaced by
	 * slashes ('/'). Neither a leading nor trailing slash is added. The result
	 * could be concatenated with a slash and the name of a resource and fed
	 * directly to <code>ClassLoader.getResource()</code>. For it to be fed to
	 * <code>Class.getResource</code> instead, a leading slash would also have
	 * to be prepended to the returned value.
	 * @param clazz the input class. A <code>null</code> value or the default
	 * (empty) package will result in an empty string ("") being returned.
	 * @return a path which represents the package name
	 * @see ClassLoader#getResource
	 * @see Class#getResource
	 */
	public static String getPackageAsResourcePath(Class<?> clazz) {
		if (clazz == null) {
			return "";
		}
		String className = clazz.getName();
		int packageEndIndex = className.lastIndexOf('.');
		if (packageEndIndex == -1) {
			return "";
		}
		String packageName = className.substring(0, packageEndIndex);
		return packageName.replace('.', '/');
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
	 * @throws NotFoundException if the class is not found
	 */
	public static Class<?> forName(String className) throws NotFoundException {
		return forName(className, true);
	}
	
	public static Class<?> forNameOrNull(String className) {
		try {
	        return forName(className, true);
        } catch (NotFoundException e) {
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
	 * @throws NotFoundException if the class is not found
	 */
	static Class<?> forName(String className, boolean initialize) throws NotFoundException {
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
	 * @throws NotFoundException if the class is not found
	 */
	public static Class<?> forName(ClassLoader classLoader, String className) throws NotFoundException {
		return forName(classLoader, className, true);
	}
	
	public static Class<?> forName(Class<?> loaderClass,String className) throws NotFoundException {
		return forName(getClassLoader(loaderClass),className);
	}
	
	public static Class<?> forNameOrNull(ClassLoader classLoader, String className) {
		try {
	        return forName(classLoader, className, true);
        } catch (NotFoundException e) {
        	return null;
        }
	}	
	
    // Inner class
    // ----------------------------------------------------------------------
    /**
     * <p>Is the specified class an inner class or static nested class.</p>
     *
     * @param cls  the class to check, may be null
     * @return {@code true} if the class is an inner or static nested class,
     *  false if not or {@code null}
     */
    public static boolean isInner(Class<?> cls) {
        return cls != null && cls.getEnclosingClass() != null;
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
	static Class<?> forName(ClassLoader classLoader, String className, boolean initialize) throws NotFoundException {
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
				} catch (NotFoundException ex2) { // NOPMD
					// ignore exception
				}
			}

			throw new NotFoundException("class '{0}' not found",className,ex);
		}
	}
	
	/**
	 * Checks if given class is a concrete one; that is, not an interface or abstract class.
	 */
	public static boolean isConcrete(Class<?> clazz) {
		if(null == clazz){
			return false;
		}
		
		return ! (Modifier.isInterface(clazz.getModifiers()) || Modifier.isAbstract(clazz.getModifiers())); 
		
	}
	
	/**
	 * Checks if given class is a simple type.
	 */
	public static boolean isSimple(Class<?> clazz) {
		return null != clazz &&
				    (clazz.isPrimitive() || 
					Primitives.isWrapperType(clazz) || 
					clazz.isEnum() ||
					Number.class.isAssignableFrom(clazz) ||
					CharSequence.class.isAssignableFrom(clazz) ||
					Date.class.isAssignableFrom(clazz) ||
					Class.class.isAssignableFrom(clazz));
	}
	
	public static boolean isEnumerable(Class<?> clazz) {
		return null != clazz && 
				(clazz.isArray() || 
			     Iterable.class.isAssignableFrom(clazz) ||
			     Iterator.class.isAssignableFrom(clazz) ||
			     Enumeration.class.isAssignableFrom(clazz) 
			     );
	}
	
	public static boolean isJavaPackage(Class<?> clazz){
		return null != clazz && clazz.getPackage().getName().startsWith("java.");
	}
	
	public static boolean isJavaxPackage(Class<?> clazz){
		return null != clazz && clazz.getPackage().getName().startsWith("javax.");
	}
	
	public static boolean isString(Class<?> clazz){
		return null == clazz ? false : clazz.equals(String.class);
	}
	
	public static boolean isBoolean(Class<?> clazz){
		return null == clazz ? false : clazz.equals(Boolean.TYPE) || clazz.equals(Boolean.class);
	}
	
	public static boolean isDouble(Class<?> clazz){
		return null == clazz ? false : clazz.equals(Double.TYPE) || clazz.equals(Double.class);
	}
	
	public static boolean isInteger(Class<?> clazz){
		return null == clazz ? false : clazz.equals(Integer.TYPE) || clazz.equals(Integer.class);
	}
	
	public static boolean isLong(Class<?> clazz){
		return null == clazz ? false : clazz.equals(Long.TYPE) || clazz.equals(Long.class);
	}
	
	public static boolean isShort(Class<?> clazz){
		return null == clazz ? false : clazz.equals(Short.TYPE) || clazz.equals(Short.class);
	}
	
	public static boolean isFloat(Class<?> clazz){
		return null == clazz ? false : clazz.equals(Float.TYPE) || clazz.equals(Float.class);
	}
	
	public static boolean isBigDecimal(Class<?> clazz){
		return null == clazz ? false : clazz.equals(BigDecimal.class);
	}
	
	public static boolean isBigInteger(Class<?> clazz){
		return null == clazz ? false : clazz.equals(BigInteger.class);
	}
	
	public static boolean isCharacter(Class<?> clazz){
		return null == clazz ? false : clazz.equals(Character.TYPE) || clazz.equals(Character.class);
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
	
    private static Class<?> classForResource(Resource resource,String basePath) throws IOException,NotFoundException {
        String fullPath = resource.getURL().toString();
        
        int baseIndex = fullPath.lastIndexOf(basePath + "/");
        
        if(baseIndex <= 0){
            throw new IllegalStateException("invalid resource '" + fullPath + "', can not found base path '" + basePath);
        }
        
        String classFile = fullPath.substring(baseIndex);
        String className = classFile.substring(0,classFile.indexOf(".class")).replace('/', '.');
        
        return forName(className);
    }	
}
