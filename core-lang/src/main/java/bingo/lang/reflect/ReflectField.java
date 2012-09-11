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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import bingo.lang.Arrays;
import bingo.lang.Classes;
import bingo.lang.Primitives;
import bingo.lang.Strings;
import bingo.lang.exceptions.ReflectException;

public class ReflectField extends ReflectMember {
	
	private final Field    javaField;
	private final Class<?> fieldType;
	
	private final ReflectMethod setter;
	private final ReflectMethod getter;
	
	private final int    fieldIndex;
	private final int    setterIndex;
	private final int    getterIndex;
	
	private final ReflectAccessor accessor;
	
	protected ReflectField(ReflectClass<?> reflectClass, Field javaField){
		super(reflectClass,javaField);
		
		this.javaField = javaField;
		this.fieldType = javaField.getType();
		this.setter    = findSetter();
		this.getter    = findGetter();
		this.accessor  = reflectClass.getAccessor();
		
		this.fieldIndex  = accessor.getFieldIndex(javaField);
		this.setterIndex = null == setter ? -1 : accessor.getMethodIndex(setter.getJavaMethod());
		this.getterIndex = null == getter ? -1 : accessor.getMethodIndex(getter.getJavaMethod());
		
		this.initialize();
	}
	
	public String getName() {
	    return javaField.getName();
    }

    public Class<?> getType(){
        return fieldType;
    }
    
    public Type getGenericType(){
        return javaField.getGenericType();
    }
    
	public Field getJavaField(){
		return javaField;
	}
	
	public Class<?> getDeclaringClass(){
		return javaField.getDeclaringClass();
	}
	
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationType){
		return javaField.isAnnotationPresent(annotationType);
	}
	
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		return javaField.getAnnotation(annotationType);
	}
	
	public boolean isSynthetic(){
		return javaField.isSynthetic();
	}
	
	public boolean isStatic(){
		return Modifier.isStatic(javaField.getModifiers());
	}	
	
	public boolean isFinal(){
		return Modifier.isFinal(javaField.getModifiers());
	}
	
	public boolean isTransient(){
		return Modifier.isTransient(javaField.getModifiers());
	}
	
	public boolean hasGetter(){
		return null != getter;
	}
	
	public boolean hasSetter(){
		return null != setter;
	}
	
	public ReflectMethod getGetter(){
		return getter;
	}
	
	public ReflectMethod getSetter(){
		return setter;
	}
	
	public boolean isPublicGet(){
		return Modifier.isPublic(javaField.getModifiers()) || (null != getter && getter.isPublic()); 
	}
	
	public boolean isPublicSet(){
		return Modifier.isPublic(javaField.getModifiers()) || (null != setter && setter.isPublic()); 
	}
	
	public boolean isPublicGetSet(){
		return Modifier.isPublic(javaField.getModifiers()) || (
				(null != setter && setter.isPublic()) && (null != getter && getter.isPublic())); 
	}
	
	public void setValue(Object instance, Object value) {
		setValue(instance,value,false);
	}
	
	public void setValue(Object instance, Object value, boolean useSetter) {
        try {
        	if(javaField.isSynthetic() || isFinal()){
        		javaField.set(instance, safeValue(value));
        	}else{
            	if(useSetter && null != setter){
            		if(setterIndex != -1){
            			accessor.invokeMethod(instance, setterIndex, safeValue(value));
            		}else{
            			setter.invoke(instance, safeValue(value));
            		}
            	}else{
            		if(fieldIndex != -1){
                    	accessor.setField(instance, fieldIndex, safeValue(value));
                    }else{
                        javaField.set(instance, safeValue(value));
                    }
            	}
        	}
        } catch (Exception e) {
        	throw new ReflectException("error setting value '{0}' to field '{1}'",value,getName(),e);
        }
	}
	
	public Object getValue(Object instance) {
		return getValue(instance,false);
	}
	
	public Object getValue(Object instance,boolean useGetter) {
        try {
        	if(useGetter && null != getter){
        		if(getterIndex != -1){
        			return accessor.invokeMethod(instance, getterIndex, Arrays.EMPTY_OBJECT_ARRAY);
        		}else{
        			return getter.invoke(instance, Arrays.EMPTY_OBJECT_ARRAY);
        		}
        	}else{
        		if(fieldIndex != -1){
                    return accessor.getField(instance, fieldIndex);
                }else {
                    return javaField.get(instance);
                }
        	}
        } catch (Exception e) {
        	throw new ReflectException("error getting value of field '{1}'",getName(),e);
        }
	}
	
	private void initialize(){
		this.setAccessiable();
	}
	
	private ReflectMethod findSetter(){
		String   fieldName  = javaField.getName().startsWith("_") ? javaField.getName().substring(1) : javaField.getName();
		String   nameToFind = "set" + Character.toUpperCase(fieldName.charAt(0)) + (fieldName.length() > 1 ? fieldName.substring(1) : "");
		Class<?> fieldType  = Primitives.wrap(javaField.getType());
		
		ReflectMethod m = findSetter(fieldType, nameToFind);
		
		if(null == m && Classes.isBoolean(fieldType) && fieldName.startsWith("is") && fieldName.length() > 2){
			nameToFind = "set" + Character.toUpperCase(fieldName.charAt(2)) + (fieldName.length() > 3 ? fieldName.substring(3) : "");
			
			m = findSetter(fieldType,nameToFind);
		}
		
		if(null == m){
			m = findSetter(fieldType,fieldName);
		}
		
		return m;
	}
	
	private ReflectMethod findSetter(Class<?> fieldType,String nameToFind){
		//iterate all public methods.
		for(ReflectMethod rm : reflectClass.getMethods()){
			Method m = rm.getJavaMethod();
			if(m.getParameterTypes().length == 1 && 
					fieldType.isAssignableFrom(Primitives.wrap(m.getParameterTypes()[0]))){
				
				if(m.getName().equals(nameToFind)){
					return rm;
				}
			}
		}
		return null;		
	}
	
	private ReflectMethod findGetter(){
		String   fieldName  = javaField.getName().startsWith("_") ? javaField.getName().substring(1) : javaField.getName();
		String   nameToFind = "get" + Character.toUpperCase(fieldName.charAt(0)) + (fieldName.length() > 1 ? fieldName.substring(1) : "");
		Class<?> fieldType  = Primitives.wrap(javaField.getType());
		
		ReflectMethod m = findGetter(fieldType,nameToFind);
		
		if(null == m && Classes.isBoolean(fieldType)){
			if(fieldName.startsWith("is") && fieldName.length() > 2){
				if(Boolean.class.equals(javaField.getType())){
					nameToFind = "get" + Strings.upperFirst(fieldName.substring(2));
					
					if(null != (m = findGetter(fieldType, nameToFind))){
						return m;
					}
				}
				
				nameToFind = fieldName;
			}else{
				nameToFind = "is" + Character.toUpperCase(fieldName.charAt(0)) + (fieldName.length() > 1 ? fieldName.substring(1) : "");
			}
			
			m = findGetter(fieldType,nameToFind);
		}

		return m;
	}
	
	private ReflectMethod findGetter(Class<?> fieldType,String nameToFind){
		for(ReflectMethod rm : reflectClass.getMethods()){
			Method m = rm.getJavaMethod();
			if(m.getParameterTypes().length == 0 && 
					Primitives.wrap(m.getReturnType()).isAssignableFrom(fieldType)){
				
				if(m.getName().equals(nameToFind)){
					return rm;
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
    
	@Override
    public String toString() {
		return javaField.toString();
    }
}
