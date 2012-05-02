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
package bingo.lang.time;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class DateFormats {
	
	private static final Map<String,  DateFormat> patternFormats  = new ConcurrentHashMap<String,  DateFormat>();
	private static final Map<Class<?>,String>     defaultPatterns = new ConcurrentHashMap<Class<?>,String>();

    public static final String DATE_PATTERN      = "yyyy-MM-dd";
    public static final String TIME_PATTERN      = "HH:mm:ss";
    public static final String DATETIME_PATTERN  = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String RFC_DATE_PATTERN  = "yyyy-MM-dd'T'HH:mm:ssZ";  
    
    public static final String[] DEFAULT_PATTERNS = new String[]{
    																DATETIME_PATTERN,
    																DATE_PATTERN,
    																TIME_PATTERN,
    																TIMESTAMP_PATTERN,
    																RFC_DATE_PATTERN
																   };    
    
    public static final DateFormat DATE_FORMAT      = new ConcurrentDateFormat(DATE_PATTERN);
    public static final DateFormat TIME_FORMAT      = new ConcurrentDateFormat(DATE_PATTERN);
    public static final DateFormat DATETIME_FORMAT  = new ConcurrentDateFormat(DATE_PATTERN);
    public static final DateFormat TIMESTAMP_FORMAT = new ConcurrentDateFormat(DATE_PATTERN);
    public static final DateFormat REF_DATE_FORMAT  = new ConcurrentDateFormat(DATE_PATTERN);
    
    static {
    	patternFormats.put(DATE_PATTERN,      DATE_FORMAT);
    	patternFormats.put(TIME_PATTERN,      TIME_FORMAT);
    	patternFormats.put(DATETIME_PATTERN,  DATETIME_FORMAT);
    	patternFormats.put(TIMESTAMP_PATTERN, TIMESTAMP_FORMAT);
    	patternFormats.put(RFC_DATE_PATTERN,  REF_DATE_FORMAT);
    	
    	defaultPatterns.put(Timestamp.class,      TIMESTAMP_PATTERN);
    	defaultPatterns.put(Time.class, 	       TIME_PATTERN);
    	defaultPatterns.put(Date.class, 	   	   DATE_PATTERN);
    	defaultPatterns.put(java.util.Date.class, DATETIME_PATTERN);
    }
    
	protected DateFormats(){
		
	}
	
	public static DateFormat getFormat(String pattern) {
        DateFormat format = patternFormats.get(pattern);
        
        if(null == format){
        	format = new ConcurrentDateFormat(pattern);
            
            patternFormats.put(pattern,format);
        }
        
        return format;
	}
	
	public static DateFormat getFormat(Class<?> type) {
		return getFormat(getPattern(type));
	}	
	
	public static String getPattern(Class<?> type) {
		String pattern = defaultPatterns.get(type);
		
		return null == pattern ? DATETIME_PATTERN : pattern;
	}
}