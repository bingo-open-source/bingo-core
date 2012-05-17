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
package bingo.lang.collections;

import java.util.Map;

import bingo.lang.exceptions.ReadonlyException;

public abstract class AbstractImmutableMap<K,V> implements Map<K, V> {

	public void clear() {
		throw readonlyException();
    }

	public V put(K key, V value) {
		throw readonlyException();
    }

	public void putAll(Map<? extends K, ? extends V> t) {
		throw readonlyException();	    
    }

	public V remove(Object key) {
		throw readonlyException();
    }

	protected static ReadonlyException readonlyException() {
		return new ReadonlyException("unsupported operation, the map is immutable");
	}

}