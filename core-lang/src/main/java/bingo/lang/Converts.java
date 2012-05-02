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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import bingo.lang.convert.Converter;
import bingo.lang.convert.Convertible;
import bingo.lang.converters.ArrayConverter;
import bingo.lang.converters.BeanConverter;
import bingo.lang.converters.BooleanConverter;
import bingo.lang.converters.CharacterConverter;
import bingo.lang.converters.ClassConverter;
import bingo.lang.converters.CollectionConverter;
import bingo.lang.converters.DateTimeConverters;
import bingo.lang.converters.EnumConverter;
import bingo.lang.converters.NumberConverters;
import bingo.lang.converters.StringConverter;
import bingo.lang.exceptions.ConvertException;
import bingo.lang.exceptions.UnsupportedException;

@SuppressWarnings("unchecked")
public class Converts {
	
	private static final Map<Class<?>, Converter<?>> converters = new ConcurrentHashMap<Class<?>, Converter<?>>();
	
	private static Converter beanConverter       = new BeanConverter();
	private static Converter arrayConverter      = new ArrayConverter();
	private static Converter enumConverter       = new EnumConverter();
	private static Converter collectionConverter = new CollectionConverter();
	
	static {
		//String , Charracter , Boolean
		register(String.class,				new StringConverter());
		register(Character.class,			new CharacterConverter());
		register(Boolean.class,				new BooleanConverter());

		//Number : Byte, Short, Integer, Long, Float, Double, BigInteger, BigDecimal
		register(Byte.class,				new NumberConverters.ByteConverter());
		register(Short.class,				new NumberConverters.ShortConverter());
		register(Integer.class,				new NumberConverters.IntegerConverter());
		register(Long.class,				new NumberConverters.LongConverter());
		register(Float.class,				new NumberConverters.FloatConverter());
		register(Double.class,				new NumberConverters.DoubleConverter());
		register(BigInteger.class,			new NumberConverters.BigIntegerConverter());
		register(BigDecimal.class,			new NumberConverters.BigDecimalConverter());
		
		//Date: java.util.Date, java.sql.Date, java.sql.Time, java.sql.Timestamp
		register(java.util.Date.class, 		new DateTimeConverters.DateTimeConverter());
		register(java.sql.Date.class,  		new DateTimeConverters.SqlDateConverter());
		register(java.sql.Timestamp.class,	new DateTimeConverters.SqlTimestampConverter());
		register(java.sql.Time.class,		new DateTimeConverters.SqlTimeConverter());
		
		//Class
		register(Class.class,				new ClassConverter());
	}
	
	protected Converts(){

	}
	
	public static void register(Class<?> clazz,Converter<?> converter){
		converters.put(clazz,converter);
	}
	
	public static <T> T convert(Class<T> targetType, Object value) throws UnsupportedException {
		return convert(targetType,null,value);
	}
	
	public static <T> T convert(Class<T> targetType, Class<?> genericType, Object value) throws UnsupportedException {
        if(null == targetType){
            throw new IllegalArgumentException("argument 'targetType' required");
        }
        
        //trim to null if value is String
        value = Strings.trimToNull(value);
        
        //primitive target type
        if(targetType.isPrimitive()){
        	return toPrimitive(targetType,value);
        }
        
        //null value TODO: default value of null 
        if(null == value){
        	return null;
        }        
        
        Class<?> sourceType = value.getClass();
        
        //target and source type is same
        if(targetType == sourceType){
        	return (T)value;
        }
        
        //string target type
        if(targetType.equals(String.class)){
        	return (T)toString(value);
        }
        
        //enum value
        if(sourceType.isEnum()){
        	value      = Enums.getValue((Enum<?>)value);
        	sourceType = value.getClass();
        }
        
        try {
	        //value is Convertible
	        Out<Object> out = new OutObject<Object>();
	        
	        if(null != value && value instanceof Convertible) {
	        	if(((Convertible)value).convertTo(targetType, genericType, out)){
	        		return (T)out.getValue();
	        	}
	        }
	        
	        //get converter fro target type
	        Converter converter = findConverter(targetType);
	        
	        //convert from
	        if(null != converter && converter.convertFrom(value, targetType, genericType, out)){
	        	return (T)out.getValue();
	        }	

	        //get converter for value type
	        converter = findConverter(sourceType);
	        
	        //convert to
	        if(null != converter && converter.convertTo(value, targetType, genericType, out)){
	        	return (T)out.getValue();
	        }
	        
	        //assiable from converter
	        if(targetType.isAssignableFrom(sourceType)){
	        	return (T)value;
	        }
	        
	        //object target type
	        if(targetType == Object.class){
	            return (T)value;
	        }
	        
	        //bean converter
	        if(beanConverter.convertTo(value, targetType, genericType,out)){
	        	return (T)out;
	        }
        } catch (ConvertException e){
        	throw e;
        } catch (Throwable e) {
        	throw new ConvertException(e,"error converting value '{0}' to type '{1}'",value,targetType.getName());
        }
        
		throw new UnsupportedException("cannot convert from '{0}' to '{1}'",sourceType.getName(),targetType.getName());
	}
	
	public static String toString(Object value) {
		if(null == value){
			return Strings.EMPTY;
		}
		
		Class<?> valueType = value.getClass();
		
        try {
	        //get converter for value type
	        Converter converter = findConverter(valueType);
	        
	        //convert to
	        if(null != converter){
	        	return converter.convertToString(value);
	        }
	        
	        return value.toString();
        } catch (ConvertException e){
        	throw e;
        } catch (Throwable e) {
        	throw new ConvertException(e,"error converting type '{0}' to String",valueType.getName());
        }
	}
	
	static <T> T toPrimitive(Class<T> targetType, Object value) {
		if(null == value){
			return (T)Primitives.defaultValue(targetType);
		}else{
			return convert(Primitives.wrap(targetType),value);
		}
	}
	
	static Converter findConverter(Class<?> type) {
		Converter converter = converters.get(type);
		
		if(null == converter){
			if(type.isEnum()){
				return enumConverter;
			}
			
			if(type.isArray()){
				return arrayConverter;
			}
			
			if(Collection.class.isAssignableFrom(type)){
				return collectionConverter;
			}
		}
		
		return converter;
	}	
}