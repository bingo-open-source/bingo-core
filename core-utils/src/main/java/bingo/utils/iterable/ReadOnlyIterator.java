package bingo.utils.iterable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import bingo.lang.Out;
import bingo.utils.Assert;
import bingo.utils.mutable.OutObject;

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
					Assert.isTrue(out.hasOutput(),"next() returns true but no output value");
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