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

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

public class Reflects {

	protected Reflects(){
		
	}
	
	public static Class<?> getTypeArgument(Type genericType){
		Assert.notNull(genericType);
		
		if(genericType instanceof ParameterizedType){
			Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();

			if (types.length > 1) {
				throw new IllegalArgumentException("type argument's length large than 1");
			}
			
			if(types.length == 1){
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

			return Object.class;
		}
		
		if(genericType instanceof Class<?>) {
			Class<?> clazz = ((Class<?>) genericType);
			
			TypeVariable<?>[] types = clazz.getTypeParameters();
			
			if (types.length > 1) {
				throw new IllegalArgumentException("type argument's length large than 1");
			}
			
			if(types.length == 1){
				return getRawType(types[0]);
			}
			
			return Object.class;
		}
		
		return Object.class;
	}
	
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
	
	public static Class<?> getRawType(Type type){
        if (type instanceof Class<?>) {
            // it is raw, no problem
            return (Class<?>) type;
        }
        
        if (type instanceof ParameterizedType) {
            // simple enough to get the raw type of a ParameterizedType
            return getRawType((ParameterizedType) type);
        } 
        
        if(type instanceof TypeVariable<?>) {
        	Type[] types = ((TypeVariable<?>) type).getBounds();
        	
        	if(types.length == 1){
        		return getRawType(types[0]);
        	}
        }
        
		if (type instanceof GenericArrayType) {
			// get raw component type
			Class<?> rawComponentType = getRawType(((GenericArrayType) type).getGenericComponentType());

			// create array type from raw component type and return its class
			return Array.newInstance(rawComponentType, 0).getClass();
		}
		
		return null;
	}
	
	//Consturctors, Fields, Methods
	//--------------------------------------------------------------------------------------------------------
	
    public static <T> List<Constructor<T>> getConstructors(Class<T> clazz){
        List<Constructor<T>> constructors = new ArrayList<Constructor<T>>();
        
        for (Class<?> search = clazz; search != null; search = search.getSuperclass()) {
            for(Constructor<T> constructor : search.getDeclaredConstructors()){
                //exclude synthetic constructor
                if(!constructor.isSynthetic()){
                    constructors.add(constructor) ;
                }
            }
        }
        
        return constructors;
    }

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
                //exclude synthetic field
                if(!field.isSynthetic()){
                    fields.add(field) ;
                }
            }
        }
        
        return fields;
    }
	
	//Internal Methods
	//--------------------------------------------------------------------------------------------------------
	
    /**
     * <p> Transforms the passed in type to a {@code Class} object. Type-checking method of convenience. </p>
     *
     * @param parameterizedType the type to be converted
     * @return the corresponding {@code Class} object
     * @throws IllegalStateException if the conversion fails
     */
    private static Class<?> getRawType(ParameterizedType parameterizedType) {
        Type rawType = parameterizedType.getRawType();

        // check if raw type is a Class object
        // not currently necessary, but since the return type is Type instead of
        // Class, there's enough reason to believe that future versions of Java
        // may return other Type implementations. And type-safety checking is
        // rarely a bad idea.
        if (!(rawType instanceof Class<?>)) {
            throw new IllegalStateException("parameterizedType.getRawType() returned not a Class object");
        }

        return (Class<?>) rawType;
    }	
}
