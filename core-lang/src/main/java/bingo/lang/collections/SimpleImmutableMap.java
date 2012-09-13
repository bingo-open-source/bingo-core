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

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import bingo.lang.Immutable;
import bingo.lang.exceptions.ReadonlyException;
import bingo.lang.tuple.ImmutableEntry;

public class SimpleImmutableMap<K,V> extends AbstractMap<K,V> implements Immutable {
	
	private final Set<Entry<K, V>> entrySet;
	
	public SimpleImmutableMap(Map<K, V> map){
		Set<Entry<K,V>> set = map instanceof LinkedHashMap ? new LinkedHashSet<Entry<K,V>>(map.size()) : new HashSet<Entry<K,V>>(map.size());

		for(Entry<K, V> entry : map.entrySet()){
			set.add(ImmutableEntry.of(entry.getKey(), entry.getValue()));
		}
		
		this.entrySet = new WrappedImmutableSet<Map.Entry<K,V>>(set);
	}

	@Override
    public Set<Entry<K, V>> entrySet() {
	    return entrySet;
    }

	@Override
	public void clear() {
		throw readonlyException();
    }

	@Override
	public V put(K key, V value) {
		throw readonlyException();
    }

	@Override
	public void putAll(Map<? extends K, ? extends V> t) {
		throw readonlyException();	    
    }

	@Override
	public V remove(Object key) {
		throw readonlyException();
    }

	protected static ReadonlyException readonlyException() {
		return new ReadonlyException("unsupported operation, the map is immutable");
	}
}
