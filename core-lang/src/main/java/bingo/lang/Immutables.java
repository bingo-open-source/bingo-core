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

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import bingo.lang.collections.WrappedImmutableList;
import bingo.lang.collections.WrappedImmutableSet;
import bingo.lang.tuple.ImmutableEntry;

public class Immutables {

	protected Immutables(){
		
	}
	
	public static <E> List<E> listOf(List<E> list) {
		return new WrappedImmutableList<E>(list);
	}
	
	public static <E> List<E> listOf(Iterable<E> iterable) {
		return new WrappedImmutableList<E>(Enumerables.toList(iterable));
	}
	
	public static <E> List<E> listOf(E... array) {
		return new WrappedImmutableList<E>(Arrays.toList(array));
	}
	
	public static <E> Set<E> setOf(Set<E> set) {
		return new WrappedImmutableSet<E>(set);
	}
	
	public static <E> Set<E> setOf(Iterable<E> iterable) {
		return new WrappedImmutableSet<E>(Enumerables.toSet(iterable));
	}
	
	public static <E> Set<E> setOf(E... array) {
		return new WrappedImmutableSet<E>(Arrays.toSet(array));
	}

	public static <K,V> Entry<K, V> entryOf(K key,V value) {
		return new ImmutableEntry<K, V>(key, value);
	}
}