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

import bingo.lang.Classes;
import bingo.lang.Enumerables;
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
	
	private final boolean 		 isInner;
	private final boolean          isSimple;
	private final boolean			 isEnumerable;
	
	private ReflectConstructor<T>[]	 constructors;
	private ReflectField[]		 	 fields;
	private ReflectMethod[]		 	 methods;
	
	private ReflectField[]		 	 declaredFields;
	private ReflectMethod[]		 	 declaredMethods;	
	
	private ReflectConstructor<T>	 defaultConstructor;	
	private boolean				 defaultConstructorInner = false;
	private ReflectInstantiator<T>  instantiator	         = null;
	
	protected ReflectClass(Class<T> javaClass){
		this.javaClass = javaClass;
		this.metadata  = new ReflectMetadata(javaClass);
		this.accessor  = ReflectAccessor.createFor(this.javaClass);
		this.isInner   = Classes.isInner(javaClass);
		this.isSimple  = Classes.isSimple(javaClass);
		this.isEnumerable = Classes.isEnumerable(javaClass);
		
		this.initialize();
	}
	
	/**
	 * 和{@link Class#getName()}一致
	 */
	public String getName() {
	    return javaClass.getName();
    }

	/**
	 * 调用缺省的无参数构造函数创建对象实例并返回
	 */
	@SuppressWarnings("unchecked")
	public T newInstance() throws ReflectException{
		if(null == defaultConstructor){
			throw new ReflectException("there is no default constructor available in class '{0}'",getName());	
		}
		
		if(defaultConstructorInner){
			return defaultConstructor.newInstance(Reflects.newInstance(javaClass.getEnclosingClass()));
		}else{
			if(accessor.canNewInstance()){
				return (T)accessor.newInstance();	
			}else{
				return defaultConstructor.newInstance((Object[])null);
			}
		}
	}
	
	public boolean canNewInstanceWithoutCallingConstructor(){
		return null != instantiator;
	}
	
	public T newInstanceWithoutCallingConstructor() throws IllegalStateException {
		if(null == instantiator){
			throw new IllegalStateException("there is no instantiator can new instance without calling constructor");
		}
		return instantiator.newInstance();
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
	
	public boolean isSimple(){
		return isSimple;
	}
	
	public boolean isEnumerable(){
		return isEnumerable;
	}
	
	public boolean isMap(){
		return Map.class.isAssignableFrom(javaClass);
	}
	
	public boolean isArray() {
		return javaClass.isArray();
	}
	
	public boolean isAbstract() {
		return Modifier.isAbstract(javaClass.getModifiers());
	}
	
	public boolean isInterface() {
		return javaClass.isInterface();
	}
	
	public boolean isConcrete(){
		return !javaClass.isInterface() && !isAbstract();
	}
	
	public boolean isInner(){
		return isInner;
	}
	
	public Class<T> getJavaClass() {
    	return javaClass;
    }
	
	public ReflectConstructor<T>[] getConstructors(){
		return constructors;
	}
	
	public ReflectConstructor<T> getConstructor(){
		return getConstructor((Class<?>[])null);
	}
	
	public ReflectConstructor<T> getConstructor(Class<?>... parameterTypes) {
		if(null == parameterTypes || parameterTypes.length == 0){
			return defaultConstructor;
		}
		
		for(ReflectConstructor<T> c : constructors) {
			Constructor<T> jc = c.getJavaConstructor();

			if(jc.getParameterTypes().length == parameterTypes.length){
				for(int i=0;i<parameterTypes.length;i++){
					
					if(!jc.getParameterTypes()[i].equals(parameterTypes[i])){
						continue;
					}
				}
				return c;
			}
		}
		return null;
	}
	
	public ReflectField[] getFields() {
		return fields;
	}
	
	public ReflectField[] getDeclaredFields(){
		return declaredFields;
	}
	
	public ReflectField getField(final String name){
		return Enumerables.firstOrNull(fields, new Predicate<ReflectField>() {
			public boolean apply(ReflectField object) {
	            return object.getName().equals(name);
            }
		});
	}
	
	public ReflectField getField(final String name,final Class<?> fieldType){
		return Enumerables.firstOrNull(fields,Predicates.<ReflectField>nameEquals(name));
	}
	
	public ReflectField getFieldIgnorecase(final String name){
		return Enumerables.firstOrNull(fields,Predicates.<ReflectField>nameEqualsIgnoreCase(name));
	}
	
	public ReflectMethod[] getMethods(){
		return methods;
	}
	
	public ReflectMethod[] getDeclaredMethods(){
		return declaredMethods;
	}
	
	public ReflectMethod getMethod(final String name){
		return Enumerables.firstOrNull(methods, Predicates.<ReflectMethod>nameEquals(name));
	}
	
	public ReflectMethod getMethod(final String name,final Class<?>... argumentTypes){
		return Enumerables.firstOrNull(methods, new Predicate<ReflectMethod>() {
			public boolean apply(ReflectMethod object) {
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
		return Enumerables.where(methods, new Predicate<ReflectMethod>() {
			public boolean apply(ReflectMethod object) {
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
		//new an empty ArrayList.
		List<ReflectConstructor<T>> constructorList = New.list();
		
		//iterate all declared constructors.
		for(Constructor<T> c : javaClass.getDeclaredConstructors()) {
			if(!c.isSynthetic()){
				ReflectConstructor<T> rc = new ReflectConstructor<T>(this,c);
				
				constructorList.add(rc);
				
				//如果为非静态内部类，且构造方法的唯一参数类型为外部类
				if(isInner && !Modifier.isStatic(javaClass.getModifiers())){
					if(c.getParameterTypes().length == 1 
							&& c.getParameterTypes()[0].equals(javaClass.getEnclosingClass())){
						defaultConstructor      = rc;
						defaultConstructorInner = true;
					}
					
				//否则为无参构造方法
				}else if(c.getParameterTypes().length == 0){
					defaultConstructor = rc;
				}
			}
		}
		
		this.instantiator = ReflectInstantiator.forType(javaClass);
		this.constructors = constructorList.toArray(new ReflectConstructor[constructorList.size()]);
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
		return Enumerables.where(members, new Predicate<T>() {
			public boolean apply(T object) {
	            return object.isDeclared();
            }
		});
	}
}