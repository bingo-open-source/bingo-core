package bingo.lang.cloning;

import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("rawtypes")
class ArrayListCloner implements TypeCloner<ArrayList> {
	
	@SuppressWarnings("unchecked")
    public ArrayList clone(final Cloner cloner, final ArrayList t, final Map<Object, Object> clones, boolean deepClone) throws IllegalAccessException {
		final ArrayList al = (ArrayList) t;
		final ArrayList l = new ArrayList();
		for (final Object o : al) {
			final Object cloneInternal = cloner.clone(o, clones, deepClone);
			l.add(cloneInternal);
		}
		return l;
	}

}
