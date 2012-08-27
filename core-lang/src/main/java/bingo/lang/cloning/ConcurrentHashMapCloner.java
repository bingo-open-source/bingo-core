package bingo.lang.cloning;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({ "unchecked", "rawtypes" })
class ConcurrentHashMapCloner implements TypeCloner<ConcurrentHashMap> {
	
	public ConcurrentHashMap clone(final Cloner cloner,final ConcurrentHashMap t,final Map<Object, Object> clones,boolean deepClone) throws IllegalAccessException {
		final ConcurrentHashMap<Object, Object> m = (ConcurrentHashMap) t;
		final ConcurrentHashMap result = new ConcurrentHashMap();
		for (final Map.Entry e : m.entrySet()) {
			final Object key = cloner.clone(e.getKey(), clones, deepClone);
			final Object value = cloner.clone(e.getValue(), clones, deepClone);

			result.put(key, value);
		}
		return result;
	}
}