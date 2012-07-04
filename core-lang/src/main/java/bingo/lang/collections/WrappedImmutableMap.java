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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WrappedImmutableMap<K,V> extends AbstractImmutableMap<K, V> {
	
	private final Map<K, V> map;
	
	public WrappedImmutableMap(Map<K, V> map){
		this.map = null == map ? new HashMap<K, V>() : map;
	}

	public boolean containsKey(Object key) {
	    return map.containsKey(key);
    }

	public boolean containsValue(Object value) {
	    return map.containsValue(value);
    }

	public Set<java.util.Map.Entry<K, V>> entrySet() {
	    return map.entrySet();
    }

	public V get(Object key) {
	    return map.get(key);
    }

	public boolean isEmpty() {
	    return map.isEmpty();
    }

	public Set<K> keySet() {
	    return map.keySet();
    }

	public int size() {
	    return map.size();
    }

	public Collection<V> values() {
	    return map.values();
    }
}
