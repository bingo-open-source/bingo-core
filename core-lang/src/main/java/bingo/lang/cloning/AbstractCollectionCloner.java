package bingo.lang.cloning;

import java.util.Collection;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
abstract class AbstractCollectionCloner<T extends Collection> implements TypeCloner<T> {
	public abstract T newInstance(T o);

	public T clone(final Cloner cloner, final T t, final Map<Object, Object> clones, boolean deepClone) throws IllegalAccessException {
		final T c = newInstance((T) t);
		final T l = (T) t;
		for (final Object o : l) {
			final Object clone = cloner.clone(o, clones, deepClone);
			c.add(clone);
		}
		return c;
	}
}
