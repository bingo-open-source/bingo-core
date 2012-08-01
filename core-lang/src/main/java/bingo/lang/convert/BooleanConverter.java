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

import java.lang.reflect.Type;

import bingo.lang.Converts;
import bingo.lang.Out;

public class BooleanConverter extends AbstractConverter<Boolean> implements Converter<Boolean> {
	
    /**
     * The set of strings that are known to map to Boolean.TRUE.
     */
    private String[] trueStrings = {"true", "yes", "y", "on", "1"};

    /**
     * The set of strings that are known to map to Boolean.FALSE.
     */
    private String[] falseStrings = {"false", "no", "n", "off", "0"};
	
    public BooleanConverter(){
    	
    }
    
    public BooleanConverter(String[] trueStrings, String[] falseStrings){
    	this.trueStrings  = copyStrings(trueStrings);
    	this.falseStrings = copyStrings(falseStrings);
    }
    
	@Override
    public boolean convertTo(Boolean value, Class<?> targetType, Type genericType, Out<Object> out) throws Throwable {
		if(Number.class.isAssignableFrom(targetType)){
			return out.returns(Converts.convert(value.booleanValue() ? 1 : 0, targetType));
		}else if(Character.class.equals(targetType)){
			return out.returns(value.booleanValue() ? '1' : '0');
		}
		return false;
    }

	@Override
    public boolean convertFrom(Object value, Class<?> targetType, Type genericType, Out<Object> out) throws Throwable {
		String stringValue = value.toString().toLowerCase();
		
        for(int i=0; i<trueStrings.length; ++i) {
            if (trueStrings[i].equals(stringValue)) {
                return out.returns(Boolean.TRUE);
            }
        }

        for(int i=0; i<falseStrings.length; ++i) {
            if (falseStrings[i].equals(stringValue)) {
                return out.returns(Boolean.FALSE);
            }
        }
		
		return false;
    }

    /**
     * This method creates a copy of the provided array, and ensures that
     * all the strings in the newly created array contain only lower-case
     * letters.
     * <p>
     * Using this method to copy string arrays means that changes to the
     * src array do not modify the dst array.
     */
    private static String[] copyStrings(String[] src) {
        String[] dst = new String[src.length];
        for(int i=0; i<src.length; ++i) {
            dst[i] = src[i].toLowerCase();
        }
        return dst;
    }	
}