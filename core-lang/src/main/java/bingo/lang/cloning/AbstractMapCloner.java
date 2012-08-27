package bingo.lang.cloning;

import java.util.Map;
import java.util.Set;

@SuppressWarnings({ "unchecked", "rawtypes" })
abstract class AbstractMapCloner<T extends Map> implements TypeCloner<T> {
	public T clone(final Cloner cloner, final T t, final Map<Object, Object> clones, boolean deepClone) throws IllegalAccessException {
		final T m = (T) t;
		final T result = getInstance((T) t);
		final Set<Map.Entry<Object, Object>> entrySet = m.entrySet();
		for (final Map.Entry e : entrySet) {
			final Object key = cloner.clone(e.getKey(), clones, deepClone);
			final Object value = cloner.clone(e.getValue(), clones, deepClone);
			result.put(key, value);
		}
		return result;
	}

	protected abstract T getInstance(T t);
}
