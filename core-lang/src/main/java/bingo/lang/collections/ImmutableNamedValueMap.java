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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import bingo.lang.Assert;
import bingo.lang.Enumerables;
import bingo.lang.Func1;
import bingo.lang.NamedEntry;

public class ImmutableNamedValueMap<NE extends NamedEntry<Object>> extends AbstractImmutableNamedValueMap<NamedEntry<Object>> {
	
	protected final LinkedHashMap<String, NE> _map;
	
	public ImmutableNamedValueMap(){
		this._map = new LinkedHashMap<String,NE>();
	}
	
	public ImmutableNamedValueMap(int initialCapacity){
		this._map = new LinkedHashMap<String,NE>(initialCapacity);
	}
	
	public ImmutableNamedValueMap(Map<? extends String,? extends Object> map){
		this();
		this.putAll(map);
	}

	public int size() {
	    return _map.size();
    }

	public boolean isEmpty() {
	    return _map.isEmpty();
    }

	public boolean contains(String name) {
		Assert.notNull(name);
	    return _map.containsKey(name.toLowerCase());
    }
	
	public boolean containsValue(Object value) {
		if(null == value){
			return false;
		}
		for(Entry<String, Object> entry : _map.values()){
			if(entry.getValue().equals(value)){
				return true;
			}
		}
	    return false;
    }
	
	public Object get(int index) throws IndexOutOfBoundsException{
		Entry<String,Object> entry = getEntry(index);
		return null == entry ? null : entry.getValue();
	}
	
	public Object get(String name) {
		Assert.notNull(name);
		Entry<String, Object> entry = getEntry(name);
		return null == entry ? null : entry.getValue();
	}

	public Object get(Object name) {
		Assert.notNull(name);
		Entry<String, Object> entry = getEntry(name.toString());
		return null == entry ? null : entry.getValue();
	}
	
	public NE getEntry(int index) throws IndexOutOfBoundsException {
		if(index >= 0){
			int i=0;
			for(NE entry : _map.values()){
				if(i == index){
					return entry;
				}
				i++;
			}
		}
		throw new IndexOutOfBoundsException(String.valueOf(index));
    }

    public NE getEntry(String name) {
		Assert.notNull(name);
		String key = name.toLowerCase();
		return _map.get(key);
    }
	
	public Set<String> keySet() {
	    return Enumerables.selectForSet(_map.values(), new Func1<Entry<String,Object>,String>(){
			public String apply(java.util.Map.Entry<String, Object> input) {
	            return input.getKey();
            }
	    });
    }

	public Collection<Object> values() {
	    return Enumerables.select(_map.values(), new Func1<Entry<String,Object>,Object>(){
			public Object apply(java.util.Map.Entry<String, Object> input) {
	            return input.getValue();
            }
	    });
    }

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
	    return new LinkedHashSet<Map.Entry<String,Object>>(_map.values());
    }
}
