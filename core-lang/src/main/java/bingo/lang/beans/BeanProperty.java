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
package bingo.lang.beans;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import bingo.lang.Converts;
import bingo.lang.Named;
import bingo.lang.exceptions.ConvertUnsupportedException;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.lang.reflect.ReflectField;
import bingo.lang.reflect.ReflectMethod;

public class BeanProperty implements Named {
	private static final Log log = LogFactory.get(BeanProperty.class);
	
	private String   	   name;
	private String         traditionalName;
	private Class<?> 	   type;
	private ReflectField   field;
	private ReflectMethod  getter;
	private ReflectMethod  setter;
	private BeanModel<?>   beanClass;
	private boolean	   	   readable;
	private boolean        writable;
	private boolean        _transient;
	private Type           genericType;
	private Annotation[]   annotations = new Annotation[]{};
	
	protected BeanProperty(BeanModel<?> beanClass,String name,String traditionalName){
		this.name      		 = name;
		this.traditionalName = traditionalName;
		this.beanClass 		 = beanClass;
	}

	public String getName() {
	    return name;
    }
	
	public String getTraditionalName() {
		return traditionalName;
	}

	public Class<?> getType(){
		return type;
	}
	
	public Type getGenericType(){
		return genericType;
	}
	
	public BeanModel<?> getBeanClass(){
		return beanClass;
	}
	
	public Annotation[] getAnnotations(){
		return annotations;
	}
	
	public boolean isReadable(){
		return readable;
	}
	
	public boolean isWritable(){
		return writable;
	}
	
	public boolean isTransient(){
		return _transient;
	}
	
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationType){
		return getAnnotation(annotationType) != null;
	}
	
	public boolean isField(){
		return null != field;
	}
	
	public boolean hasGetter(){
		return null != getter;
	}
	
	public boolean hasSetter(){
		return null != setter;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		for(Annotation a : annotations){
			if(a.annotationType().equals(annotationType)){
				return (T)a;
			}
		}
		return null;
	}
	
	public Field getField(){
		return null != field ? field.getJavaField() : null;
	}
	
	public ReflectField getReflectField(){
		return field;
	}
	
	public Method getSetter(){
		return null != setter ? setter.getJavaMethod() : null;
	}
	
	public Method getGetter(){
		return null != getter ? getter.getJavaMethod() : null;
	}
	
	public Object getValue(Object bean) {
		if(!readable){
			throw new IllegalStateException("Property '" + name + "' of '" + beanClass.getJavaClass().getName() + "' not readable");
		}
		
		return null != getter ? getter.invoke(bean) : field.getValue(bean,true);
	}
	
	public void setValue(Object bean,Object value){
		if(!writable){
			throw new IllegalStateException("Property '" + name + "' of '" + beanClass.getJavaClass().getName() + "' not writable");
		}
		
		if(null != value && !type.isAssignableFrom(value.getClass())){
			value = Converts.convert(value, type,genericType);
		}
		
		if(null != setter){
			setter.invoke(bean,value);
		}else{
			field.setValue(bean, value, true);
		}
	}
	
	public boolean trySetValue(Object bean,Object value) {
		if(writable){
			try {
	            if(null != value && !type.isAssignableFrom(value.getClass())){
	            	value = Converts.convert(value, type,genericType);
	            }
	            
	    		if(null != setter){
	    			setter.invoke(bean,value);
	    		}else{
	    			field.setValue(bean, value, true);
	    		}
	    		
	    		return true;
            } catch (ConvertUnsupportedException e) {
            	log.debug("cannot set property '{}' of bean '{}' -> {}",name,beanClass.getJavaClass().getSimpleName(),e.getMessage());
            }
		}
		return false;
	}
	
	protected void setType(Class<?> type){
		this.type = type;
	}
	
	protected void setGenericType(Type genericType){
		this.genericType = genericType;
	}
	
	protected void setField(ReflectField field){
		this.field = field;
	}
	
	protected void setSetter(ReflectMethod setter){
		this.setter = setter;
	}
	
	protected void setGetter(ReflectMethod getter){
		this.getter = getter;
	}
	
	protected void setReadable(boolean readable){
		this.readable = readable;
	}
	
	protected void setWritable(boolean writable){
		this.writable = writable;
	}
	
	protected void setTransient(boolean isTransient){
		this._transient = isTransient;
	}
	
	protected void setAnnotations(Annotation[] annotations){
		this.annotations = annotations;
	}

	@Override
    public String toString() {
		return "BeanProperty:" + beanClass.getJavaClass().getName() + "#" + name;
    }
}
