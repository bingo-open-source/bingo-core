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
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import bingo.lang.convert.ArrayConverter;
import bingo.lang.convert.BeanConverter;
import bingo.lang.convert.BooleanConverter;
import bingo.lang.convert.CharacterConverter;
import bingo.lang.convert.ClassConverter;
import bingo.lang.convert.CollectionConverters;
import bingo.lang.convert.Converter;
import bingo.lang.convert.Convertible;
import bingo.lang.convert.DateTimeConverters;
import bingo.lang.convert.EnumConverter;
import bingo.lang.convert.NumberConverters;
import bingo.lang.convert.StringConverter;
import bingo.lang.exceptions.ConvertException;
import bingo.lang.exceptions.ConvertUnsupportedException;

@SuppressWarnings("unchecked")
public class Converts {
	
	private static final Map<Class<?>, Converter<?>> converters = new ConcurrentHashMap<Class<?>, Converter<?>>();
	
	private static Converter beanConverter  = new BeanConverter();
	private static Converter arrayConverter = new ArrayConverter();
	private static Converter enumConverter  = new EnumConverter();
	
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
		
		//Collection :  
		register(Iterable.class,new CollectionConverters.ListConverter());
		register(Collection.class,new CollectionConverters.ListConverter());
		register(List.class,new CollectionConverters.ListConverter());
		register(Set.class,new CollectionConverters.SetConverter());
		
		//Class
		register(Class.class,				new ClassConverter());
	}
	
	protected Converts(){

	}
	
	/**
	 * 注册某个类及其对应的 {@link Converter} 。
	 * @param clazz 要注册的类。
	 * @param converter 类对应的 {@link Converter} 。
	 */
	public static void register(Class<?> clazz,Converter<?> converter){
		converters.put(clazz,converter);
	}
	
	/**
	 * 将传入的value对象转换为指定的targetType类型并返回。
	 * @param value 传入的欲进行转换的对象。
	 * @param targetType 转换的目标类型。
	 * @return 由value对象转换过来的targetType类型的对象。
	 * @throws ConvertUnsupportedException 当传入的value对象不支持转换为指定的targetType类型时，抛出此异常。
	 */
	public static <T> T convert(Object value,Class<T> targetType) throws ConvertUnsupportedException {
		return convert(value,targetType,null);
	}
	
	public static <T> T convert(Object value,Class<T> targetType, Type genericType) throws ConvertUnsupportedException {
		Assert.notNull(targetType);
        
        //trim to null if value is String
        value = Strings.trimToNull(value);
        
        //primitive target type
        if(targetType.isPrimitive()){
        	return toPrimitive(value,targetType);
        }
        
        //null value TODO: default value of null 
        if(null == value){
        	return null;
        }
        
        Class<?> sourceType = value.getClass();
        
        //target and source type is same
        if(targetType.equals(sourceType)){
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
        
        if(targetType.isEnum()){
        	return (T)Enums.valueOf((Class<? extends Enum>)targetType,value);
        }
        
        try {
	        //value is Convertible
	        Out<Object> out = new OutObject<Object>();
	        
	        if(value instanceof Convertible) {
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

	        //get converter for source type
	        converter = findConverter(sourceType);
	        
	        //convert to
	        if(null != converter && converter.convertTo(value, targetType, genericType, out)){
	        	return (T)out.getValue();
	        }
	        
	        //assignablefrom convert
	        if(targetType.isAssignableFrom(sourceType)){
	        	return (T)value;
	        }
	        
	        //object type convert
	        if(targetType.equals(Object.class)){
	            return (T)value;
	        }
	        
	        if(beanConverter.convertFrom(value, targetType, genericType, out) ){
	        	return (T)out.getValue();
	        }
	        
	        if(beanConverter.convertTo(value, targetType, genericType, out)){
	        	return (T)out.getValue();
	        }
        } catch (ConvertException e){
        	throw e;
        } catch (Throwable e) {
        	throw new ConvertException(e,"Error converting '{0}' to '{1}', value : {2}",sourceType.getName(),targetType.getName(),value);
        }
        
		throw new ConvertUnsupportedException("Cannot convert '{0}' to '{1}', value : {2}",sourceType.getName(),targetType.getName(),value.toString());
	}
	
	/**
	 * 返回代表该对象的字符串。
	 * 方法将从已注册的 {@link Converter} 中寻找该类型对应的 {@link Converter}。
	 * 如果存在对应的 {@link Converter} 的话则返回该对象的 
	 * {@link Converter#convertToString(Object)} 方法的返回值。
	 * 如果不存在，则直接调用该对象的 {@link Object#toString()}。
	 * @param value 传入的该对象。
	 * @return 若传入对象为<code>null</code>，则返回 <code>null</code>。
	 */
	public static String toString(Object value) {
		if(null == value){
			return null;
		}
		
		Class<?> sourceType = value.getClass();
		
        try {
	        //get converter for value type
	        Converter converter = findConverter(sourceType);
	        
	        //convert to string
	        if(null != converter){
	        	return converter.convertToString(value);
	        }
	        
	        if(value.getClass().isArray()){
	            StringBuilder string = new StringBuilder();
	            for(int i=0;i<Array.getLength(value);i++){
	                if(i > 0){
	                    string.append(',');
	                }
	                string.append(toString(Array.get(value, i)));
	            }
	            return string.toString();
	        }

	        if(value instanceof Iterable){
	        	return Strings.join((Iterable)value,",");
	        }
	        
	        return value.toString();
        } catch (ConvertException e){
        	throw e;
        } catch (Throwable e) {
        	throw new ConvertException(e,"error converting '{0}' to String",sourceType.getName());
        }
	}
	
	/**
	 * 返回传入的value转换为targetType所对应的基本类型的值。<br>
	 * 如果传入的value为 <code>null</code> ，则返回targetType类型的默认值。
	 * @param targetType 目标类型，一般为数据类型。
	 * @param value 要转换类型的值。
	 * @return 转换为基本类型后的值，如果之前传入的value为 <code>null</code> ，
	 * 则返回该基本类型的默认值。
	 */
	static <T> T toPrimitive(Object value,Class<T> targetType) {
		if(null == value){
			return (T)Primitives.defaultValue(targetType);
		}else{
			return convert(value,Primitives.wrap(targetType));
		}
	}
	
	/**
	 * 从已注册的类中，找到某个类对应的 {@link Converter} 。
	 * 可传入数组或枚举，返回数组和枚举对应的 {@link Converter} 。
	 * @param type 要找到 {@link Converter} 的某个类。
	 * @return 该类对应的 {@link Converter} 。如果找不到，则返回null。
	 */
	static Converter findConverter(Class<?> type) {
		Converter converter = converters.get(type);
		
		if(null == converter){
			
			if(type.isArray()){
				return arrayConverter;
			}
			
			if(type.isEnum()){
				return enumConverter;
			}
		}
		
		return converter;
	}
}
