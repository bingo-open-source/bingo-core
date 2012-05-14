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
import java.util.Map;
import java.util.WeakHashMap;

import bingo.lang.Collections;
import bingo.lang.Named;
import bingo.lang.New;
import bingo.lang.Predicate;
import bingo.lang.Predicates;
import bingo.lang.Reflects;
import bingo.lang.exceptions.ReflectException;

public class ReflectClass<T> implements Named {
	
	private static final Map<Class<?>, ReflectClass<?>> cache = 
		java.util.Collections.synchronizedMap(new WeakHashMap<Class<?>, ReflectClass<?>>());
	
	@SuppressWarnings("unchecked")
	public static <T> ReflectClass<T> get(Class<T> type) {
		ReflectClass<?> clazz = cache.get(type);
		
		if(null == clazz){
			clazz = new ReflectClass<T>(type);
			
			cache.put(type, clazz);
		}
		
		return (ReflectClass<T>)clazz;
	}
	
	private final Class<T>	         javaClass;
	private final ReflectAccessor   accessor;
	private final ReflectMetadata   metadata;
	
	private ReflectConstructor<T>[]	 constructors;
	private ReflectField[]		 	 fields;
	private ReflectMethod[]		 	 methods;
	
	private ReflectConstructor<T>[] declaredConstructors;
	private ReflectField[]		 	 declaredFields;
	private ReflectMethod[]		 	 declaredMethods;	
	
	private ReflectConstructor<T>	 defaultConstructor;	
	
	protected ReflectClass(Class<T> javaClass){
		this.javaClass = javaClass;
		this.metadata  = new ReflectMetadata(javaClass);
		this.accessor  = ReflectAccessor.createFor(this.javaClass);
		this.initialize();
	}
	
	public String getName() {
	    return javaClass.getName();
    }

	@SuppressWarnings("unchecked")
	public T newInstance(){
		if(null == defaultConstructor){
			throw new ReflectException("there is no default constructor available in class '{0}'",getName());
		}
		return (T)accessor.newInstance();
	}
	
	@SuppressWarnings("unchecked")
	public T[] newArray(int length){
		return (T[])accessor.newArray(length);
	}
	
	public int getArrayLength(Object array){
		return accessor.getArrayLength(array);
	}	
	
	public Object getArrayItem(Object array,int index){
		return accessor.getArrayItem(array, index);
	}
	
	public void setArrayItem(Object array,int index,Object value){
		accessor.setArrayItem(array, index, value);
	}
	
	public boolean isAbstract() {
		return Modifier.isAbstract(javaClass.getModifiers());
	}
	
	public boolean isArray() {
		return javaClass.isArray();
	}
	
	public boolean isInterface() {
		return javaClass.isInterface();
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
	
	public ReflectField[] getFields() {
		return fields;
	}
	
	public ReflectField[] getDeclaredFields(){
		return declaredFields;
	}
	
	public ReflectField getField(final String name){
		return Collections.firstOrNull(fields, new Predicate<ReflectField>() {
			public boolean evaluate(ReflectField object) {
	            return object.getName().equals(name);
            }
		});
	}
	
	public ReflectField getField(final String name,final Class<?> fieldType){
		return Collections.firstOrNull(fields, new Predicate<ReflectField>() {
			public boolean evaluate(ReflectField object) {
	            return object.getName().equals(name) && object.getJavaField().getType().equals(fieldType);
            }
		});
	}
	
	public ReflectField findField(final String name){
		return Collections.firstOrNull(fields, new Predicate<ReflectField>() {
			public boolean evaluate(ReflectField object) {
	            return object.getName().equalsIgnoreCase(name);
            }
		});
	}
	
	public ReflectMethod[] getMethods(){
		return methods;
	}
	
	public ReflectMethod[] getDeclaredMethods(){
		return declaredMethods;
	}
	
	public ReflectMethod getMethod(final String name){
		return Collections.firstOrNull(methods, Predicates.nameEquals(ReflectMethod.class, name));
	}
	
	public ReflectMethod getMethod(final String name,final Class<?>... argumentTypes){
		return Collections.firstOrNull(methods, new Predicate<ReflectMethod>() {
			public boolean evaluate(ReflectMethod object) {
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
	
	public ReflectMethod getMethod(String name,Class<?> returnType,Class<?>... argumentTypes){
		ReflectMethod m = getMethod(name, argumentTypes);
		
		return null == m ? null : (m.getJavaMethod().getReturnType().equals(returnType) ? m : null);
	}	
	
	public ReflectMethod[] getMethods(final String name) {
		return Collections.where(methods, new Predicate<ReflectMethod>() {
			public boolean evaluate(ReflectMethod object) {
	            return object.getName().equals(name);
            }
		}).toArray(new ReflectMethod[]{});
	}
	
	ReflectAccessor getAccessor(){
		return accessor;
	}
	
	ReflectMetadata getMetadata(){
		return metadata;
	}
	
	private void initialize(){
		//constructors
		this.createConstructors();
		
		//methods
		this.createMethods();	 //must create methods firstly , will be used in createFields()
		
		//fields
		this.createFields();
	}
	
	@SuppressWarnings("unchecked")
	private void createConstructors(){
		List<ReflectConstructor<T>> constructorList = New.list();
		
		for(Constructor<T> c : Reflects.getConstructors(javaClass)) {
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
	
	private void createFields(){
		List<ReflectField> fieldList = New.list();
		
		for(Field f : Reflects.getFields(javaClass)){
			if(!f.isSynthetic() && !Object.class.equals(f.getDeclaringClass())){
				fieldList.add(new ReflectField(this,f));
			}
		}
		
		this.fields = fieldList.toArray(new ReflectField[fieldList.size()]);
		this.declaredFields = getDeclaredMembers(fieldList).toArray(new ReflectField[]{});
	}
	
	private void createMethods(){
		List<ReflectMethod> methodList = New.list();
		
		for(Method m : Reflects.getMethods(javaClass)){
			if(m.isSynthetic() || Modifier.isAbstract(m.getModifiers()) || Object.class.equals(m.getDeclaringClass())){
				continue;
			}
			
			methodList.add(new ReflectMethod(this,m));
		}
		
		this.methods = methodList.toArray(new ReflectMethod[methodList.size()]);
		this.declaredMethods = getDeclaredMembers(methodList).toArray(new ReflectMethod[]{});
	}
	
	@Override
    public String toString() {
		return javaClass.toString();
    }
	
	private static <T extends ReflectMember,E> List<T> getDeclaredMembers(List<T> members){
		return Collections.where(members, new Predicate<T>() {
			public boolean evaluate(T object) {
	            return object.isDeclared();
            }
		});
	}
}