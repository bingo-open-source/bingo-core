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

import java.util.Map.Entry;

public final class Predicates {
	
	public static <T extends Named> Predicate<T> nameEquals(final String name){
		return new Predicate<T>() {
			public boolean apply(T object) {
				return Strings.equals(object.getName(), name);
			}
		};
	}

	public static <T extends Named> Predicate<T> nameEqualsIgnoreCase(final String name){
		return new Predicate<T>() {
			public boolean apply(T object) {
				return Strings.equalsIgnoreCase(object.getName(),name);
			}
		};
	}
	
	public static <K,V> Predicate<Entry<K,V>> entryKeyEquals(final K key){
		return new Predicate<Entry<K,V>>() {
			public boolean apply(Entry<K, V> entry) {
	            return entry.getKey().equals(key);
            }
		};
	}
	
	public static <V> Predicate<Entry<String,V>> entryKeyEqualsIgnoreCase(final String key){
		return new Predicate<Entry<String,V>>() {
			public boolean apply(Entry<String, V> entry) {
	            return entry.getKey().equalsIgnoreCase(key);
            }
		};
	}
}
