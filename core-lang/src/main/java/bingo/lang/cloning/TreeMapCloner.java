package bingo.lang.cloning;

import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings({ "unchecked", "rawtypes" })
class TreeMapCloner implements TypeCloner<TreeMap> {
	
	public TreeMap clone(final Cloner cloner, final TreeMap t, final Map<Object, Object> clones, boolean deepClone) throws IllegalAccessException {
		final TreeMap<Object, Object> m = (TreeMap) t;
		final TreeMap result = new TreeMap(m.comparator());
		for (final Map.Entry e : m.entrySet()) {
			final Object key = cloner.clone(e.getKey(), clones, deepClone);
			final Object value = cloner.clone(e.getValue(), clones, deepClone);
			result.put(key, value);
		}
		return result;
	}
}
