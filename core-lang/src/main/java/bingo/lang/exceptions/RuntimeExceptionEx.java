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
package bingo.lang.exceptions;

import bingo.lang.Strings;

/**
 * <p>
 * Extends {@link RuntimeException} to allow {@link Strings#format(String, Object...)} in constructor.
 * </p>
 *
 * <pre>
 * Examples:
 * 
 * throw new RuntimeExceptionEx("the argument '{0}' could not be empty","name");
 * throw new RuntimeExceptionEx(cause,"the argument '{0}' could not be empty","name");
 * </pre>
 */
public class RuntimeExceptionEx extends RuntimeException {

    private static final long serialVersionUID = -288751546316286455L;
    
    /**
     * 默认构造方法，与原生 {@link RuntimeException}的构造方法效果一致。
     */
	public RuntimeExceptionEx() {
		super();
	}

	/**
	 * 指定消息message的构造方法，与原生的 {@link RuntimeException#RuntimeException(String)}构造方法一致。
	 * @param message 指定消息
	 */
	public RuntimeExceptionEx(String message) {
		super(message);
	}
	
	/**
	 * 指定消息message和原因cause的构造方法，
	 * 与原生的 {@link RuntimeException#RuntimeException(String, Throwable)}构造方法一致。
	 * @param message 指定消息。
	 * @param cause 指定原因。
	 */
	public RuntimeExceptionEx(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * 指定原因cause的构造方法，与原生的 {@link RuntimeException#RuntimeException(Throwable)}构造方法一致。
	 * @param cause 指定原因。
	 */
	public RuntimeExceptionEx(Throwable cause) {
		super(cause);
	}
	
	/**
	 * 指定可格式化参数的消息的构造方法。
	 * <pre>
	 * 示例：
	 * RuntimeException("message is {0}", "hello") = RuntimeException("message is hello.")
	 * </pre>
	 * @param message 可格式化参数的消息。
	 * @param args 用于格式化的参数。
	 */
	public RuntimeExceptionEx(String message,Object... args) {
		super(Strings.format(message, args));
	}
	
	/**
	 * 指定原因cause和可格式化参数的消息message以及参数args的构造方法。
	 * <pre>
	 * 示例：
	 * RuntimeException(cause, "message is {0}", "hello") = RuntimeException("message is hello.", cause)
	 * </pre>
	 * @param cause 指定的原因。
	 * @param message 可格式化参数的消息。
	 * @param args 用于格式化的参数。
	 */
	public RuntimeExceptionEx(Throwable cause,String message,Object... args) {
		super(Strings.format(message, args),cause);
	}
}