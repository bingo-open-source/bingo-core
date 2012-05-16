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
package bingo.utils.json;

import java.util.Date;
import java.util.Enumeration;
import java.util.IdentityHashMap;
import java.util.Map;

import bingo.lang.Enums;
import bingo.lang.Strings;
import bingo.lang.beans.BeanClass;
import bingo.lang.beans.BeanProperty;
import bingo.lang.reflect.ReflectClass;

class JSONEncoder {
	
	private static final Integer zero = new Integer(0);
	private static final int MAX_DEEP = 100;
    
    private JSONWriter 						 writer;
    private boolean   						 ignoreNull;
    private boolean   						 ignoreEmpty;
    private IdentityHashMap<Object,Integer> references = new IdentityHashMap<Object, Integer>();
    private int					 		 deep       = 0;
    
    public JSONEncoder(){
        this(new JSONSettings());
    }
    
    public JSONEncoder(JSONSettings settings){
        this.writer = new JSONWriter(settings);
        this.setting(settings);
    }
    
    private void setting(JSONSettings settings){
        this.ignoreNull  = settings.isIgnoreNull();
        this.ignoreEmpty = settings.isIgnoreEmpty();
    }
    
    public String encode(Object value){
        if (null == value) {
            return encodeNull();
        } else {
            StringBuilder out = new StringBuilder();
            encode(null,value, out);
            references.clear();
            return out.toString();
        }
    }

    private String encodeNull() {
        StringBuilder out = new StringBuilder();
        writer.writeNull(out);
        return out.toString();
    }

    private void encode(String name,Object value, StringBuilder out) {
    	deep++;
    	
    	if(deep >= MAX_DEEP){
    		throw new JSONException("stack size reach '{0}', please check your object , may be some getter generate new object at every call",deep);
    	}
    	
        if (null == value) {
            writer.writeNull(out);
        } else if (value instanceof String) {
            writer.writeString((String) value, out);
        } else if (value instanceof Byte) {
            writer.writeByte((Byte) value, out);
        } else if (value instanceof Number) {
            writer.writeNumber((Number) value, out);
        } else if (value instanceof Boolean) {
            writer.writeBoolean((Boolean) value, out);
        } else if (value instanceof Character) {
            writer.writeCharacter((Character) value, out);
        } else if (value instanceof Class<?>) {
            writer.writeString(((Class<?>) value).getName(), out);
        } else if (value instanceof Date) {
            writer.writeDate((Date) value, out);
        } else {
            //detect cyclic references
            if(references.containsKey(value)){
            	return;
            }
        	
        	references.put(value, zero);
        	
            if (value instanceof Object[]) {
                encode(name,(Object[]) value, out);
            } else if (value.getClass().isArray()) {
                encodeArray(name,value, out);
            } else if (value instanceof Map<?, ?>) {
                encode(name,(Map<?, ?>) value, out);
            } else if (value instanceof Iterable<?>) {
                encode(name,(Iterable<?>) value, out);
            } else if (value instanceof Enumeration<?>) {
                encode(name,(Enumeration<?>) value, out);
            } else if (value instanceof Enum<?>) {
                encode(name,Enums.getValue(((Enum<?>) value)), out);
            } else {
                encodeBean(name,value, out);
            }
        }
        
        deep--;
    }

    private void encode(String name,Object[] array, StringBuilder out) {
        writer.openArray(out);
        for (int i = 0; i < array.length; i++) {
        	Object value = array[i];
        	
            //detect cyclic references
            if(references.containsKey(value)){
            	continue;
            }
            
            if (i > 0) {
                writer.writeArrayValueSeperator(out);
            }
            
            encode(name,array[i], out);
        }
        writer.closeArray(out);
    }

    private void encodeArray(String name,Object array, StringBuilder out) {
    	ReflectClass<?> reflectClass = ReflectClass.get(array.getClass().getComponentType());
    	
        writer.openArray(out);
        
        int len = reflectClass.getArrayLength(array);
        for (int i = 0; i < len; i++) {
        	Object value = reflectClass.getArrayItem(array, i);
        	
            //detect cyclic references
            if(references.containsKey(value)){
            	continue;
            }
            
            if (i > 0) {
                writer.writeArrayValueSeperator(out);
            }
            
            encode(name,value, out);
        }
        writer.closeArray(out);
    }
    
    private void encode(String name,Iterable<?> iterable, StringBuilder out) {
        writer.openArray(out);
        int index = 0;
        for (Object value : iterable) {
            //detect cyclic references
            if(references.containsKey(value)){
            	continue;
            }
            
            if (index == 0) {
                index++;
            } else {
                writer.writeArrayValueSeperator(out);
            }
            
            encode(name,value, out);
        }
        writer.closeArray(out);
    }

    private void encode(String name,Enumeration<?> enumeration, StringBuilder out) {
        writer.openArray(out);
        
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
                writer.writeArrayValueSeperator(out);
            }
            
            encode(name,value, out);
        }
        writer.closeArray(out);
    }

    private void encode(String name,Map<?, ?> map, StringBuilder out) {
        writer.openObject(out);

        int index = 0;
        for (Object key : map.keySet()) {
            String prop = String.valueOf(key);
            Object propValue = map.get(key);
            
            if(null == propValue && ignoreNull){
                continue;
            }
            
            if(ignoreEmpty && (propValue instanceof String) && ((String)propValue).trim().equals("")){
                continue;
            }
            
            //detect cyclic references
            if(references.containsKey(propValue)){
            	continue;
            }            
            
            if (index == 0) {
                index++;
            } else {
                writer.writePropertyValueSeperator(out);
            }

            encodeNamedValue(prop, map.get(key), out);
        }

        writer.closeObject(out);
    }

    private void encodeBean(String name,Object bean, StringBuilder out) {
        Class<?> clazz = bean.getClass();
        writer.openObject(out);
        
        try {
            BeanClass<?> beanClass = BeanClass.get(clazz);

            int index = 0;
            for(BeanProperty prop : beanClass.getProperties()){
                if(prop.isReadable() && prop.isField() && !prop.isAnnotationPresent(JSONIgnore.class) && !prop.isTransient()){
                    String propName = prop.getName();
                    
                    Object propValue = prop.getValue(bean);
                    
                    if(null == propValue && ignoreNull){
                        continue;
                    }
                    
                    if(ignoreEmpty && Strings.isBlank(propValue)){
                        continue;
                    }
                    
                    //detect cyclic references
                    if(references.containsKey(propValue)){
                    	continue;
                    }                    
                    
                    if (index == 0) {
                        index++;
                    } else {
                        writer.writePropertyValueSeperator(out);
                    }

                    encodeNamedValue(propName, propValue, out);
                }
            }
        } catch (JSONException e){
        	throw e;
        } catch (Exception e) {
            throw new JSONException("error encoding for value : " + bean.getClass().getName(), e);
        }
        
        writer.closeObject(out);
    }

    private void encodeNamedValue(String name, Object value, StringBuilder out) {
        writer.openName(out);
        writer.writeName(name, out);
        writer.closeName(out);

        writer.openValue(name, out);
        encode(name,value, out);
        writer.closeValue(name, out);
    }
}