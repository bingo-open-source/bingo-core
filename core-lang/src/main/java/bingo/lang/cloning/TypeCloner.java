package bingo.lang.cloning;

import java.util.Map;

public interface TypeCloner<T> {
	public T clone(Cloner cloner, T object, Map<Object, Object> clones,boolean deepClone) throws IllegalAccessException;
}