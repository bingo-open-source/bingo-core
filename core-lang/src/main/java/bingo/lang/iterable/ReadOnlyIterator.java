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
package bingo.lang.iterable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import bingo.lang.Assert;
import bingo.lang.OutObject;
import bingo.lang.Out;

public abstract class ReadOnlyIterator<T> implements Iterator<T> {

	private Boolean	     hasNext	= null;
	private T	         current	= null;
	private OutObject<T>	out	 	= new OutObject<T>();

	public boolean hasNext() {
		if (hasNext == null) {
			out.reset();

			try {
				hasNext = next(out);
				current = out.getValue();
				
				if(hasNext){
					Assert.isTrue(out.hasValue(),"next() returns true but no output value");
				}
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return hasNext;
	}

	public T next() {
		if (this.hasNext()) {
			hasNext = null;
			return current;
		} else {
			throw new NoSuchElementException();
		}
	}

	public void remove() {
		throw new UnsupportedOperationException("remove()");
	}

	protected abstract boolean next(Out<T> out) throws Exception;
}