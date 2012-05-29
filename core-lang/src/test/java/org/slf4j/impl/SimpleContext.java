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
package org.slf4j.impl;

public class SimpleContext {

	private static final ThreadLocal<SimpleContext> ctx = new ThreadLocal<SimpleContext>();
	
	public static SimpleContext get(){
		SimpleContext context = ctx.get();
		
		if(null == context){
			context = new SimpleContext();
			ctx.set(context);
		}
		
		return context;
	}
	
	public boolean isTraceEnabled = true;
	public boolean isDebugEnabled = true;
	public boolean isInfoEnabled  = true;
	public boolean isWarnEnabled  = true;
	public boolean isErrorEanbled = true;

	private String     msg;
	private Throwable throwable;

	public String msg(){
		return msg;
	}
	
	public Throwable throwable(){
		return throwable;
	}
	
	SimpleContext reset() {
		this.msg       = null;
		this.throwable = null;
		return this;
	}

	void log(String msg,Throwable throwable){
		this.msg       = msg;
		this.throwable = throwable;
	}
}
