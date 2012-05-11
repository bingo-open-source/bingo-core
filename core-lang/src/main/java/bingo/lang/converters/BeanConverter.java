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
package bingo.lang.converters;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import bingo.lang.Classes;
import bingo.lang.Converts;
import bingo.lang.Out;
import bingo.lang.reflect.ReflectClass;
import bingo.lang.reflect.ReflectField;


@SuppressWarnings("unchecked")
public class BeanConverter extends AbstractConverter<Object>{

	@Override
    public boolean convertFrom(Object value, Class<?> targetType, Type genericType, Out<Object> out) throws Throwable {
		if(!Classes.isConcrete(targetType)){
			return false;
		}
		
		if(value instanceof Map){
			return out.returns(convertFromMap(targetType, genericType, (Map)value));
		}
		
		return false;
    }
	
	@Override
    public boolean convertTo(Object value, Class<?> targetType, Type genericType, Out<Object> out) throws Throwable {
		if(Map.class.isAssignableFrom(targetType)){
			return out.returns(convertToMap(value));
		}
		return false;
    }

	protected Object convertFromMap(Class<?> targetType, Type genericType,Map map) {
		ReflectClass<?> reflectClass = ReflectClass.get(targetType);
		
		Object bean = reflectClass.newInstance();
		
        for(Object entryObject : map.entrySet()){
            Entry entry = (Entry)entryObject;
            
            Object key   = entry.getKey();
            Object param = entry.getValue();
            
            String name  = key.getClass().equals(String.class) ? (String)key : key.toString();
            
            ReflectField field = reflectClass.findField(name);
            
            if(null != field){
                field.setValue(bean, Converts.convert(field.getType(),field.getGenericType(),param));
            }
        }
		
		return bean;
	}

	protected Map<String,Object> convertToMap(Object bean) {
		
		ReflectClass<?> reflectClass = ReflectClass.get(bean.getClass());
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		for(ReflectField field : reflectClass.getFields()){
			if(field.isPublicGet()){
				map.put(field.getName(), field.getValue(bean));
			}
		}
		
		return map;
	}
}