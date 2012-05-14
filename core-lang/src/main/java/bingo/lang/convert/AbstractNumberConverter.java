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

import bingo.lang.Out;

public abstract class AbstractNumberConverter<T extends Number> extends AbstractConverter<T> {

    private static final Integer ZERO = new Integer(0);
    private static final Integer ONE  = new Integer(1);
	
	@Override
    public boolean convertFrom(Object value, Class<?> targetType, Type genericType, Out<Object> out) throws Throwable {
		if(value instanceof Number){
			return out.returns(toNumber(targetType, ((Number)value)));
		}else if(value instanceof Boolean){
			return out.returns(toNumber(targetType,((Boolean)value) ? ONE : ZERO));
		}else {
			return out.returns(toNumber(targetType, value));
		}
    }
	
	protected T toNumber(Class<?> targetType,Object value) {
		return toNumber(targetType,value.toString());
	}

	protected abstract T toNumber(Class<?> targetType,Number number);
	
	protected abstract T toNumber(Class<?> targetType,String stringValue);
}