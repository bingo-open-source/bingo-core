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
package bingo.lang.json;

import java.util.Date;
import java.util.Enumeration;
import java.util.IdentityHashMap;
import java.util.Map;

import bingo.lang.Enums;
import bingo.lang.Strings;
import bingo.lang.reflect.ReflectClass;
import bingo.lang.reflect.ReflectField;

class JSONEncoder {
	
	private static final Integer zero = new Integer(0);
	private static final int MAX_DEEP = 100;
    
    private IdentityHashMap<Object,Integer> references = new IdentityHashMap<Object, Integer>();
    private int					 		 deep       = 0;
    private JSONSettings				     settings ;
    
    public JSONEncoder(){
        this(new JSONSettings());
    }
    
    public JSONEncoder(JSONSettings settings){
    	this.settings = settings;
    }
    	
    public String encode(Object value){
        if (null == value) {
            return encodeNull();
        } else {
        	JSONWriterImpl writer = new JSONWriterImpl(new StringBuilder(128),settings.isKeyQuoted(),settings.isIgnoreNull());
        	
            encode(null,value, writer);
            
            references.clear();
            
            return writer.toString();
        }
    }

    private String encodeNull() {
    	return JSONWriterImpl.NULL_STRING;
    }

    private void encode(String name,Object value, JSONWriterImpl writer) {
    	deep++;
    	
    	if(deep >= MAX_DEEP){
    		throw new JSONException("stack size reach '{0}', please check your object , may be some getter generate new object at every call",deep);
    	}
    	
        if (null == value) {
            writer.nullValue();
        } else if (value instanceof String) {
            writer.value((String) value);
        } else if (value instanceof Byte) {
            writer.value(((Byte) value).byteValue());
        } else if (value instanceof Boolean) {
            writer.value(((Boolean) value).booleanValue());
        } else if (value instanceof Character) {
            writer.value(((Character) value).charValue());
        } else if (value instanceof Number) {
            writer.value((Number) value);
        } else if (value instanceof Date) {
            writer.value((Date) value);
        } else if (value instanceof Class<?>) {
            writer.value(((Class<?>) value).getName());
        } else {
            //detect cyclic references
            if(references.containsKey(value)){
            	return;
            }
        	
        	references.put(value, zero);
        	
            if (value instanceof Object[]) {
                encode(name,(Object[]) value, writer);
            } else if (value.getClass().isArray()) {
                encodeArray(name,value, writer);
            } else if (value instanceof Map<?, ?>) {
                encode(name,(Map<?, ?>) value, writer);
            } else if (value instanceof Iterable<?>) {
                encode(name,(Iterable<?>) value, writer);
            } else if (value instanceof Enumeration<?>) {
                encode(name,(Enumeration<?>) value, writer);
            } else if (value instanceof Enum<?>) {
                encode(name,Enums.getValue(((Enum<?>) value)), writer);
            } else {
                encodeBean(name,value, writer);
            }
        }
        
        deep--;
    }

    private void encode(String name,Object[] array, JSONWriterImpl writer) {
        writer.startArray();
        for (int i = 0; i < array.length; i++) {
        	Object value = array[i];
        	
            //detect cyclic references
            if(references.containsKey(value)){
            	continue;
            }
            
            if (i > 0) {
                writer.separator();
            }
            
            encode(name,array[i], writer);
        }
        writer.endArray();
    }

    private void encodeArray(String name,Object array, JSONWriterImpl writer) {
    	ReflectClass<?> reflectClass = ReflectClass.get(array.getClass().getComponentType());
    	
        writer.startArray();
        
        int len = reflectClass.getArrayLength(array);
        for (int i = 0; i < len; i++) {
        	Object value = reflectClass.getArrayItem(array, i);
        	
            //detect cyclic references
            if(references.containsKey(value)){
            	continue;
            }
            
            if (i > 0) {
                writer.separator();
            }
            
            encode(name,value, writer);
        }
        
        writer.endArray();
    }
    
    private void encode(String name,Iterable<?> iterable, JSONWriterImpl writer) {
        writer.startArray();
        
        int index = 0;
        for (Object value : iterable) {
            //detect cyclic references
            if(references.containsKey(value)){
            	continue;
            }
            
            if (index == 0) {
                index++;
            } else {
                writer.separator();
            }
            
            encode(name,value, writer);
        }
        
        writer.endArray();
    }

    private void encode(String name,Enumeration<?> enumeration, JSONWriterImpl writer) {
        writer.startArray();
        
        int index = 0;
        while (enumeration.hasMoreElements()) {
        	
        	Object value = enumeration.nextElement();
        	
            //detect cyclic references
            if(references.containsKey(value)){
            	continue;
            }
        	
            if (index == 0) {
                index++;
            } else {
                writer.separator();
            }
            
            encode(name,value, writer);
        }
        writer.endArray();
    }

    private void encode(String name,Map<?, ?> map, JSONWriterImpl writer) {
        writer.startObject();

        int index = 0;
        for (Object key : map.keySet()) {
            String prop = String.valueOf(key);
            Object propValue = map.get(key);
            
            if(null == propValue && settings.isIgnoreNull()){
                continue;
            }
            
            if(settings.isIgnoreEmpty() && (propValue instanceof String) && ((String)propValue).trim().equals("")){
                continue;
            }
            
            //detect cyclic references
            if(references.containsKey(propValue)){
            	continue;
            }            
            
            if (index == 0) {
                index++;
            } else {
                writer.separator();
            }

            encodeNamedValue(prop, map.get(key), writer);
        }

        writer.endObject();
    }

    private void encodeBean(String name,Object bean, JSONWriterImpl writer) {
        Class<?> clazz = bean.getClass();
        writer.startObject();
        
        try {
            ReflectClass<?> beanClass = ReflectClass.get(clazz);

            int index = 0;
            for(ReflectField prop : beanClass.getFields()){
            	if(prop.isStatic() || prop.isSynthetic()){
            		continue;
            	}
            	JSONField jsonField = prop.getAnnotation(JSONField.class);
            	
                if(null != jsonField || ((!prop.isAnnotationPresent(JSONIgnore.class) && !prop.isTransient()) && (prop.isPublic() || prop.hasGetter()))){
                    String propName = prop.getName();
                    
                    JSONNamed named = prop.getAnnotation(JSONNamed.class);
                    
                    if(null != named){
                    	propName = named.value();
                    }
                    
                    Object propValue = prop.getValue(bean);
                    
                    if(null == propValue && settings.isIgnoreNull()){
                        continue;
                    }
                    
                    if(settings.isIgnoreEmpty() && Strings.isBlank(propValue)){
                        continue;
                    }
                    
                    //detect cyclic references
                    if(references.containsKey(propValue)){
                    	continue;
                    }                    
                    
                    if (index == 0) {
                        index++;
                    } else {
                        writer.separator();
                    }

                    encodeNamedValue(propName, propValue, writer);
                }
            }
        } catch (JSONException e){
        	throw e;
        } catch (Exception e) {
            throw new JSONException("error encoding for value : " + bean.getClass().getName(), e);
        }
        
        writer.endObject();
    }

    private void encodeNamedValue(String name, Object value, JSONWriterImpl writer) {
        writer.name(name);
        encode(name,value, writer);
    }
}