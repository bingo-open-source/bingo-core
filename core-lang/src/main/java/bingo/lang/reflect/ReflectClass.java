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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import bingo.lang.Collections;
import bingo.lang.Named;
import bingo.lang.New;
import bingo.lang.Predicate;
import bingo.lang.exceptions.ReflectException;

public class ReflectClass<T> implements Named {
	
	public static <T> ReflectClass<T> forType(Class<T> type) {
		return null;
	}
	
	public static <T> ReflectClass<T> forName(String name) {
		return null;
	}	
	
	private final Class<T>	         javaClass;
	private final ReflectAccessor   accessor;
	
	private ReflectConstructor<T>[]	 constructors;
	private ReflectField<T>[]		 fields;
	private ReflectMethod<T>[]		 methods;
	
	private ReflectConstructor<T>[] declaredConstructors;
	private ReflectField<T>[]		 declaredFields;
	private ReflectMethod<T>[]		 declaredMethods;	
	
	private ReflectConstructor<T>	 defaultConstructor;	
	
	protected ReflectClass(Class<T> javaClass){
		this.javaClass = javaClass;
		this.accessor  = ReflectAccessor.createFor(this.javaClass);
		this.create();
	}
	
	public String getName() {
	    return javaClass.getName();
    }

	public T newInstance(){
		if(null == defaultConstructor){
			throw new ReflectException("there is no default constructor available in class '{0}'",getName());
		}
		return defaultConstructor.newInstance();
	}
	
	public Class<T> getJavaClass() {
    	return javaClass;
    }
	
	public ReflectConstructor<T>[] getConstructors(){
		return constructors;
	}
	
	public ReflectConstructor<T>[] getDeclaredConstructors(){
		return declaredConstructors;
	}
	
	public ReflectConstructor<T> getConstructor(Class<?>... argumentTypes){
		return null;
	}
	
	public ReflectField<T>[] getFields() {
		return fields;
	}
	
	public ReflectField<T>[] getDeclaredFields(){
		return declaredFields;
	}
	
	public ReflectField<T> getField(final String name){
		return Collections.firstOrNull(fields, new Predicate<ReflectField<T>>() {
			public boolean evaluate(ReflectField<T> object) {
	            return object.getName().equals(name);
            }
		});
	}
	
	public ReflectField<T> getField(final String name,final Class<?> fieldType){
		return Collections.firstOrNull(fields, new Predicate<ReflectField<T>>() {
			public boolean evaluate(ReflectField<T> object) {
	            return object.getName().equals(name) && object.getJavaField().getType().equals(fieldType);
            }
		});
	}
	
	public ReflectField<T> findField(final String name){
		return Collections.firstOrNull(fields, new Predicate<ReflectField<T>>() {
			public boolean evaluate(ReflectField<T> object) {
	            return object.getName().equalsIgnoreCase(name);
            }
		});
	}
	
	public ReflectMethod<T>[] getMethods(){
		return methods;
	}
	
	public ReflectMethod<T>[] getDeclaredMethods(){
		return declaredMethods;
	}
	
	public ReflectMethod<T> getMethod(final String name,final Class<?>... argumentTypes){
		return Collections.firstOrNull(methods, new Predicate<ReflectMethod<T>>() {
			public boolean evaluate(ReflectMethod<T> object) {
	            if(object.getName().equals(name)){
	            	Method javaMethod = object.getJavaMethod();
	            	
	            	if(javaMethod.getParameterTypes().length == argumentTypes.length){
                        boolean matched = true;
                        
                        for(int i=0;i<javaMethod.getParameterTypes().length;i++){
                            if(!javaMethod.getParameterTypes()[i].equals(argumentTypes[i])){
                                matched = false;
                                break;
                            }
                        }
                        
                        if(matched){
                            return true;    
                        }
	            	}
	            }
	            return false;
            }
		});
	}
	
	public ReflectMethod<T> getMethod(String name,Class<?> returnType,Class<?>... argumentTypes){
		ReflectMethod<T> m = getMethod(name, argumentTypes);
		
		return null == m ? null : (m.getJavaMethod().getReturnType().equals(returnType) ? m : null);
	}	
	
	@SuppressWarnings("unchecked")
	public ReflectMethod<T>[] getMethods(final String name) {
		return Collections.where(methods, new Predicate<ReflectMethod<T>>() {
			public boolean evaluate(ReflectMethod<T> object) {
	            return object.getName().equals(name);
            }
		}).toArray(new ReflectMethod[]{});
	}
	
	ReflectAccessor getAccessor(){
		return accessor;
	}
	
	private void create(){
		//constructors
		this.createConstructors();
		
		//fields
		this.createFields();
		
		//methods
		this.createMethods();
	}
	
	@SuppressWarnings("unchecked")
	private void createConstructors(){
		List<ReflectConstructor<T>> constructorList = New.list();
		
		for(Constructor<T> c : javaClass.getConstructors()) {
			if(!c.isSynthetic()){
				ReflectConstructor<T> rc = new ReflectConstructor<T>(this,c);
				
				constructorList.add(rc);	
				
				if(c.getParameterTypes().length == 0){
					defaultConstructor = rc;
				}				
			}
		}
		
		this.constructors = constructorList.toArray(new ReflectConstructor[constructorList.size()]);
		this.declaredConstructors = getDeclaredMembers(constructorList).toArray(new ReflectConstructor[]{});
	}
	
	@SuppressWarnings("unchecked")
	private void createFields(){
		List<ReflectField<T>> fieldList = New.list();
		
		for(Field f : javaClass.getFields()){
			if(!f.isSynthetic()){
				fieldList.add(new ReflectField(this,f));
			}
		}
		
		this.fields = fieldList.toArray(new ReflectField[fieldList.size()]);
		this.declaredFields = getDeclaredMembers(fieldList).toArray(new ReflectField[]{});
	}
	
	@SuppressWarnings("unchecked")
	private void createMethods(){
		List<ReflectMethod<T>> methodList = New.list();
		
		for(Method m : javaClass.getMethods()){
			if(m.isSynthetic() || Modifier.isAbstract(m.getModifiers())){
				continue;
			}
			
			methodList.add(new ReflectMethod(this,m));
		}
		
		this.methods = methodList.toArray(new ReflectMethod[methodList.size()]);
		this.declaredMethods = getDeclaredMembers(methodList).toArray(new ReflectMethod[]{});
	}
	
	private static <T extends ReflectMember<E>,E> List<T> getDeclaredMembers(List<T> members){
		return Collections.where(members, new Predicate<T>() {
			public boolean evaluate(T object) {
	            return object.isDeclared();
            }
		});
	}
}