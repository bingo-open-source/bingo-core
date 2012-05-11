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
package bingo.lang.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import bingo.lang.Arrays;
import bingo.lang.Primitives;
import bingo.lang.exceptions.ReflectException;

public class ReflectField extends ReflectMember {
	
	private final Field    javaField;
	private final Class<?> fieldType;
	
	private final Method setter;
	private final Method getter;
	
	private final int    fieldIndex;
	private final int    setterIndex;
	private final int    getterIndex;
	
	protected ReflectField(ReflectClass<?> reflectClass, Field javaField){
		super(reflectClass,javaField);
		
		this.javaField = javaField;
		this.fieldType = javaField.getType();
		this.setter    = findSetter();
		this.getter    = findGetter();
		
		this.fieldIndex  = reflectClass.getAccessor().getFieldIndex(javaField);
		this.setterIndex = null == setter ? -1 : reflectClass.getAccessor().getMethodIndex(setter);
		this.getterIndex = null == getter ? -1 : reflectClass.getAccessor().getMethodIndex(getter);
		
		this.create();
	}
	
	public String getName() {
	    return javaField.getName();
    }

	public Field getJavaField(){
		return javaField;
	}
	
    public Class<?> getType(){
        return fieldType;
    }
    
    public Type getGenericType(){
        return javaField.getGenericType();
    }
	
	public boolean isStatic(){
		return Modifier.isStatic(javaField.getModifiers());
	}	
	
	public boolean isFinal(){
		return Modifier.isFinal(javaField.getModifiers());
	}
	
	public boolean hasGetter(){
		return null != getter;
	}
	
	public boolean hasSetter(){
		return null != setter;
	}
	
	public boolean isPublicGet(){
		return Modifier.isPublic(javaField.getModifiers()) || (null != getter && Modifier.isPublic(getter.getModifiers())); 
	}
	
	public boolean isPublicSet(){
		return Modifier.isPublic(javaField.getModifiers()) || (null != setter && Modifier.isPublic(setter.getModifiers())); 
	}
	
	public boolean isPublicGetSet(){
		return Modifier.isPublic(javaField.getModifiers()) || (
				(null != setter && Modifier.isPublic(setter.getModifiers())) && (null != getter && Modifier.isPublic(getter.getModifiers()))); 
	}
	
	public void setValue(Object instance, Object value) {
        try {
            if(setterIndex != -1){
                reflectClass.getAccessor().invokeMethod(instance, setterIndex, safeValue(value));
            }else if(fieldIndex != 0){
            	reflectClass.getAccessor().setField(instance, fieldIndex, safeValue(value));
            }else{
                javaField.set(instance, safeValue(value));
            }
        } catch (Exception e) {
        	throw new ReflectException(e,"error setting value '{0}' to field '{1}'",value,getName());
        }	
	}
	
	public Object getValue(Object instance) {
        try {
            if(getterIndex != -1){
                return reflectClass.getAccessor().invokeMethod(instance, getterIndex, Arrays.EMPTY_OBJECT_ARRAY);
            }else if(fieldIndex != -1){
                return reflectClass.getAccessor().getField(instance, fieldIndex);
            }else {
                return javaField.get(instance);
            }
        } catch (Exception e) {
        	throw new ReflectException(e,"error getting value of field '{1}'",getName());
        }
	}
	
	private void create(){
		this.setAccessiable();
	}
	
	private Method findSetter(){
		String   fieldName  = javaField.getName();
		String   nameToFind = "set" + Character.toUpperCase(fieldName.charAt(0)) + (fieldName.length() > 1 ? fieldName.substring(1) : "");
		Class<?> fieldType  = Primitives.wrap(javaField.getType());
		
		Method m = findSetter(fieldType, nameToFind);
		
		if(null == m && Primitives.isBoolean(fieldType) && fieldName.startsWith("is") && fieldName.length() > 2){
			nameToFind = "set" + Character.toUpperCase(fieldName.charAt(2)) + (fieldName.length() > 3 ? fieldName.substring(3) : "");
			
			m = findSetter(fieldType,nameToFind);
		}
		
		if(null == m){
			m = findSetter(fieldType,fieldName);
		}
		
		return m;
	}
	
	private Method findSetter(Class<?> fieldType,String nameToFind){
		for(Method m : reflectClass.getJavaClass().getMethods()){
			if(m.getParameterTypes().length == 1 && 
					fieldType.isAssignableFrom(Primitives.wrap(m.getParameterTypes()[0]))){
				
				if(m.getName().equals(nameToFind)){
					return m;
				}
			}
		}
		return null;		
	}	
	
	private Method findGetter(){
		String   fieldName  = javaField.getName();
		String   nameToFind = "get" + Character.toUpperCase(fieldName.charAt(0)) + (fieldName.length() > 1 ? fieldName.substring(1) : "");
		Class<?> fieldType  = Primitives.wrap(javaField.getType());
		
		Method m = findGetter(fieldType,nameToFind);
		
		if(null == m && Primitives.isBoolean(fieldType)){
			if(fieldName.startsWith("is")){
				nameToFind = fieldName;
			}else{
				nameToFind = "is" + Character.toUpperCase(fieldName.charAt(0)) + (fieldName.length() > 1 ? fieldName.substring(1) : "");
			}
			
			m = findGetter(fieldType,nameToFind);
		}
		
		if(null == m){
			m = findGetter(fieldType,fieldName);
		}
		
		return m;
	}
	
	private Method findGetter(Class<?> fieldType,String nameToFind){
		for(Method m : reflectClass.getJavaClass().getMethods()){
			if(m.getParameterTypes().length == 0 && 
					Primitives.wrap(m.getReturnType()).isAssignableFrom(fieldType)){
				
				if(m.getName().equals(nameToFind)){
					return m;
				}
			}
		}
		return null;		
	}
	
	private void setAccessiable(){
		try {
			if(!javaField.isAccessible()){
				this.javaField.setAccessible(true);	
			}
        } catch (SecurityException e) {
        	;
        }
	}
	
    private Object safeValue(Object value){
        if(null == value){
        	return Primitives.defaultValue(fieldType);
        }
        return value;
    }
}