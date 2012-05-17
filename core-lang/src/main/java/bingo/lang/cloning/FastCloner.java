package bingo.lang.cloning;

import java.util.Map;

/**
 * @author kostantinos.kougios
 *
 * 21 May 2009
 */
interface FastCloner
{
	public Object clone(Object t, Cloner cloner, Map<Object, Object> clones) throws IllegalAccessException;
}
