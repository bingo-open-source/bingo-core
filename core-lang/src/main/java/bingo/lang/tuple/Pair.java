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
package bingo.lang.tuple;

public interface Pair<L, R> {

	/**
	 * <p>
	 * Gets the left element from this pair.
	 * </p>
	 * 
	 * <p>
	 * When treated as a key-value pair, this is the key.
	 * </p>
	 * 
	 * @return the left element, may be null
	 */
	L getLeft();

	/**
	 * <p>
	 * Gets the right element from this pair.
	 * </p>
	 * 
	 * <p>
	 * When treated as a key-value pair, this is the value.
	 * </p>
	 * 
	 * @return the right element, may be null
	 */
	R getRight();

}