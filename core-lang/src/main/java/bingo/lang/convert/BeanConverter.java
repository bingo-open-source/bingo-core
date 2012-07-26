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
package bingo.lang.convert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import bingo.lang.Classes;
import bingo.lang.Converts;
import bingo.lang.Out;
import bingo.lang.annotations.NamedAnnotation;
import bingo.lang.beans.BeanModel;
import bingo.lang.beans.BeanProperty;
import bingo.lang.reflect.ReflectClass;


@SuppressWarnings({"rawtypes"})
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
		BeanModel<?> beanClass = BeanModel.get(targetType);
		
		Object bean = beanClass.newInstance();
		
		for(BeanProperty prop : beanClass.getProperties()){
			String name = prop.getName();
			
			for(Annotation a : prop.getAnnotations()){
				NamedAnnotation namedAnnotation = a.annotationType().getAnnotation(NamedAnnotation.class);
				
				if(null != namedAnnotation){
					name = (String)ReflectClass.get(a.getClass()).getMethod(namedAnnotation.value()).invoke(a);
				}
			}
			
	        for(Object entryObject : map.entrySet()){
	            Entry entry = (Entry)entryObject;
	            
	            if(entry.getKey().equals(name)){
		            Object param = entry.getValue();
		            
		            if(null != prop && prop.isWritable()){
		                prop.setValue(bean, Converts.convert(param,prop.getType(),prop.getGenericType()));
		                break;
		            }
	            }
	        }
		}
		
		return bean;
	}

	protected Map<String,Object> convertToMap(Object bean) {
		BeanModel<?> beanClass = BeanModel.get(bean.getClass());
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		for(BeanProperty prop : beanClass.getProperties()){
			if(prop.isReadable()){
				map.put(prop.getName(), prop.getValue(bean));
			}
		}
		
		return map;
	}
}