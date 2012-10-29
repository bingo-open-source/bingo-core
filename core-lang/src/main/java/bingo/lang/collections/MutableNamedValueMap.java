/*
a * Copyright 2012 the original author or authors.
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
import bingo.lang.Beans;
import bingo.lang.Enumerables;
import bingo.lang.Func1;
import bingo.lang.Named;
import bingo.lang.NamedEntry;
import bingo.lang.beans.BeanModel;
import bingo.lang.beans.BeanProperty;
import bingo.lang.tuple.MutableNamedEntry;

public class MutableNamedValueMap<NE extends NamedEntry<Object>> implements NamedValueMap<NE> {
	
	protected final LinkedHashMap<String, NE> _map;
	
	public MutableNamedValueMap(){
		this._map = new LinkedHashMap<String, NE>();
	}
	
	public MutableNamedValueMap(int initialCapacity){
		this._map = new LinkedHashMap<String,NE>(initialCapacity);
	}
	
	public MutableNamedValueMap(Map<? extends String,? extends Object> map){
		this();
		this.putAll(map);
	}
	
	public int size() {
	    return _map.size();
    }

	public boolean isEmpty() {
	    return _map.isEmpty();
    }

	public boolean containsKey(Object key) {
		Assert.notNull(key);
	    return _map.containsKey(key.toString().toLowerCase());
    }
	
	public boolean contains(String name) {
		Assert.notNull(name);
	    return _map.containsKey(name.toLowerCase());
    }
	
	public boolean contains(Named named) {
		Assert.notNull(named);
	    return contains(named.getName());
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
	
	public Object get(Named named) {
		Assert.notNull(named);
	    return get(named.getName());
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
	
	public NamedValueMap<NE> set(int index, Object value) {
		put(index,value);
		return this;
    }

	public NamedValueMap<NE> set(String name, Object value) {
		put(name,value);
		return this;
    }

	public NamedValueMap<NE> set(Named named, Object value) {
		put(named,value);
		return this;
    }
	
	public boolean trySet(int index, Object value) {
		put(index,value);
	    return true;
    }

	public boolean trySet(String name, Object value) {
	    put(name,value);
	    return true;
    }

	public boolean trySet(Named named, Object value) {
		put(named,value);
		return true;
    }

	public NamedValueMap<NE> setAll(Map<? extends String, ? extends Object> map) {
		putAll(map);
		return this;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> NamedValueMap<NE> setAll(T bean) {
		if(null == bean){
			return this;
		}
		
		if(bean instanceof Map){
			setAll((Map)bean);
		}else{
			setAll(Beans.toMap(bean));
		}
	    return this;
    }

	public Object put(int index, Object value) throws IndexOutOfBoundsException {
		getEntry(index).setValue(value);
		return value;
    }

    public Object put(String name, Object value) {
		Assert.notNull(name);
		
		String key = name.toLowerCase();
		NE entry = _map.get(key);
		
		if(null != entry){
			entry.setValue(value);
		}else{
			_map.put(key,newEntry(name, value));
		}
		
	    return value;
    }
	
	public Object put(Named named, Object value) {
		Assert.notNull(named);
	    return put(named.getName(),value);
    }

	public NE getEntry(Named named) {
		Assert.notNull(named);
	    return getEntry(named.getName());
    }

	public Object remove(String name) {
		Assert.notNull(name);
		NE entry = _map.remove(name.toLowerCase());
		return null == entry ? null : entry.getValue();
    }
	
	public Object remove(int index) {
		Entry<String, Object> entry = getEntry(index);
	    return null == entry ? null : remove(entry.getKey());
    }

	public Object remove(Named named) {
		Assert.notNull(named);
	    return remove(named.getName());
    }

	public Object remove(Object key) {
		Assert.notNull(key);
	    return remove(key.toString());
    }

	public void putAll(Map<? extends String, ? extends Object> t) {
		for(Entry<? extends String, ? extends Object> entry : t.entrySet()){
			put(entry.getKey(),entry.getValue());
		}
    }

	public void clear() {
		_map.clear();
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

	public <T> T toObject(Class<T> type) {
        BeanModel<T> model = BeanModel.get(type);
        
        T bean = model.newInstance();
        
        for(BeanProperty prop : model.getProperties()){
        	if(prop.isWritable()){
        		Entry<String, Object> entry = getEntry(prop.getName());
        		
        		if(null != entry){
        			prop.setValue(bean, entry.getValue());
        		}
        	}
        }
        
        return bean;
    }
	
	@SuppressWarnings("unchecked")
    protected NE newEntry(String name,Object value){
		return (NE)new MutableNamedEntry<Object>(name,value);
	}
}