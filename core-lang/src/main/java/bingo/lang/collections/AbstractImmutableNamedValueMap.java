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
import bingo.lang.Named;
import bingo.lang.NamedEntry;
import bingo.lang.beans.BeanModel;
import bingo.lang.beans.BeanProperty;

public abstract class AbstractImmutableNamedValueMap<NE extends NamedEntry<Object>> extends AbstractImmutableMap<String, Object> implements NamedValueMap<NE> {

	public Object remove(int index) {
		throw readonlyException();
    }

	public Object remove(Named named) {
		throw readonlyException();
    }

	public Object put(int index, Object value) throws IndexOutOfBoundsException {
		throw readonlyException();
    }

	public Object put(Named named, Object value) {
		throw readonlyException();
    }

	public NamedValueMap<NE> set(int index, Object value) {
		throw readonlyException();
    }

	public NamedValueMap<NE> set(String name, Object value) {
		throw readonlyException();	    
    }

	public NamedValueMap<NE> set(Named named, Object value) {
		throw readonlyException();
	}
	
	public NamedValueMap<NE> setAll(Map<? extends String, ? extends Object> map) {
		throw readonlyException();
    }
	
	public <T> NamedValueMap<NE> setAll(T bean) {
		throw readonlyException();
    }

	public boolean trySet(int index, Object value) {
	    return false;
    }

	public boolean trySet(String name, Object value) {
	    return false;
    }

	public boolean trySet(Named named, Object value) {
	    return false;
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
