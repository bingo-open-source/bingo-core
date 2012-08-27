package bingo.lang.cloning;

import java.util.LinkedList;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
class LinkedListCloner implements TypeCloner<LinkedList> {
	
	public LinkedList clone(final Cloner cloner, final LinkedList t, final Map<Object, Object> clones, boolean deepClone) throws IllegalAccessException {
		final LinkedList al = (LinkedList) t;
		final LinkedList l = new LinkedList();
		for (final Object o : al) {
			l.add(cloner.clone(o, clones, deepClone));
		}
		return l;
	}
}
