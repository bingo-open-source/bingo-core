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

	/**
	 * 
	 * @param value 
	 * @param targetType
	 * @param genericType
	 * @param out
	 * @return
	 * @throws
	 */
	@SuppressWarnings("unused")
	public boolean convertFrom(Object value, Class<?> targetType, Type genericType, Out<Object> out) throws Throwable {
		Class<?> sourceType = value.getClass();
		Class<?> targetComponentType = targetType.getComponentType();

		if (sourceType.isArray()) {
			
			Class<?> sourceCompoenentType = sourceType.getComponentType();

			if (targetComponentType.isAssignableFrom(sourceCompoenentType)) {
				return out.returns(value);
			} else {
				int length = Array.getLength(value);
				Object array = Array.newInstance(targetComponentType, length);
				for (int i = 0; i < length; i++) {
					Array.set(array, i, Converts.convert(Array.get(value, i),targetComponentType));
				}
				return out.returns(array);
			}
			
		} else if (value instanceof CharSequence) {
			
			return out.returns(stringToArray(value.toString(),targetComponentType));
			
		} else if (value instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>)value;

			return out.returns(iterableToArray(collection, targetComponentType, collection.size()));
			
		} else if (value instanceof Iterable<?>) {
			Iterable<?> iterable = (Iterable<?>) value;
			
			int length = 0;
			
			for (Object e : iterable) {
				length++;
			}
			
			return out.returns(iterableToArray(iterable, targetComponentType, length));
		}

		return false;
	}

	/**
	 * 将传入的Object对象（一般为array数组）转换为字符串，数组元素之间用逗号“,”连接。
	 * @param array 传入的Object对象，一般为array数组。
	 * @return 转换后的字符串。
	 * @throws Throwable 
	 */
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
	
	/**
	 * 将类型为 {@link Iterable}的iterable对象转换为长度为length、类型为componentType数组。
	 * @param iterable 传入的待转换的 {@link Iterable}对象。
	 * @param componentType 指定的转换后数组类型。
	 * @param length 数组的长度。
	 * @return 转换后的对象。
	 */
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
	
	/**
	 * 将传入的string字符串以逗号“,”为分隔符，转换为类型为指定的componentType的数组。
	 * @param string 传入的待转换的字符串。
	 * @param componentType 指定的数组类型。
	 * @return 由string转换过来的类型为componentType数组。
	 */
    private static Object stringToArray(String string,Class<?> componentType){
        String[] strings = Strings.split(string,',');
        
        Object array = Array.newInstance(componentType, strings.length);
        
        for(int i=0;i<strings.length;i++){
            Array.set(array, i, Converts.convert(strings[i],componentType));
        }
        
        return array;
    }
}
