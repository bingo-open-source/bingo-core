package bingo.lang.cloning;

import java.util.GregorianCalendar;
import java.util.Map;

class CalendarCloner implements TypeCloner<GregorianCalendar> {
	
	public GregorianCalendar clone(final Cloner cloner,final GregorianCalendar t,final Map<Object, Object> clones,boolean deepClone) throws IllegalAccessException {
		final GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(((GregorianCalendar) t).getTimeInMillis());
		return gc;
	}
	
}