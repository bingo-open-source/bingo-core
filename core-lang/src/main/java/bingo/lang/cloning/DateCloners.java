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
package bingo.lang.cloning;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class DateCloners {

	static class SqlTimestampCloner implements TypeCloner<Timestamp> {
		public Timestamp clone(Cloner cloner, Timestamp object, Map<Object, Object> clones, boolean deepClone) throws IllegalAccessException {
	        return new Timestamp(object.getTime());
        }
	}
	
	static class SqlTimeCloner implements TypeCloner<Time> {
		public Time clone(Cloner cloner, Time object, Map<Object, Object> clones, boolean deepClone) throws IllegalAccessException {
	        return new Time(object.getTime());
        }
	}
	
	static class SqlDateCloner implements TypeCloner<java.sql.Date> {
		public java.sql.Date clone(Cloner cloner, java.sql.Date object, Map<Object, Object> clones, boolean deepClone) throws IllegalAccessException {
	        return new java.sql.Date(object.getTime());
        }
	}
	
	static class DateCloner implements TypeCloner<Date> {
		public Date clone(Cloner cloner, Date object, Map<Object, Object> clones, boolean deepClone) throws IllegalAccessException {
	        return new Date(object.getTime());
        }
	}
}
