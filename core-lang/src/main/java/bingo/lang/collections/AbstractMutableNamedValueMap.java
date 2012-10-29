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

import bingo.lang.Assert;
import bingo.lang.Beans;
import bingo.lang.Named;
import bingo.lang.NamedEntry;
import bingo.lang.beans.BeanModel;
import bingo.lang.beans.BeanProperty;

public abstract class AbstractMutableNamedValueMap<NE extends NamedEntry<Object>> implements NamedValueMap<NE> {
	
	public NamedValueMap<NE> set(Named named, Object value) {
		set(named.getName(),value);
		return this;
	}
	
	public NamedValueMap<NE> setAll(Map<? extends String, ? extends Object> map) {
		for(Entry<? extends String, ? extends Object> entry : map.entrySet()){
			set(entry.getKey(),entry.getValue());
		}
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

	public Object remove(Object key) {
	    return remove(key.toString());
    }

	public Object remove(Named named) {
	    return remove(named.getName());
    }

	public void putAll(Map<? extends String, ? extends Object> t) {
		setAll(t);
    }

	public Object put(String key, Object value) {
		set(key,value);
	    return value;
    }

	public boolean trySet(int index, Object value) {
		set(index,value);
	    return true;
    }

	public boolean trySet(String name, Object value) {
		put(name,value);
	    return true;
    }

	public boolean trySet(Named named, Object value) {
		set(named,value);
	    return true;
    }

	public Object get(Named named) {
		Assert.notNull(named);
	    return get(named.getName());
    }
	
	public boolean containsKey(Object key) {
		Assert.notNull(key);
	    return contains(key.toString());
    }

	public boolean contains(Named named) {
		Assert.notNull(named);
	    return contains(named.getName());
    }

	public Object get(Object key) {
		Assert.notNull(key);
	    return get(key.toString());
    }
	
	public NE getEntry(Named named) {
		Assert.notNull(named);
	    return getEntry(named.getName());
    }

	public <T> T toObject(Class<T> type) {
        BeanModel<T> model = BeanModel.get(type);
        
        T bean = model.newInstance();
        
        for(BeanProperty prop : model.getProperties()){
        	if(prop.isWritable()){
        		NE entry = getEntry(prop.getName());
        		
        		if(null != entry){
        			prop.setValue(bean, entry.getValue());
        		}
        	}
        }
        
        return bean;
    }
}
