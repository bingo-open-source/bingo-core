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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bingo.lang.enumerable.ArrayEnumerable;
import bingo.lang.enumerable.EmptyEnumerable;
import bingo.lang.enumerable.IterableEnumerable;
import bingo.lang.enumerable.ListEnumerable;
import bingo.lang.enumerable.NamedArrayEnumerable;
import bingo.lang.enumerable.NamedEmptyEnumerable;
import bingo.lang.enumerable.NamedIterableEnumerable;
import bingo.lang.enumerable.NamedListEnumerable;
import bingo.lang.enumerable.NamedSetEnumerable;
import bingo.lang.enumerable.SetEnumerable;
import bingo.lang.exceptions.EmptyDataException;

public class Enumerables {
	
	public static final Enumerable<Object> EMPTY_OBJECT_ENUMEABLE = new EmptyEnumerable<Object>();

	protected Enumerables(){
		
	}
	
	public static final <E> Enumerable<E> empty(){
		return new EmptyEnumerable<E>();
	}
	
	public static final <E> Enumerable<E> of(E... array){
		return null == array || array.length == 0 ? new EmptyEnumerable<E>() : new ArrayEnumerable<E>(array);
	}
	
	public static final <E> Enumerable<E> of(Collection<E> collection){
		return null == collection || collection.isEmpty() ? new EmptyEnumerable<E>() :  new ListEnumerable<E>(collection);
	}
	
	public static final <E> Enumerable<E> of(List<E> list){
		return null == list || list.isEmpty() ? new EmptyEnumerable<E>() :  new ListEnumerable<E>(list);
	}

	public static final <E> Enumerable<E> of(Set<E> set){
		return null == set || set.isEmpty() ? new EmptyEnumerable<E>() :  new SetEnumerable<E>(set);
	}
	
	public static final <E> Enumerable<E> of(Iterable<E> iterable){ 
		return null == iterable ? new EmptyEnumerable<E>() :  (iterable instanceof Enumerable ? (Enumerable<E>)iterable :  IterableEnumerable.of(iterable));
	}
	
	public static final <E extends Named> NamedEnumerable<E> namedEmpty(){
		return new NamedEmptyEnumerable<E>();
	}
	
	public static final NamedEnumerable<NamedEntry<String>> namedOf(Map<String,String> map) {
		return namedOf(Maps.toNamedEntryArray(map));
	}
	
	public static final <E extends Named> NamedEnumerable<E> namedOf(E... array){
		return null == array || array.length == 0 ? new NamedEmptyEnumerable<E>() : new NamedArrayEnumerable<E>(array);
	}
	
	public static final <E extends Named> NamedEnumerable<E> namedOf(Collection<E> collection){
		return null == collection || collection.isEmpty() ? new NamedEmptyEnumerable<E>() :  new NamedListEnumerable<E>(collection);
	}
	
	public static final <E extends Named> NamedEnumerable<E> namedOf(List<E> list){
		return null == list || list.isEmpty() ? new NamedEmptyEnumerable<E>() :  new NamedListEnumerable<E>(list);
	}

	public static final <E extends Named> NamedEnumerable<E> namedOf(Set<E> set){
		return null == set || set.isEmpty() ? new NamedEmptyEnumerable<E>() :  new NamedSetEnumerable<E>(set);
	}
	
	public static final <E extends Named> NamedEnumerable<E> namedOf(Iterable<E> iterable){ 
		return null == iterable ? new NamedEmptyEnumerable<E>() : new NamedIterableEnumerable<E>(iterable);
	}
	
	//isEmpty
	//-------------------------------------------------------------------------------------------------------
	/**
	 * Return <code>true</code> if the supplied array is <code>null</code> or empty. Otherwise, return
	 * <code>false</code>.
	 * 
	 * @param array the array to check
	 * 
	 * @return whether the given array is null or empty
	 */
	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}
	
	/**
	 * Return <code>true</code> if the supplied Iterable is <code>null</code> or empty. Otherwise, return
	 * <code>false</code>.
	 * 
	 * @param iterable the Iterable to check
	 * 
	 * @return whether the given Iterable is null or empty
	 */
	public static boolean isEmpty(Iterable<?> iterable) {
		return (iterable == null || !iterable.iterator().hasNext());
	}
	
	//toArray
	//-------------------------------------------------------------------------------------------------------
	/**
	 * return an empty object array if the supplied {@link Iterable} is null. 
	 * Otherwise, return an object array of all iterable.
	 * 
	 *  @param iterable the iterable to Array.
	 *  
	 *  @return empty object array if iterable is null or an object array of all iterable.
	 */
	public static Object[] toArray(Iterable<?> iterable) {
		if (null == iterable) {
			return Arrays.EMPTY_OBJECT_ARRAY;
		}
		
		Iterator<?> it = iterable.iterator();
		
		if(it.hasNext()){
			ArrayList<Object> list = new ArrayList<Object>();

			do {
				list.add(it.next());
			}while(it.hasNext());
			
			return list.toArray();
		}

		return Arrays.EMPTY_OBJECT_ARRAY;
	}
	
	public static String[] toStringArray(Iterable<String> iterable){
		if(null == iterable){
			return Arrays.EMPTY_STRING_ARRAY;
		}
		
		Iterator<String> it = iterable.iterator();
		if(it.hasNext()){
			ArrayList<String> list = new ArrayList<String>();
			
			do{
				list.add(it.next());
			}while(it.hasNext());
			
			return list.toArray(new String[list.size()]);
		}
		
		return Arrays.EMPTY_STRING_ARRAY;
	}
	
	public static <T> T[] toArray(Iterable<T> iterable, Class<T> elementType) {
		if (null == iterable) {
			return Arrays.newInstance(elementType, 0);
		}

		Iterator<T> it = iterable.iterator();
		
		if(it.hasNext()){
			
			ArrayList<T> list = new ArrayList<T>();

			do {
				list.add(it.next());
			}while(it.hasNext());

			return Collections.toArray(list,elementType);
		}
		
		return Arrays.newInstance(elementType, 0);
	}
	
	//toList
	/**
	 * return a list containing all elements in the supplied iterable.
	 * 
	 * @param iterable the supplied iterable.
	 * 
	 * @return a list containing all elements in the supplied iterable.
	 */
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
	/**
	 * return a set containing all elements in the supplied iterable.
	 * 
	 * @param iterable the supplied iterable.
	 * 
	 * @return a set containing all elements in the supplied iterable.
	 */
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
	
	/**
	 * return the first element in the supplied {@link Iterable}.
	 * 
	 * @param iterable get the first element from this iterable.
	 * 
	 * @return the first element.
	 * 
	 * @throws EmptyDataException if the supplied iterable is null or empty.
	 */
	public static <T> T first(Iterable<T> iterable) throws EmptyDataException {
		if(null == iterable){
			throw new EmptyDataException("iterable is null");
		}
		
		for(T e : iterable){
			return e;
		}
		
		throw new EmptyDataException("iterable is empty");
	}
	
	public static <T> T firstOrNull(Iterable<T> iterable) {
		if(null == iterable){
			return null;
		}
		
		for(T e : iterable){
			return e;
		}
		
		return null;
	}
	
	public static <T> T firstOrNull(Iterable<? extends T> iterable, Predicate<T> predicate) {
		if(null == iterable){
			return null;
		}
		
		for (T object : iterable) {
			if (predicate.apply(object)) {
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
			if (predicate.apply(object)) {
				return object;
			}
		}
		
		return null;
	}

	public static <T, O> O firstOrNull(Iterable<? extends T> iterable, OutPredicate<T, O> predicate) {
		if(null == iterable){
			return null;
		}
		
		Out<O> out = new OutObject<O>();
		
		for (T object : iterable) {
			if ((predicate.apply(object, out))) {
				return out.getValue();
			}
		}

		return null;
	}

	public static <T, O> O firstOrNull(T[] array, OutPredicate<T, O> predicate) {
		if(null == array){
			return null;
		}
		
		Out<O> out = new OutObject<O>();

		for (T object : array) {
			if ((predicate.apply(object, out))) {
				return out.getValue();
			}
		}

		return null;
	}

	public static <T> List<T> where(Iterable<? extends T> iterable,Predicate<T> where){
		List<T> list = new ArrayList<T>();
		
		if(null != iterable){
			for(T item : iterable){
				if(where.apply(item)){
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
				if(where.apply(item)){
					list.add(item);
				}
			}
		}
		
		return list;
	}
	
	public static <T> boolean any(Iterable<? extends T> iterable,Predicate<T> predicate) {
		if(null == iterable){
			return false;
		}
		
		for(T item : iterable){
			if(predicate.apply(item)){
				return true;
			}
		}
		return false;
	}

	public static <T, O> List<O> select(Iterable<? extends T> iterable, Func1<T, O> func) {
		List<O> list = new ArrayList<O>();

		if(null != iterable){
			for (T object : iterable) {
				list.add(func.apply(object));
			}
		}

		return list;
	}
	
	public static <T, O> Set<O> selectForSet(Iterable<? extends T> iterable, Func1<T,O> func){
		Set<O> set = new LinkedHashSet<O>();

		if(null != iterable){
			for (T object : iterable) {
				set.add(func.apply(object));
			}
		}

		return set;
	}

	public static <T, O> List<O> select(T[] array, Func1<T, O> func) {
		List<O> list = new ArrayList<O>();

		if(null != array){
			for (T object : array) {
				list.add(func.apply(object));
			}
		}

		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static <E,T> List<T> ofType(Iterable<E> iterable,Class<T> type){
		List<T> list = new ArrayList<T>();
		
		if(null != iterable){
			for(E e : iterable){
				if(null != e && type.isAssignableFrom(e.getClass())){
					list.add((T)e);
				}
			}
		}
		
		return list;
	}

	public static <T, R> List<R> selectMany(Iterable<T> iterable, Func1<T,Collection<R>> func) {
		List<R> list = new ArrayList<R>();

		if(null != iterable){
			for (T object : iterable) {
				list.addAll(func.apply(object));
			}
		}

		return list;
	}
	
	public static <T> String join(T[] array,char separator,Func1<T, String> func) {
		if (array == null) {
			return Strings.EMPTY;
		}

		int len = array.length;

		StringBuilder buf = new StringBuilder(len * 16);

		for (int i = 0; i < len; i++) {
			if (i > 0) {
				buf.append(separator);
			}

			if (array[i] != null) {
				buf.append(func.apply(array[i]));
			}
		}

		return buf.toString();
	}	
	
	public static <T> String join(Iterable<T> iterable,char separator,Func1<T, String> func) {
		if(null == iterable){
			return Strings.EMPTY;
		}

		StringBuilder buf = new StringBuilder();
		
		int i=0;

		for (T e : iterable) {
			if (i > 0) {
				buf.append(separator);
			}

			if (null != e) {
				buf.append(func.apply(e));
				i++;
			}
		}

		return buf.toString();
	}
	
	public static <T extends Named> T get(Iterable<T> iterable,String name){
		if(null == iterable){
			return null;
		}
		
		for(T item : iterable){
			if(Strings.equals(item.getName(), name)){
				return item;
			}
		}
		return null;
	}
	
	public static <T extends Named> T find(Iterable<T> iterable,String name){
		if(null == iterable){
			return null;
		}
		
		for(T item : iterable){
			if(Strings.equalsIgnoreCase(item.getName(), name)){
				return item;
			}
		}
		return null;
	}
	
	public static <T extends Named> T find(T[] array,String name){
		if(null == array){
			return null;
		}
		
		for(T item : array){
			if(Strings.equalsIgnoreCase(item.getName(), name)){
				return item;
			}
		}
		
		return null;
	}
	
	public static boolean contains(Iterable<? extends Named> iterable,String name){
		if(null == iterable){
			return false;
		}
		
		for(Named item : iterable){
			if(Strings.equals(item.getName(), name)){
				return true;
			}
		}
		
		return false;
	}	
	
	public static boolean containsIgnoreCase(Iterable<? extends Named> iterable,String name){
		if(null == iterable){
			return false;
		}
		
		for(Named item : iterable){
			if(Strings.equalsIgnoreCase(item.getName(), name)){
				return true;
			}
		}
		
		return false;
	}
}
