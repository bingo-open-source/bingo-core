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

import java.io.Serializable;


/**
 * An {@link Out} <code>Object</code> wrapper.
 */
public class OutObject<T> implements Mutable<T>, Out<T>, Serializable {

	private static final long	serialVersionUID	= 32234969580844058L;

	private T	              value;
	private boolean	      output;

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
		this.output = true;
	}
	
	public boolean returns(T value) {
		this.value = value;
		this.output = true;
		return true;
    }

	public boolean hasValue() {
		return output;
	}

	public void reset() {
		this.value = null;
		this.output = false;
	}

	//-----------------------------------------------------------------------
	/**
	 * <p>
	 * Compares this object against the specified object. The result is <code>true</code> if and only if the argument is
	 * not <code>null</code> and is a <code>OutObject</code> object that contains the same <code>T</code> value as this
	 * object.
	 * </p>
	 * 
	 * @param obj the object to compare with, <code>null</code> returns <code>false</code>
	 * @return <code>true</code> if the objects are the same; <code>true</code> if the objects have equivalent
	 *         <code>value</code> fields; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (this.getClass() == obj.getClass()) {
			OutObject<?> that = (OutObject<?>) obj;
			return Objects.equals(this.value, that.value);
		} else {
			return false;
		}
	}

	/**
	 * Returns the value's hash code or <code>0</code> if the value is <code>null</code>.
	 * 
	 * @return the value's hash code or <code>0</code> if the value is <code>null</code>.
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	//-----------------------------------------------------------------------
	/**
	 * Returns the String value of this mutable.
	 * 
	 * @return the mutable value as a string
	 */
	@Override
	public String toString() {
		return value == null ? "null" : value.toString();
	}
}