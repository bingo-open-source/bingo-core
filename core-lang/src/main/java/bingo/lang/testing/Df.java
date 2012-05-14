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
package bingo.lang.testing;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import bingo.lang.Classes;
import bingo.lang.Converts;
import bingo.lang.Primitives;
import bingo.lang.Randoms;
import bingo.lang.reflect.ReflectClass;
import bingo.lang.reflect.ReflectField;

/**
 * Simple data factory for unit testing.
 */
public class Df {
	
	private static final int MAX_STRING_LENGTH   = 300;
//	private static final int MAX_COLLECTION_SIZE = 100;

	protected Df(){
		
	}
	
	public static <T> T createBean(Class<T> beanType){
		return fillBean(ReflectClass.get(beanType).newInstance());
	}
	
	public static <T> T fillBean(T bean){
		ReflectClass<?> reflectClass = ReflectClass.get(bean.getClass());

		for(ReflectField field : reflectClass.getFields()){
			if(!field.isFinal()){
				Class<?> type = field.getType();
				Object   value = field.getValue(bean);
				
				if(null != value && !value.equals(Primitives.defaultValue(type))){
					continue;
				}
				
				if(Classes.isString(type)){
					field.setValue(bean, Randoms.nextString(Randoms.nextInt(1,MAX_STRING_LENGTH)));
				}else if(Classes.isBoolean(type)){
					field.setValue(bean, Randoms.nextBoolean());
				}else if(Classes.isCharacter(type)){
					field.setValue(bean, Randoms.nextCharacter());
				}else if(Classes.isInteger(type)){
					field.setValue(bean, Randoms.nextInt());
				}else if(Classes.isShort(type)){
					field.setValue(bean, Randoms.nextShort());
				}else if(Classes.isLong(type)){
					field.setValue(bean, Randoms.nextLong());
				}else if(Classes.isFloat(type)){
					field.setValue(bean, Randoms.nextFloat());
				}else if(Classes.isDouble(type)){
					field.setValue(bean, Randoms.nextDouble());
				}else if(Classes.isBigDecimal(type)){
					field.setValue(bean, BigDecimal.valueOf(Randoms.nextDouble()));
				}else if(Classes.isBigInteger(type)){
					field.setValue(bean, BigInteger.valueOf(Randoms.nextLong()));
				}else if(Date.class.isAssignableFrom(type)){
					field.setValue(bean, Converts.convert(Randoms.nextDateTime(),type));
				}
			}
		}
		
		return bean;
	}
}