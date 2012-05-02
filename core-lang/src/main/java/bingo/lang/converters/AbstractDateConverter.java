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
package bingo.lang.converters;

import java.util.Calendar;
import java.util.Date;

import bingo.lang.Dates;
import bingo.lang.Out;
import bingo.lang.Strings;
import bingo.lang.exceptions.ConvertException;
import bingo.lang.time.DateFormats;

public abstract class AbstractDateConverter<T extends Date> extends AbstractConverter<T> {
	
	protected String[] patterns;
	
	public AbstractDateConverter(){
		
	}

	public AbstractDateConverter(String pattern){
		this.patterns = new String[]{pattern};
	}
	
	public AbstractDateConverter(String[] patterns){
		this.patterns = patterns;
	}

	@Override
    public boolean convertFrom(Object value, Class<?> targetType, Class<?> genericType, Out<Object> out) throws Throwable {
		if(value instanceof Date){
			return out.returns(toDate(targetType,(Date)value));
		}else if(value instanceof Calendar){
			return out.returns(toDate(targetType,(Calendar)value));
		}else if(value instanceof Long){
			return out.returns(toDate(targetType,(Long)value));
		}else{
			String stringValue = value.toString();
			
			if(Strings.isDigits(stringValue)){
				return out.returns((toDate(targetType, Long.parseLong(stringValue))));
			}else{
				return out.returns(toDate(targetType,stringValue));
			}
		}
    }

	@Override
    public String convertToString(T value) throws Throwable {
	    return DateFormats.getFormat(value.getClass()).format((Date)value);
    }
	
	public void setPattern(String pattern){
		this.patterns = new String[]{pattern};
	}
	
	public void setPatterns(String[] patterns){
		this.patterns = patterns;
	}
	
	protected abstract T toDate(Class<?> targetType, Date date);
	
	protected abstract T toDate(Class<?> targetType, Calendar calendar);
	
	protected abstract T toDate(Class<?> targetType, Long time);
	
	protected T toDate(Class<?> targetType, String stringValue) {
		if(null == patterns || patterns.length == 0){
			return toDate(targetType, Dates.parse(stringValue,DateFormats.getPattern(targetType)));
		}else{
			Date date = null;
			
			for(String pattern : patterns){
				date = Dates.parseOrNull(stringValue,pattern);
				
				if(null != date){
					return toDate(targetType,date);
				}
			}
			
			throw new ConvertException("cannot convert string '{0}' to '{1}'",stringValue,targetType.getName());
		}
	}
}
