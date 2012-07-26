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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import bingo.lang.Arrays;
import bingo.lang.Assert;
import bingo.lang.Collections;
import bingo.lang.Enumerables;
import bingo.lang.Predicate;
import bingo.lang.Predicates;
import bingo.lang.reflect.ReflectClass;
import bingo.lang.reflect.ReflectField;
import bingo.lang.reflect.ReflectMethod;

public class BeanModel<T> {
	
	private static final Map<Class<?>, BeanModel<?>> cache = 
		java.util.Collections.synchronizedMap(new WeakHashMap<Class<?>, BeanModel<?>>());
	
	@SuppressWarnings("unchecked")
	public static <T> BeanModel<T> get(T bean){
		Assert.notNull(bean);
		
		return (BeanModel<T>)get(bean.getClass());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> BeanModel<T> get(Class<T> type) {
		BeanModel<?> clazz = cache.get(type);
		
		if(null == clazz){
			clazz = new BeanModel<T>(type);
			
			cache.put(type, clazz);
		}
		
		return (BeanModel<T>)clazz;
	}

	public static final String SETTER_PREFIX = "set";
	public static final String GETTER_PREFIX = "get";
	public static final String IS_PREFIX     = "is";
	
	private ReflectClass<T> clazz;
	private BeanProperty[]  properties;
	
	protected BeanModel(Class<T> javaClass){
		this.clazz = ReflectClass.get(javaClass);
		this.initialize();
	}
	
	public T newInstance(){
		return clazz.newInstance();
	}
	
	public Map<String, Object> toMap(Object bean){
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		for(BeanProperty p : properties){
			if(p.isReadable()){
				map.put(p.getName(),p.getValue(bean));
			}
		}
		
		return map;
	}
	
	public Map<String, Object> toMap(Object bean,Predicate<BeanProperty> filter){
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		for(BeanProperty p : properties){
			if(p.isReadable() && filter.apply(p)){
				map.put(p.getName(),p.getValue(bean));
			}
		}
		
		return map;
	}
	
	public boolean set(Object bean,String property,Object value){
		BeanProperty prop = getProperty(property);
		
		if(null != prop){
			prop.setValue(bean, value);
			return true;
		}
		
		return false;
	}
	
	public Object get(Object bean,String property) {
		BeanProperty prop = getProperty(property);
		
		return null == prop ? null : prop.getValue(bean);
	}
	
	public Class<?> getJavaClass(){
		return clazz.getJavaClass();
	}
	
	public BeanProperty[] getProperties(){
		return properties;
	}
	
	public BeanProperty getProperty(String name){
		return Enumerables.firstOrNull(properties,Predicates.<BeanProperty>nameEquals(name));
	}
	
	public BeanProperty getPropertyIgnorecase(String name){
		return Enumerables.firstOrNull(properties,Predicates.<BeanProperty>nameEqualsIgnoreCase(name));
	}

	private void initialize(){
		
		Map<String, BeanProperty> props = new LinkedHashMap<String, BeanProperty>();
		
		Set<Method> methods = new HashSet<Method>();
		
		//from fields
		for(ReflectField field : clazz.getFields()){
			
			if(field.isStatic()){
				continue;
			}
			
			if(field.isPublic() || field.hasGetter() || field.hasSetter()){
			
				String name = field.getName().startsWith("_") ? field.getName().substring(1) : field.getName();
				
				if(props.containsKey(name)){
					continue;
				}
				
				BeanProperty prop = new BeanProperty(this,name);
				
				prop.setType(field.getType());
				prop.setGenericType(field.getGenericType());
				
				prop.setField(field);

				prop.setGetter(field.getGetter());
				prop.setReadable(field.isPublicGet());

				prop.setSetter(field.getSetter());
				prop.setWritable(field.isPublicSet());
				prop.setTransient(field.isTransient());
				prop.setAnnotations(field.getJavaField().getAnnotations());
				
				if(prop.hasGetter()){
					methods.add(prop.getGetter());
				}
				
				if(prop.hasSetter()){
					methods.add(prop.getSetter());
				}
				
				props.put(name, prop);
			}
		}
		
		//from methods
		for(ReflectMethod rm : clazz.getMethods()){
			Method m = rm.getJavaMethod();
			
			if(rm.isStatic()){
				continue;
			}
			
			if(!methods.contains(m)){
				String methodName = rm.getName();
				
				if(methodName.startsWith(SETTER_PREFIX) && methodName.length() > SETTER_PREFIX.length()){
					if(m.getReturnType().equals(void.class) && m.getParameterTypes().length == 1){
						BeanProperty prop = getOrCreatePropertyFor(props,methodName,SETTER_PREFIX,m.getParameterTypes()[0]);
						
						if(null != prop){
							prop.setSetter(rm);
							prop.setWritable(rm.isPublic());
							
							if(null == prop.getGenericType()){
								prop.setGenericType(m.getGenericParameterTypes()[0]);	
								//TODO : duplicate annotations ?
								prop.setAnnotations(Arrays.concat(prop.getAnnotations(), m.getAnnotations()));
							}
						}
						
					}
				}else if(methodName.startsWith(GETTER_PREFIX) && methodName.length() > GETTER_PREFIX.length()){
					
					if(!m.getReturnType().equals(void.class) && m.getParameterTypes().length == 0) {
						
						BeanProperty prop = getOrCreatePropertyFor(props, methodName, GETTER_PREFIX, m.getReturnType());

						if(null != prop){
							prop.setGetter(rm);
							prop.setReadable(rm.isPublic());
							if(null == prop.getGenericType()){
								prop.setGenericType(m.getGenericReturnType());	
								prop.setAnnotations(Arrays.concat(prop.getAnnotations(), m.getAnnotations()));
							}							
						}
					}
					
				}else if(methodName.startsWith(IS_PREFIX) && methodName.length() > IS_PREFIX.length()){

					if((m.getReturnType().equals(Boolean.class) || m.getReturnType().equals(Boolean.class)) && m.getParameterTypes().length == 0){
						
						BeanProperty prop = getOrCreatePropertyFor(props, methodName, IS_PREFIX, m.getReturnType());
						
						if(null != prop && !prop.hasGetter()){
							prop.setGetter(rm);
							prop.setReadable(rm.isPublic());
							if(null == prop.getGenericType()){
								prop.setGenericType(m.getGenericReturnType());	
								prop.setAnnotations(Arrays.concat(prop.getAnnotations(), m.getAnnotations()));
							}
						}
					}
				}
			}
		}
		
		this.properties = Collections.toArray(props.values(),BeanProperty.class);
	}
	
	private BeanProperty getOrCreatePropertyFor(Map<String, BeanProperty> props, String methodName,String prefix,Class<?> type){
		String propName = methodName.substring(prefix.length());
		
		char c = propName.charAt(0);
		if(Character.isUpperCase(c)){
			propName = Character.toLowerCase(c) + propName.substring(1);
			
			BeanProperty prop = props .get(propName);
			
			if(null == prop){
				prop = new BeanProperty(this, propName);
				
				prop.setType(type);
				props.put(propName, prop);
			}else if(!type.equals(prop.getType())){
				return null;
			}
			
			return prop; 
		}
		
		return null;
	}

	@Override
    public String toString() {
	    return "BeanClass:" + clazz.getJavaClass().getName();
    }
}
