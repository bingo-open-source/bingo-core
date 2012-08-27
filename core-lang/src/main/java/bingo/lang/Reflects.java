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
package bingo.lang;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bingo.lang.exceptions.NotFoundException;
import bingo.lang.exceptions.ReflectException;
import bingo.lang.reflect.ReflectClass;
import bingo.lang.reflect.ReflectField;

public class Reflects {
	
	protected Reflects(){
		
	}
	
	public static <T> ReflectClass<T> forType(Class<T> classType){
		return ReflectClass.get(classType);
	}
	
	public static ReflectClass<?> forName(String className) throws NotFoundException {
		return ReflectClass.get(Classes.forName(className));
	}
	
	public static <T> T newInstance(Class<T> type){
		return ReflectClass.get(type).newInstance();
	}
	
	public static <T> T newInstanceWithoutCallingConstructor(Class<T> type) throws IllegalStateException {
		return ReflectClass.get(type).newInstanceWithoutCallingConstructor();
	}
	
	public static <T>	T[] newArray(Class<T> type,int length){
		return ReflectClass.get(type).newArray(length);
	}
	
	/**
	 * Get the field represented by the supplied {@link Field field object} on the
	 * specified {@link Object target object}. In accordance with {@link Field#get(Object)}
	 * semantics, the returned value is automatically wrapped if the underlying field
	 * has a primitive type.
	 * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException(Exception)}.
	 * @param field the field to get
	 * @param target the target object from which to get the field
	 * @return the field's current value
	 */
	public static Object getFieldValue(Object target,Field field) {
		try {
			return field.get(target);
		}
		catch (IllegalAccessException ex) {
			handleException(ex);
			throw new ReflectException(
					"Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
		}
	}
	
	public static Object getFieldValue(Object target,String name) throws NotFoundException {
		Assert.notNull(target);
		Assert.notNull(name);
		
		ReflectField field = ReflectClass.get(target.getClass()).getField(name);
		
		if(null == field){
			throw new NotFoundException("field name '{0}' not exists in type '{1}'",name,target.getClass().getName());
		}
		
		return field.getValue(target,false);
	}
	
	/**
	 * Invoke the specified {@link Method} against the supplied target object with no arguments.
	 * The target object can be <code>null</code> when invoking a static {@link Method}.
	 * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
	 * @param method the method to invoke
	 * @param target the target object to invoke the method on
	 * @return the invocation result, if any
	 * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
	 */
	public static Object invokeMethod(Method method, Object target) {
		return invokeMethod(method, target, new Object[0]);
	}

	/**
	 * Invoke the specified {@link Method} against the supplied target object with the
	 * supplied arguments. The target object can be <code>null</code> when invoking a
	 * static {@link Method}.
	 * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
	 * @param method the method to invoke
	 * @param target the target object to invoke the method on
	 * @param args the invocation arguments (may be <code>null</code>)
	 * @return the invocation result, if any
	 */
	public static Object invokeMethod(Method method, Object target, Object... args) {
		try {
			return method.invoke(target, args);
		}
		catch (Exception ex) {
			handleException(ex);
			throw new ReflectException(ex.getMessage(),ex);
		}
	}
	
	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with the
	 * supplied <code>name</code>. Searches all superclasses up to {@link Object}.
	 * @param clazz the class to introspect
	 * @param name the name of the field
	 * @return the corresponding Field object, or <code>null</code> if not found
	 */
	public static Field findField(Class<?> clazz, String name) {
		return findField(clazz, name, null);
	}

	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with the
	 * supplied <code>name</code> and/or {@link Class type}. Searches all superclasses
	 * up to {@link Object}.
	 * @param clazz the class to introspect
	 * @param name the name of the field (may be <code>null</code> if type is specified)
	 * @param type the type of the field (may be <code>null</code> if name is specified)
	 * @return the corresponding Field object, or <code>null</code> if not found
	 */
	public static Field findField(Class<?> clazz, String name, Class<?> type) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.isTrue(name != null || type != null, "Either name or type of the field must be specified");
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (Field field : fields) {
				if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}
	
	/**
	 * Attempt to find a {@link Method} on the supplied class with the supplied name
	 * and no parameters. Searches all superclasses up to <code>Object</code>.
	 * <p>Returns <code>null</code> if no {@link Method} can be found.
	 * @param clazz the class to introspect
	 * @param name the name of the method
	 * @return the Method object, or <code>null</code> if none found
	 */
	public static Method findMethod(Class<?> clazz, String name) {
		return findMethod(clazz, name, new Class[0]);
	}

	/**
	 * Attempt to find a {@link Method} on the supplied class with the supplied name
	 * and parameter types. Searches all superclasses up to <code>Object</code>.
	 * <p>Returns <code>null</code> if no {@link Method} can be found.
	 * @param clazz the class to introspect
	 * @param name the name of the method
	 * @param paramTypes the parameter types of the method
	 * (may be <code>null</code> to indicate any signature)
	 * @return the Method object, or <code>null</code> if none found
	 */
	public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(name, "Method name must not be null");
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
			for (Method method : methods) {
				if (name.equals(method.getName())
						&& (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}	
	
	
	//generic types
	//---------------------------------------------------------------------------------------------

	
	/*
	public static Class<?> getTypeArgument(Type genericType, Class<?> genericDelcarationType) {
		
		if(genericType instanceof ParameterizedType){
			Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();

			if (types.length != 1) {
				return null;
			}
			
			Type type = types[0];
			
			if(type instanceof WildcardType){
				
				types = ((WildcardType)type).getUpperBounds();
				
				if(types.length != 1){
					return null;
				}
				
				type = types[0];
			}

			return getRawType(types[0]);
		}
		
		if(genericType instanceof Class<?>) {
			Class<?> clazz = ((Class<?>) genericType);
			
			TypeVariable<?>[] types = clazz.getTypeParameters();
			
			if(types.length == 1){
				return getRawType(types[0]);				
			}else if(types.length == 0 && null != genericDelcarationType){
				
				Class<?> search = clazz;
				
				Type   superClass      = search.getGenericSuperclass();
				Type[] superInterfaces = search.getGenericInterfaces();
				
				if(null == superClass && superInterfaces.length == 0){
					return null;
				}
				
				if(null != superClass && genericDelcarationType.equals(getRawType(superClass))){
					return getTypeArgument(superClass);
				}
				
				for(Type superInterface : superInterfaces){
					if(superInterface.equals(genericDelcarationType)){
						return getTypeArgument(superInterface);
					}
				}
				
			}
			
			return null;
		}
		
		return null;
	}
	*/
	
	
	//Consturctors, Fields, Methods
	//--------------------------------------------------------------------------------------------------------
	
	/**
	 * 获得所传入的clazz类的所有非编译器合成方法，包括父类的。
	 * @param clazz 欲获得所有方法的clazz类。
	 * @return 所有非编译器合成方法。
	 */
    public static List<Method> getMethods(Class<?> clazz){
        List<Method> methods = new ArrayList<Method>();
        
        for (Class<?> search = clazz; search != null; search = search.getSuperclass()) {
            for(Method method : search.getDeclaredMethods()){
                //exclude synthetic method
                if(!method.isSynthetic()){
                    methods.add(method) ;
                }
            }
        }
        
        return methods;
    }
	
    public static List<Field> getFields(Class<?> clazz){
        List<Field> fields = new ArrayList<Field>();
        
        for (Class<?> search = clazz; search != null; search = search.getSuperclass()) {
            for(Field field : search.getDeclaredFields()){
            	fields.add(field) ;
            }
        }
        
        return fields;
    }
    
    //Protected Methods
    //------------------------------------------------------------------
	/**
	 * Handle the given reflection exception. Should only be called if no checked exception is expected to be thrown by
	 * the target method.
	 * <p>
	 * Throws the underlying RuntimeException or Error in case of an InvocationTargetException with such a root cause.
	 * Throws an IllegalStateException with an appropriate message else.
	 * 
	 * @param ex the reflection exception to handle
	 */
	protected static void handleException(Exception ex) {
		if (ex instanceof InvocationTargetException) {
			Throwable cause = ((InvocationTargetException) ex).getTargetException();
			
			if (cause instanceof RuntimeException) {
				throw (RuntimeException) cause;
			}
			
			if (cause instanceof Error) {
				throw (Error) cause;
			}
			throw new ReflectException(ex);
		}
		
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: " + ex.getMessage());
		}
		
		if (ex instanceof NoSuchFieldException) {
			throw new IllegalStateException("Field not found: " + ex.getMessage());
		}
		
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Illegal access method or field: " + ex.getMessage());
		}
		
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		
		throw new ReflectException(ex);
	}    
	
	//Internal Methods
	//--------------------------------------------------------------------------------------------------------
}
