package bingo.lang.cloning;

import java.util.HashSet;
import java.util.Map;

/**
 * @author kostantinos.kougios
 *
 * 21 May 2009
 */
class FastClonerHashSet implements FastCloner
{
	@SuppressWarnings({ "unchecked"})
	public Object clone(final Object t, final Cloner cloner, final Map<Object, Object> clones) throws IllegalAccessException
	{
		final HashSet al = (HashSet) t;
		final HashSet l = new HashSet();
		for (final Object o : al)
		{
			final Object cloneInternal = cloner.cloneInternal(o, clones);
			l.add(cloneInternal);
		}
		return l;
	}
}
