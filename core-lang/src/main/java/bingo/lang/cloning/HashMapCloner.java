package bingo.lang.cloning;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
class HashMapCloner implements TypeCloner<HashMap> {
	
	public HashMap clone(final Cloner cloner, final HashMap t, final Map<Object, Object> clones, boolean deepClone) throws IllegalAccessException {
		final HashMap<Object, Object> m = t;
		final HashMap result = new HashMap();
		for (final Map.Entry e : m.entrySet()) {
			final Object key = cloner.clone(e.getKey(), clones, deepClone);
			final Object value = cloner.clone(e.getValue(), clones, deepClone);

			result.put(key, value);
		}
		return result;
	}
}