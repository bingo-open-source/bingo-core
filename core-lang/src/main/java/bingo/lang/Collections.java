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
import java.util.List;
import java.util.Set;

/**
 * <code>null</code> safe {@link Collection} utility.
 */
public class Collections {

	protected Collections() {

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static <E> List<E> cast(List list){
		return (List<E>)list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static <E> Set<E> cast(Set set){
		return (Set<E>)set;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static <E> Collection<E> cast(Collection c){
		return (Collection<E>)c;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static <E> Iterable<E> cast(Iterable i){
		return (Iterable<E>)i;
	}
	
	public static <E> List<E> listOf(Collection<E> collection) {
		return new ArrayList<E>(collection);
	}
	
	public static <E> List<E> listOf(Iterable<E> iterable) {
		return Enumerables.toList(iterable);
	}
	
	public static <E> List<E> listOf(E... array) {
		return Arrays.toList(array);
	}
	
	public static <E> Set<E> setOf(Collection<E> collection) {
		return new HashSet<E>(collection);
	}
	
	public static <E> Set<E> setOf(Iterable<E> iterable) {
		return Enumerables.toSet(iterable);
	}
	
	public static <E> Set<E> setOf(E... array) {
		return Arrays.toSet(array);
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

	/**
	 * return an empty object array if the supplied {@link Collection} is null. 
	 * Otherwise, return collection.toArray().
	 * 
	 *  @param collection the collection to return array.
	 *  
	 *  @return empty object array if collection is null or collection.toArray().
	 */
	public static Object[] toArray(Collection<?> collection) {
		if (null == collection) {
			return Arrays.EMPTY_OBJECT_ARRAY;
		}

		return collection.toArray();
	}
	
	public static <T> T[] toArray(Collection<T> collection,Class<T> elementType){
		if(null == collection || collection.isEmpty()){
			return Reflects.newArray(elementType, 0);
		}else{
			T[] array = Reflects.newArray(elementType, collection.size());
			
			int i=0;
			for(T e : collection){
				array[i++] = e;
			}
			return array;
		}
	}
	
	public static int[] toIntArray(Collection<Integer> collection){
		if(null == collection || collection.isEmpty()){
			return Arrays.EMPTY_INT_ARRAY;
		}
		int[] array = new int[collection.size()];
		
		int i=0;
		for(Integer value : collection){
			array[i++] = value;
		}
		return array;
	}
	
	public static String[] toStringArray(Collection<String> collection){
		if(null == collection || collection.isEmpty()){
			return Arrays.EMPTY_STRING_ARRAY;
		}
		String[] array = new String[collection.size()];
		int i=0;
		for(String value : collection){
			array[i++] = value;
		}
		return array;
	}
	
	/**
	 * concatenate lists into one list.
	 * 
	 *  @param lists all lists to be concatenated.
	 *  
	 *  @return the one list. it will never return null even the supplied lists is null or empty.
	 *  in that case, it will return an empty list.
	 */
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
	
	public static <T> void addAll(Collection<T> c,T[] array){
		if(null == c || null == array){
			return;
		}
		for(T t : array){
			c.add(t);
		}
	}
	
	public static <T> void addAll(Collection<T> c,Iterable<T> iterable){
		if(null == c || null == iterable){
			return;
		}
		for(T t : iterable){
			c.add(t);
		}
	}
}