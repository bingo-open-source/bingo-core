/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

//from apache commons-lang3

/**
 * <p>
 * A suite of utilities surrounding the use of the {@link java.util.Calendar} and {@link java.util.Date} object.
 * </p>
 */
public class Dates {

	/**
	 * Number of milliseconds in a standard second.
	 */
	public static final long	 MILLIS_PER_SECOND	 = 1000;

	/**
	 * Number of milliseconds in a standard minute.
	 */
	public static final long	 MILLIS_PER_MINUTE	 = 60 * MILLIS_PER_SECOND;

	/**
	 * Number of milliseconds in a standard hour.
	 */
	public static final long	 MILLIS_PER_HOUR	 = 60 * MILLIS_PER_MINUTE;

	/**
	 * Number of milliseconds in a standard day.
	 */
	public static final long	 MILLIS_PER_DAY	     = 24 * MILLIS_PER_HOUR;
	
	protected Dates(){
		
	}
}