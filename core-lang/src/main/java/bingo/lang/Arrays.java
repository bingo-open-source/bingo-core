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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>null</code> safe {@link Array} utility
 * 
 * @author fenghm (fenghm@bingosoft.net)
 * 
 * @since 0.1
 */
//From apache commons-lang3, under Apache License 2.0
public class Arrays {

	/**
	 * An empty immutable {@code Object} array.
	 */
	public static final Object[]	EMPTY_OBJECT_ARRAY	       = new Object[0];

	/**
	 * An empty immutable {@code Class} array.
	 */
	public static final Class<?>[]	EMPTY_CLASS_ARRAY	       = new Class[0];

	/**
	 * An empty immutable {@code String} array.
	 */
	public static final String[]	EMPTY_STRING_ARRAY	       = new String[0];

	/**
	 * An empty immutable {@code long} array.
	 */
	public static final long[]	    EMPTY_LONG_ARRAY	       = new long[0];

	/**
	 * An empty immutable {@code Long} array.
	 */
	public static final Long[]	    EMPTY_LONG_OBJECT_ARRAY	   = new Long[0];

	/**
	 * An empty immutable {@code int} array.
	 */
	public static final int[]	    EMPTY_INT_ARRAY	           = new int[0];

	/**
	 * An empty immutable {@code Integer} array.
	 */
	public static final Integer[]	EMPTY_INT_OBJECT_ARRAY	   = new Integer[0];

	/**
	 * An empty immutable {@code short} array.
	 */
	public static final short[]	    EMPTY_SHORT_ARRAY	       = new short[0];

	/**
	 * An empty immutable {@code Short} array.
	 */
	public static final Short[]	    EMPTY_SHORT_OBJECT_ARRAY	= new Short[0];

	/**
	 * An empty immutable {@code byte} array.
	 */
	public static final byte[]	    EMPTY_BYTE_ARRAY	       = new byte[0];

	/**
	 * An empty immutable {@code Byte} array.
	 */
	public static final Byte[]	    EMPTY_BYTE_OBJECT_ARRAY	   = new Byte[0];

	/**
	 * An empty immutable {@code double} array.
	 */
	public static final double[]	EMPTY_DOUBLE_ARRAY	       = new double[0];

	/**
	 * An empty immutable {@code Double} array.
	 */
	public static final Double[]	EMPTY_DOUBLE_OBJECT_ARRAY	= new Double[0];

	/**
	 * An empty immutable {@code float} array.
	 */
	public static final float[]	    EMPTY_FLOAT_ARRAY	       = new float[0];

	/**
	 * An empty immutable {@code Float} array.
	 */
	public static final Float[]	    EMPTY_FLOAT_OBJECT_ARRAY	= new Float[0];

	/**
	 * An empty immutable {@code boolean} array.
	 */
	public static final boolean[]	EMPTY_BOOLEAN_ARRAY	       = new boolean[0];

	/**
	 * An empty immutable {@code Boolean} array.
	 */
	public static final Boolean[]	EMPTY_BOOLEAN_OBJECT_ARRAY	= new Boolean[0];

	/**
	 * An empty immutable {@code char} array.
	 */
	public static final char[]	    EMPTY_CHAR_ARRAY	       = new char[0];

	/**
	 * An empty immutable {@code Character} array.
	 */
	public static final Character[]	EMPTY_CHAR_OBJECT_ARRAY	   = new Character[0];

	/**
	 * The index value when an element is not found in a list or array: {@code -1}. This value is returned by methods in
	 * this class and can also be used in comparisons with values returned by various method from {@link java.util.List}
	 * .
	 */
	public static final int	        INDEX_NOT_FOUND	           = -1;

	protected Arrays() {

	}

	/**
	 * Determine whether the given array is empty: i.e. <code>null</code> or of zero length.
	 * 
	 * @param array the array to check
	 * 
	 * @return true if null input or empty elements
	 */
	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive longs is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * 
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(long[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive ints is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(int[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive shorts is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(short[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive chars is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * 
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(char[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive bytes is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * 
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(byte[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive doubles is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * 
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(double[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive floats is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * 
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(float[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive booleans is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * 
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(boolean[] array) {
		return array == null || array.length == 0;
	}

	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if an array of Objects is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param <T> the component type of the array
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static <T> boolean isNotEmpty(T[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive longs is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(long[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive ints is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(int[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive shorts is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(short[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive chars is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(char[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive bytes is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(byte[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive doubles is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(double[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive floats is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(float[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive booleans is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(boolean[] array) {
		return (array != null && array.length != 0);
	}

	// IndexOf search
	// ----------------------------------------------------------------------

	// Object IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given object in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param objectToFind the object to find, may be {@code null}
	 * @return the index of the object within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(Object[] array, Object objectToFind) {
		return indexOf(array, objectToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given object in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will return
	 * {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param objectToFind the object to find, may be {@code null}
	 * @param startIndex the index to start searching at
	 * @return the index of the object within the array starting at the index, {@link #INDEX_NOT_FOUND} ({@code -1}) if
	 *         not found or {@code null} array input
	 */
	public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		if (objectToFind == null) {
			for (int i = startIndex; i < array.length; i++) {
				if (array[i] == null) {
					return i;
				}
			}
		} else if (array.getClass().getComponentType().isInstance(objectToFind)) {
			for (int i = startIndex; i < array.length; i++) {
				if (objectToFind.equals(array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given object within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param objectToFind the object to find, may be {@code null}
	 * @return the last index of the object within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(Object[] array, Object objectToFind) {
		return lastIndexOf(array, objectToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given object in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the array
	 * length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param objectToFind the object to find, may be {@code null}
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the object within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(Object[] array, Object objectToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		} else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		if (objectToFind == null) {
			for (int i = startIndex; i >= 0; i--) {
				if (array[i] == null) {
					return i;
				}
			}
		} else if (array.getClass().getComponentType().isInstance(objectToFind)) {
			for (int i = startIndex; i >= 0; i--) {
				if (objectToFind.equals(array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the object is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param objectToFind the object to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(Object[] array, Object objectToFind) {
		return indexOf(array, objectToFind) != INDEX_NOT_FOUND;
	}

	// long IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(long[] array, long valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will return
	 * {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(long[] array, long valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(long[] array, long valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the array
	 * length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(long[] array, long valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		} else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(long[] array, long valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// int IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(int[] array, int valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will return
	 * {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(int[] array, int valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(int[] array, int valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the array
	 * length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(int[] array, int valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		} else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(int[] array, int valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// short IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(short[] array, short valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will return
	 * {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(short[] array, short valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(short[] array, short valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the array
	 * length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(short[] array, short valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		} else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(short[] array, short valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// char IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 * @since 2.1
	 */
	public static int indexOf(char[] array, char valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will return
	 * {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 * @since 2.1
	 */
	public static int indexOf(char[] array, char valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 * @since 2.1
	 */
	public static int lastIndexOf(char[] array, char valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the array
	 * length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 * @since 2.1
	 */
	public static int lastIndexOf(char[] array, char valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		} else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 * @since 2.1
	 */
	public static boolean contains(char[] array, char valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// byte IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(byte[] array, byte valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will return
	 * {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(byte[] array, byte valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(byte[] array, byte valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the array
	 * length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(byte[] array, byte valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		} else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(byte[] array, byte valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// double IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(double[] array, double valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will return
	 * {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(double[] array, double valueToFind, int startIndex) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(double[] array, double valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the array
	 * length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(double[] array, double valueToFind, int startIndex) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		} else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(double[] array, double valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// float IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(float[] array, float valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will return
	 * {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(float[] array, float valueToFind, int startIndex) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(float[] array, float valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the array
	 * length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(float[] array, float valueToFind, int startIndex) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		} else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(float[] array, float valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// boolean IndexOf
	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(boolean[] array, boolean valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will return
	 * {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code
	 *         null} array input
	 */
	public static int indexOf(boolean[] array, boolean valueToFind, int startIndex) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) if {@code null} array input.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(boolean[] array, boolean valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * 
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger than the array
	 * length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or
	 *         {@code null} array input
	 */
	public static int lastIndexOf(boolean[] array, boolean valueToFind, int startIndex) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		} else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * 
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(boolean[] array, boolean valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}
	
	/**
	 * Copies the specified array, truncating or padding with nulls (if necessary) so the copy has the specified length.
	 * For all indices that are valid in both the original array and the copy, the two arrays will contain identical
	 * values. For any indices that are valid in the copy but not the original, the copy will contain <tt>null</tt>.
	 * Such indices will exist if and only if the specified length is greater than that of the original array.
	 * 
	 * @param original the array to be copied
	 * @param newLength the length of the copy to be returned
	 * @return a copy of the original array, truncated or padded with nulls to obtain the specified length
	 * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
	 * 
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] copyOf(T[] original, int newLength) {
		T[] copy = (T[]) Array.newInstance(original.getClass().getComponentType(), newLength);
		System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
		return copy;
	}

	/**
	 * Converts a T[] array to a List<T> collection.
	 * 
	 * @param <T> elements type
	 * 
	 * @param elements object array to convert.
	 * 
	 * @return List<T> collection contains all the elements in T[] array.
	 */
	public static <T> List<T> toList(T... elements) {
		if (null == elements) {
			return new ArrayList<T>();
		}
		return java.util.Arrays.asList(elements);
	}

	/**
	 * Converts a T[] array to a String[] array.
	 * 
	 * @param <T> elements type
	 * 
	 * @param array object array to convert.
	 * 
	 * @return String[] array contains all the elements in T[] array, {@link #EMPTY_STRING_ARRAY} if input is null.
	 */
	public static final <T> String[] toStringArray(T[] array) {
		if (null == array) {
			return EMPTY_STRING_ARRAY;
		}

		String[] strings = new String[array.length];

		for (int i = 0; i < array.length; i++) {
			Object object = array[i];

			if (null == object) {
				strings[i] = null;
			} else {
				strings[i] = Objects.toString(object);
			}
		}

		return strings;
	}

	/**
	 * cast the supplied array to an array which it's type is the type of the specific to array.
	 * 
	 * @param array the array to be cast.
	 * @param to specify the type of the array after cast.
	 * @return an array after cast.
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T[] cast(Object[] array, T[] to) {
		if (to.length == array.length) {
			for (int i = 0; i < array.length; i++) {
				to[i] = (T) array[i];
			}
			return to;
		} else {
			return toList(array).toArray(to);
		}
	}

	//Private Methods
	//---------------------------------------------------------------------------------------------	
}