package bingo.lang.cloning;

import java.util.GregorianCalendar;
import java.util.Map;

/**
 * @author kostantinos.kougios
 *
 * 21 May 2009
 */
class FastClonerCalendar implements FastCloner
{
	public Object clone(final Object t, final Cloner cloner, final Map<Object, Object> clones)
	{
		final GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(((GregorianCalendar) t).getTimeInMillis());
		return gc;
	}
}
