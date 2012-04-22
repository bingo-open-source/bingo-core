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
package bingo.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import bingo.lang.Func1;
import bingo.lang.Mutable;
import bingo.lang.Predicate;
import bingo.lang.Predicate1;
import bingo.utils.mutable.MutableObject;

/**
 * null safe {@link Collection} utility
 * 
 * @author fenghm (fenghm@bingosoft.net)
 * 
 * @since 0.1
 */
public class Collections {

	protected Collections() {

	}

	/**
	 * Return <code>true</code> if the supplied Collection is <code>null</code> or empty. Otherwise, return
	 * <code>false</code>.
	 * 
	 * @param collection the Collection to check
	 * 
	 * @return whether the given Collection is null or empty
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	public static Object[] toArray(Iterable<?> iterable) {
		if (null == iterable) {
			return Arrays.EMPTY_OBJECT_ARRAY;
		}

		ArrayList<Object> list = new ArrayList<Object>();

		for (Object item : iterable) {
			list.add(item);
		}

		return list.toArray();
	}

	public static Object[] toArray(Collection<?> collection) {
		if (null == collection) {
			return Arrays.EMPTY_OBJECT_ARRAY;
		}

		return collection.toArray();
	}

	public static Object[] toArray(Enumeration<?> enumeration) {
		ArrayList<Object> list = new ArrayList<Object>();

		while (enumeration.hasMoreElements()) {
			list.add(enumeration.nextElement());
		}

		return list.toArray();
	}

	public static <T> List<T> concat(List<T>... lists) {
		ArrayList<T> list = new ArrayList<T>();

		if (null == lists || lists.length == 0) {
			return list;
		}

		for (List<T> l : lists) {
			list.addAll(l);
		}

		return list;
	}

	public static <T> T firstOrNull(Iterable<T> iterable, Predicate<T> predicate) {
		for (T object : iterable) {
			if (predicate.evaluate(object)) {
				return object;
			}
		}
		return null;
	}

	public static <T> T firstOrNull(T[] array, Predicate<T> predicate) {
		for (T object : array) {
			if (predicate.evaluate(object)) {
				return object;
			}
		}
		return null;
	}

	public static <T, R> R firstOrNull(Iterable<T> iterable, Predicate1<T, R> predicate) {
		Mutable<R> result = new MutableObject<R>();

		for (T object : iterable) {
			if ((predicate.evaluate(object, result))) {
				return result.getValue();
			}
		}

		return null;
	}

	public static <T, R> R firstOrNull(T[] array, Predicate1<T, R> predicate) {
		Mutable<R> result = new MutableObject<R>();

		for (T object : array) {
			if ((predicate.evaluate(object, result))) {
				return result.getValue();
			}
		}

		return null;
	}
	
	public static <T> List<T> select(Iterable<T> iterable,Predicate<T> predicate){
		List<T> list = new ArrayList<T>();
		
		if(null != iterable){
			for(T item : iterable){
				if(predicate.evaluate(item)){
					list.add(item);
				}
			}
		}
		
		return list;
	}

	public static <T, R> List<R> select(Iterable<T> iterable, Func1<T, R> func) {
		List<R> list = new ArrayList<R>();

		if(null != iterable){
			for (T object : iterable) {
				list.add(func.evaluate(object));
			}
		}

		return list;
	}

	public static <T, R> List<R> select(T[] array, Func1<T, R> func) {
		List<R> list = new ArrayList<R>();

		if(null != array){
			for (T object : array) {
				list.add(func.evaluate(object));
			}
		}

		return list;
	}

	public static <T, R> List<R> selectMany(Iterable<T> iterable, Func1<T, Collection<R>> func) {
		List<R> list = new ArrayList<R>();

		if(null != iterable){
			for (T object : iterable) {
				list.addAll(func.evaluate(object));
			}
		}

		return list;
	}
}