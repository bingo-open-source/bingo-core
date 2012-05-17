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
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class Types {
	
	protected static final Type[] EMPTY_TYPES = new Type[]{};

	protected Types(){
		
	}
	
	public static Type[] getTypeArguments(Type genericType){
		Assert.notNull(genericType);
		
		if(genericType instanceof ParameterizedType){
			return((ParameterizedType) genericType).getActualTypeArguments();
		}
		
		if(genericType instanceof Class<?>) {
			Class<?> clazz = ((Class<?>) genericType);
			
			TypeVariable<?>[] types = clazz.getTypeParameters();
			
			if (types.length > 1) {
				throw new IllegalArgumentException("type argument's length large than 1");
			}
			
			return types;
		}
		
		return EMPTY_TYPES;
	}
	
	public static Class<?> getActualTypeArgument(Type genericType){
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

				return getActualType(types[0]);
				
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
				return getActualType(types[0]);
			}
			
			return Object.class;
		}
		
		return Object.class;
	}
	
	public static Class<?> getActualType(Type type){
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
        		return getActualType(types[0]);
        	}
        }
        
		if (type instanceof GenericArrayType) {
			// get raw component type
			Class<?> rawComponentType = getActualType(((GenericArrayType) type).getGenericComponentType());

			// create array type from raw component type and return its class
			return Array.newInstance(rawComponentType, 0).getClass();
		}
		
		return null;
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
