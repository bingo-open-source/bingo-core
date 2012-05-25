/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bingo.lang;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//from google guava

/**
 * Contains static utility methods pertaining to primitive types and their corresponding wrapper types.
 * 
 * @author Kevin Bourrillion
 * @since 1.0
 */
public class Primitives {
	
	/** A map from primitive types to their corresponding wrapper types. */
	private static final Map<Class<?>, Class<?>>	PRIMITIVE_TO_WRAPPER_TYPE;

	/** A map from wrapper types to their corresponding primitive types. */
	private static final Map<Class<?>, Class<?>>	WRAPPER_TO_PRIMITIVE_TYPE;
	
	protected Primitives() {

	}

	// Sad that we can't use a BiMap. :(

	static {
		Map<Class<?>, Class<?>> primToWrap = new HashMap<Class<?>, Class<?>>(16);
		Map<Class<?>, Class<?>> wrapToPrim = new HashMap<Class<?>, Class<?>>(16);

		add(primToWrap, wrapToPrim, boolean.class, Boolean.class);
		add(primToWrap, wrapToPrim, byte.class, Byte.class);
		add(primToWrap, wrapToPrim, char.class, Character.class);
		add(primToWrap, wrapToPrim, double.class, Double.class);
		add(primToWrap, wrapToPrim, float.class, Float.class);
		add(primToWrap, wrapToPrim, int.class, Integer.class);
		add(primToWrap, wrapToPrim, long.class, Long.class);
		add(primToWrap, wrapToPrim, short.class, Short.class);
		add(primToWrap, wrapToPrim, void.class, Void.class);

		PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(primToWrap);
		WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(wrapToPrim);
	}

	private static void add(Map<Class<?>, Class<?>> forward, Map<Class<?>, Class<?>> backward,
							Class<?> key, Class<?> value) {
		forward.put(key, value);
		backward.put(value, key);
	}
	
	/**
	 * <p>
	 * 	返回一个基本类型对应的默认值。
	 * </p>
	 * {@link Integer} 的默认值为0；<br>
	 * {@link Boolean} 的默认值为 <code>false</code>；<br>
	 * {@link Long} 的默认值为0L；<br>
	 * {@link Float} 的默认值为0.0f；<br>
	 * {@link Double} 的默认值为0.0d；<br>
	 * {@link Short} 的默认值为0；<br>
	 * {@link Byte} 的默认值为0；<br>
	 * {@link Character} 的默认值为\u0000。<br>
	 * @param type 要返回其默认值的类型。
	 * @return 该类型对应的默认值。
	 */
	public static Object defaultValue(Class<?> type){
        if(Integer.TYPE == type){
            return 0;
        }
        
        if(Boolean.TYPE == type){
            return false;
        }
        
        if(Long.TYPE == type){
            return 0L;
        }
        
        if(Float.TYPE == type){
            return 0.0f;
        }
        
        if(Double.TYPE == type){
            return 0.0d;
        }
        
        if(Short.TYPE == type){
            return 0;
        }
        
        if(Byte.TYPE == type){
        	return 0;
        }
        
        if(Character.TYPE == type){
        	return '\u0000';
        }
        
        return null;
	}

	/**
	 * Returns an immutable set of all nine primitive types (including {@code void}). Note that a simpler way to test
	 * whether a {@code Class} instance is a member of this set is to call {@link Class#isPrimitive}.
	 * 
	 * @since 3.0
	 */
	public static Set<Class<?>> getPrimitiveTypes() {
		return PRIMITIVE_TO_WRAPPER_TYPE.keySet();
	}

	/**
	 * Returns an immutable set of all nine primitive-wrapper types (including {@link Void}).
	 * 
	 * @since 3.0
	 */
	public static Set<Class<?>> getWrapperTypes() {
		return WRAPPER_TO_PRIMITIVE_TYPE.keySet();
	}

	/**
	 * Returns {@code true} if {@code type} is one of the nine primitive-wrapper types, such as {@link Integer}.
	 * 
	 * @see Class#isPrimitive
	 */
	public static boolean isWrapperType(Class<?> type) {
		return WRAPPER_TO_PRIMITIVE_TYPE.containsKey(Assert.notNull(type));
	}

	/**
	 * 判断该类型是否为 {@link Boolean} 。
	 * @param type 要判断的类型。
	 * @return 如果是 {@link Boolean} 则返回 <code>true</code> 。
	 */
	public static boolean isBoolean(Class<?> type){
		return Boolean.TYPE == type || Boolean.class == type;
	}
	
	/**
	 * 判断该类型是否为 {@link Integer} 。
	 * @param type 要判断的类型。
	 * @return 如果是 {@link Integer} 则返回 <code>true</code> 。
	 */
	public static boolean isInteger(Class<?> type){
		return Integer.TYPE == type || Integer.class == type;
	}
	
	/**
	 * 判断类型是否为 {@link Long} 。
	 * @param type 要判断的类型。
	 * @return 如果是 {@link Long} 则返回 <code>true</code> 。
	 */
	public static boolean isLong(Class<?> type){
		return Long.TYPE == type || Long.class == type;
	}
	
	/**
	 * 判断类型是否为 {@link Short} 。
	 * @param type 要判断的类型。
	 * @return 如果是 {@link Short} 则返回 <code>true</code> 。
	 */
	public static boolean isShort(Class<?> type){
		return Short.TYPE == type || Short.class == type;
	}
	
	/**
	 * 判断类型是否为 {@link Byte} 。
	 * @param type 要判断的类型。
	 * @return 如果是 {@link Byte} 则返回 <code>true</code> 。
	 */
	public static boolean isByte(Class<?> type){
		return Byte.TYPE == type || Byte.class == type;
	}	
	
	/**
	 * 判断类型是否为 {@link Character} 。
	 * @param type 要判断的类型。
	 * @return 如果是 {@link Character} 则返回 <code>true</code> 。
	 */
	public static boolean isCharacter(Class<?> type){
		return Character.TYPE == type || Character.class == type;
	}	
	
	/**
	 * 判断类型是否为 {@link Float} 。
	 * @param type 要判断的类型。
	 * @return 如果是 {@link Float} 则返回 <code>true</code> 。
	 */
	public static boolean isFloat(Class<?> type){
		return Float.TYPE == type || Float.class == type;
	}	
	
	/**
	 * 判断类型是否为 {@link Double} 。
	 * @param type 要判断的类型。
	 * @return 如果是 {@link Double} 则返回 <code>true</code> 。
	 */
	public static boolean isDouble(Class<?> type){
		return Double.TYPE == type || Double.class == type;
	}	
	
	/**
	 * Returns the corresponding wrapper type of {@code type} if it is a primitive type; otherwise returns {@code type}
	 * itself. Idempotent.
	 * 
	 * <pre>
	 *     wrap(int.class) == Integer.class
	 *     wrap(Integer.class) == Integer.class
	 *     wrap(String.class) == String.class
	 * </pre>
	 */
	public static <T> Class<T> wrap(Class<T> type) {
		Assert.notNull(type);

		// cast is safe: long.class and Long.class are both of type Class<Long>
		@SuppressWarnings("unchecked")
		Class<T> wrapped = (Class<T>) PRIMITIVE_TO_WRAPPER_TYPE.get(type);
		return (wrapped == null) ? type : wrapped;
	}

	/**
	 * Returns the corresponding primitive type of {@code type} if it is a wrapper type; otherwise returns {@code type}
	 * itself. Idempotent.
	 * 
	 * <pre>
	 *     unwrap(Integer.class) == int.class
	 *     unwrap(int.class) == int.class
	 *     unwrap(String.class) == String.class
	 * </pre>
	 */
	public static <T> Class<T> unwrap(Class<T> type) {
		Assert.notNull(type);

		// cast is safe: long.class and Long.class are both of type Class<Long>
		@SuppressWarnings("unchecked")
		Class<T> unwrapped = (Class<T>) WRAPPER_TO_PRIMITIVE_TYPE.get(type);
		return (unwrapped == null) ? type : unwrapped;
	}
}
