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

import bingo.lang.exceptions.InvalidValueException;


/**
 * <code>null</code> safe {@link Enum} utility.
 */
public class Enums {

	protected Enums(){
		
	}
	
	@SuppressWarnings("unchecked")
	public static Object getValue(Enum<?> enumObject) {
		
		if(enumObject instanceof Valued){
			return ((Valued)enumObject).getValue();
		}
		
		return enumObject.name();
	}
	
	@SuppressWarnings("unchecked")
	public static <E extends Enum<?>> E valueOf(Class<E> enumType,Object value) throws InvalidValueException{
        if(null == value){
            return null;
        }
        
        if(Valued.class.isAssignableFrom(enumType)){
            for(E e : enumType.getEnumConstants()){
            	if(((Valued)e).getValue().equals(value)){
            		return e;
            	}
            }
        }else{
            String stringValue = value.toString();
            
            if(stringValue.equals("")){
                return null;
            }
            
            for(E e : enumType.getEnumConstants()){
                if(e.toString().equals(stringValue)){
                    return e;
                }
            }
        }
        
        throw new InvalidValueException("invalid enum value '{0}' of type '{1}'",value,enumType.getName());
	}
}