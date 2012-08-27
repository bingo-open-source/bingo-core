package bingo.lang.cloning;

import java.util.HashSet;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
class HashSetCloner implements TypeCloner<HashSet> {
	
	public HashSet clone(final Cloner cloner, final HashSet t, final Map<Object, Object> clones, boolean deepClone) throws IllegalAccessException {
		final HashSet al = (HashSet) t;
		final HashSet l = new HashSet();
		for (final Object o : al) {
			final Object cloneInternal = cloner.clone(o, clones, deepClone);
			l.add(cloneInternal);
		}
		return l;
	}
}
