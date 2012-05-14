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
package bingo.lang.convert;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Collection;

import bingo.lang.Converts;
import bingo.lang.Out;
import bingo.lang.Strings;


public class ArrayConverter extends AbstractConverter<Object>{

	@Override
	@SuppressWarnings("unused")
	public boolean convertFrom(Object value, Class<?> targetType, Type genericType, Out<Object> out) throws Throwable {
		Class<?> sourceType = value.getClass();
		Class<?> componentType = targetType.getComponentType();

		if (sourceType.isArray()) {
			
			Class<?> sourceCompoenentType = sourceType.getComponentType();

			if (componentType.isAssignableFrom(sourceCompoenentType)) {
				return out.returns(value);
			} else {
				int length = Array.getLength(value);
				Object array = Array.newInstance(componentType, length);
				for (int i = 0; i < length; i++) {
					Array.set(array, i, Converts.convert(Array.get(value, i),componentType));
				}
				return out.returns(array);
			}
			
		} else if (value instanceof CharSequence) {
			
			return out.returns(stringToArray(value.toString(),componentType));
			
		} else if (value instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>)value;

			return out.returns(iterableToArray(collection, componentType, collection.size()));
			
		} else if (value instanceof Iterable<?>) {
			Iterable<?> iterable = (Iterable<?>) value;
			
			int length = 0;
			
			for (Object e : iterable) {
				length++;
			}
			
			return out.returns(iterableToArray(iterable, componentType, length));
		}

		return false;
	}

	@Override
    public String convertToString(Object array) throws Throwable {
		
        StringBuilder string = new StringBuilder(128);
        
        for(int i=0;i<Array.getLength(array);i++){
            if(i > 0){
                string.append(',');
            }
            
            string.append(Converts.toString(Array.get(array, i)));
        }
        
        return string.toString();
    }
	
	private static Object iterableToArray(Iterable<?> iterable,Class<?> componentType,int length){
		Object array = Array.newInstance(componentType, length);

		if(length > 0){
			int index = 0;
			
			for (Object element : iterable) {
				Array.set(array, index++, Converts.convert(element,componentType));
			}
		}

		return array;
	}
	
    private static Object stringToArray(String string,Class<?> componentType){
        String[] strings = Strings.split(string,',');
        
        Object array = Array.newInstance(componentType, strings.length);
        
        for(int i=0;i<strings.length;i++){
            Array.set(array, i, Converts.convert(strings[i],componentType));
        }
        
        return array;
    }
}
