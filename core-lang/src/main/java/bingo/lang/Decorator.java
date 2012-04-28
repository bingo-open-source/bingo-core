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

/**
 * Base interface for decorations.
 *
 * <p>Implementations are expected to implement all methods of type T, and forward to the decorated instance by default.
 */
public interface Decorator<T> {
	
	/**
	 * Gets the object being decorated by this Decorator.
	 */
    T getDecorated();
}
