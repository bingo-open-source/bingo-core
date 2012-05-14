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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import bingo.lang.time.DateFormats;

public class DateTimeConverters {

	public static class DateTimeConverter extends AbstractDateConverter<Date> implements Converter<Date> {
		
		public DateTimeConverter() {
		    this.patterns = DateFormats.DEFAULT_PATTERNS;
	    }

		@Override
	    protected Date toDate(Class<?> targetType, Date date) {
		    return date;
	    }

		@Override
	    protected Date toDate(Class<?> targetType, Calendar calendar) {
		    return new Date(calendar.getTimeInMillis());
	    }

		@Override
	    protected Date toDate(Class<?> targetType, Long time) {
		    return new Date(time);
	    }
	}
	
	public static class SqlDateConverter extends AbstractDateConverter<java.sql.Date> {

		@Override
	    protected java.sql.Date toDate(Class<?> targetType, Calendar calendar) {
		    return new java.sql.Date(calendar.getTimeInMillis());
	    }

		@Override
	    protected java.sql.Date toDate(Class<?> targetType, java.util.Date date) {
		    return new java.sql.Date(date.getTime());
	    }

		@Override
	    protected java.sql.Date toDate(Class<?> targetType, Long time) {
		    return new java.sql.Date(time);
	    }

	}
	
	public static class SqlTimeConverter extends AbstractDateConverter<Time> {
		
		@Override
	    protected Time toDate(Class<?> targetType, Calendar calendar) {
		    return new java.sql.Time(calendar.getTimeInMillis());
	    }

		@Override
	    protected Time toDate(Class<?> targetType, Date date) {
		    return new java.sql.Time(date.getTime());
	    }

		@Override
	    protected Time toDate(Class<?> targetType, Long time) {
			return new java.sql.Time(time);
		}
	}
	
	public static class SqlTimestampConverter extends AbstractDateConverter<Timestamp> {
		
		@Override
	    protected Timestamp toDate(Class<?> targetType, Calendar calendar) {
		    return new Timestamp(calendar.getTimeInMillis());
	    }

		@Override
	    protected Timestamp toDate(Class<?> targetType, Date date) {
		    return new Timestamp(date.getTime());
	    }

		@Override
	    protected Timestamp toDate(Class<?> targetType, Long time) {
		    return new Timestamp(time);
	    }
	}
}
