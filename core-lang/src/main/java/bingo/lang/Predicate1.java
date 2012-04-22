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
 * Defines a functor interface implemented by classes that perform a predicate test on an object.
 * 
 * @author fenghm (fenghm@bingosoft.net)
 * 
 * @since 0.1
 */
public interface Predicate1<T,R> {

    /**
     * Use the specified parameter to perform a test that returns true or false.
     *
     * @param object  the object to evaluate, should not be changed
     * 
     * @return true or false
     * 
     * @throws ClassCastException (runtime) if the input is the wrong class
     * 
     * @throws IllegalArgumentException (runtime) if the input is invalid
     * 
     * @throws RuntimeException (runtime) if the predicate encounters a problem
     */
	boolean evaluate(T object,Mutable<R> result);
}