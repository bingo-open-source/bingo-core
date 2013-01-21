/*
 * Copyright 2013 the original author or authors.
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
package bingo.lang.logging;

import bingo.lang.Valued;

public enum LogLevel implements Valued<Integer>{
	Trace(5),
	Debug(4),
	Info(3),
	Warn(2),
	Error(1),
	Fatal(0);
	
	private final int value;
	
	private LogLevel(int value) {
		this.value = value;
    }

	public Integer getValue() {
	    return value;
    }
	
	public boolean isTraceEnabled(){
		return value == Trace.value;
	}
	
	public boolean isDebugEnabled(){
		return value > Info.value;
	}
	
	public boolean isInfoEnabled(){
		return value > Warn.value;
	}
	
	public boolean isWarnEnabled(){
		return value > Error.value;
	}
	
	public boolean isErrorEnabled(){
		return value > Fatal.value;
	}
	
	public boolean isFatalEnabled(){
		return value >= Fatal.value;
	}
}