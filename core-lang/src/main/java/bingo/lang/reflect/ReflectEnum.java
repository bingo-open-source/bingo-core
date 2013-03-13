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

import java.lang.reflect.Field;
import java.util.Map;
import java.util.WeakHashMap;

import bingo.lang.Assert;
import bingo.lang.Strings;

public class ReflectEnum {
	
	private static final String VALUE_FIELD_NAME = "value";

	private static final Map<Class<?>, ReflectEnum> cache = 
		java.util.Collections.synchronizedMap(new WeakHashMap<Class<?>, ReflectEnum>());
	
	public static ReflectEnum get(Class<?> enumType){
		Assert.notNull(enumType,"enumType must not be null");
		Assert.isValidState(enumType.isEnum(),Strings.format("{0} is not an enum type", enumType.getName()));
		
		ReflectEnum reflectEnum = cache.get(enumType);
		
		if(null == reflectEnum){
			reflectEnum = new ReflectEnum(enumType);
			cache.put(enumType, reflectEnum);
		}
		
		return reflectEnum;
	}
	
	private final ReflectClass<?> reflectClass;
	private ReflectField 		  valueField;
	private Class<?>              valueType;
	private boolean			      hasValueField;
	private Field[]			      enumConstantFields;
	
	protected ReflectEnum(Class<?> type){
		this.reflectClass = ReflectClass.get(type);
		this.initialize();
	}
	
	public boolean isValued(){
		return hasValueField;
	}
	
	public Class<?> getValueType(){
		return valueType;
	}
	
	public Object getValue(Object enumConstant) {
		return null != valueField ? valueField.getValue(enumConstant) : enumConstant.toString();	
	}
	
	public Enum<?>[] getEnumConstants(){
		return (Enum<?>[])reflectClass.getJavaClass().getEnumConstants();
	}
	
	public Field[] getEnumConstantFields(){
		if(null == enumConstantFields){
			Enum<?>[] enums = (Enum<?>[])reflectClass.getJavaClass().getEnumConstants();
			
			enumConstantFields = new Field[enums.length];
			
			for(int i=0;i<enums.length;i++){
				enumConstantFields[i] = reflectClass.getField(enums[i].name()).getJavaField();
			}
		}
		return enumConstantFields;
	}
	
	private void initialize(){
		this.valueField    = reflectClass.getField(VALUE_FIELD_NAME);
		this.hasValueField = null != valueField;
		this.valueType     = hasValueField ? valueField.getType() : null;
	}
}