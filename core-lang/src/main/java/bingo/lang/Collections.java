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
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bingo.lang.exceptions.EmptyElementException;

/**
 * <code>null</code> safe {@link Collection} utility.
 */
public class Collections {

	protected Collections() {

	}
	
	//isEmpty
	//---------------------------------------------------------------------------------------------------------	

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

	//toArray
	//---------------------------------------------------------------------------------------------------------
	public static Object[] toArray(Collection<?> collection) {
		if (null == collection) {
			return Arrays.EMPTY_OBJECT_ARRAY;
		}

		return collection.toArray();
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
	
	public static Object[] toArray(Enumeration<?> enumeration) {
		ArrayList<Object> list = new ArrayList<Object>();

		while (enumeration.hasMoreElements()) {
			list.add(enumeration.nextElement());
		}

		return list.toArray();
	}	
	
	//toList
	//---------------------------------------------------------------------------------------------------------
	public static <T> List<T> toList(Iterable<T> iterable) {
		if (null == iterable) {
			return new ArrayList<T>();
		}

		ArrayList<T> list = new ArrayList<T>();

		for (T item : iterable) {
			list.add(item);
		}

		return list;
	}
	
	//toSet
	//---------------------------------------------------------------------------------------------------------
	public static <T> Set<T> toSet(Iterable<T> iterable) {
		if (null == iterable) {
			return new HashSet<T>();
		}

		HashSet<T> set = new HashSet<T>();

		for (T item : iterable) {
			set.add(item);
		}

		return set;
	}	

	//concat
	//---------------------------------------------------------------------------------------------------------	
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
	
	//query
	//---------------------------------------------------------------------------------------------------------	
	public static <T> T first(Iterable<T> iterable){
		if(null == iterable){
			throw new EmptyElementException("iterable is null");
		}
		
		for(T e : iterable){
			return e;
		}
		
		throw new EmptyElementException("iterable is empty");
	}
	
	public static <T> T firstOrNull(Iterable<T> iterable, Predicate<T> predicate) {
		if(null == iterable){
			return null;
		}
		
		for (T object : iterable) {
			if (predicate.evaluate(object)) {
				return object;
			}
		}
		return null;
	}

	public static <T> T firstOrNull(T[] array, Predicate<T> predicate) {
		if(null == array){
			return null;
		}
		
		for (T object : array) {
			if (predicate.evaluate(object)) {
				return object;
			}
		}
		return null;
	}

	public static <T, O> O firstOrNull(Iterable<T> iterable, Predicate1<T, O> predicate) {
		if(null == iterable){
			return null;
		}
		
		Out<O> out = new OutObject<O>();
		
		for (T object : iterable) {
			if ((predicate.evaluate(object, out))) {
				return out.getValue();
			}
		}

		return null;
	}

	public static <T, O> O firstOrNull(T[] array, Predicate1<T, O> predicate) {
		if(null == array){
			return null;
		}
		
		Out<O> out = new OutObject<O>();

		for (T object : array) {
			if ((predicate.evaluate(object, out))) {
				return out.getValue();
			}
		}

		return null;
	}
	
	public static <T> List<T> where(Iterable<T> iterable,Predicate<T> where){
		List<T> list = new ArrayList<T>();
		
		if(null != iterable){
			for(T item : iterable){
				if(where.evaluate(item)){
					list.add(item);
				}
			}
		}
		
		return list;
	}
	
	public static <T> List<T> where(T[] array,Predicate<T> where){
		List<T> list = new ArrayList<T>();
		
		if(null != array){
			for(T item : array){
				if(where.evaluate(item)){
					list.add(item);
				}
			}
		}
		
		return list;
	}

	public static <T, O> List<O> select(Iterable<T> iterable, Func1<T, O> func) {
		List<O> list = new ArrayList<O>();

		if(null != iterable){
			for (T object : iterable) {
				list.add(func.evaluate(object));
			}
		}

		return list;
	}

	public static <T, O> List<O> select(T[] array, Func1<T, O> func) {
		List<O> list = new ArrayList<O>();

		if(null != array){
			for (T object : array) {
				list.add(func.evaluate(object));
			}
		}

		return list;
	}

	public static <T, R> Collection<R> selectMany(Iterable<T> iterable, Func1<T,Collection<R>> func) {
		List<R> list = new ArrayList<R>();

		if(null != iterable){
			for (T object : iterable) {
				list.addAll(func.evaluate(object));
			}
		}

		return list;
	}	
}