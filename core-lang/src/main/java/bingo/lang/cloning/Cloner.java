package bingo.lang.cloning;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Pattern;

import bingo.lang.Assert;
import bingo.lang.Exceptions;
import bingo.lang.Objects;
import bingo.lang.Reflects;
import bingo.lang.annotations.Immutable;
import bingo.lang.exceptions.CloneException;
import bingo.lang.reflect.ReflectClass;
import bingo.lang.reflect.ReflectField;

@SuppressWarnings("rawtypes")
public class Cloner {
	private final Set<Class<?>>	            immutableClasses	 = new CopyOnWriteArraySet<Class<?>>();
	private final Set<Class<?>>	            immutableInstanceOf	 = new HashSet<Class<?>>();
	private final Map<Object, Boolean>	    immutables	         = new IdentityHashMap<Object, Boolean>(); //TODO : non thread safe

	private final Map<Class<?>, TypeCloner>	typeCloners	         = new ConcurrentHashMap<Class<?>, TypeCloner>();
	private final Map<Class<?>, TypeCloner>	typeClonerInstanceOf = new ConcurrentHashMap<Class<?>, TypeCloner>();

	private final Set<Class<?>>	            nullInstead	         = new HashSet<Class<?>>();

	public Cloner() {
		init();
	}
	
	public <T> T deepClone(final T o) {
		if (o == null) {
			return null;
		}
		
		final Map<Object, Object> clones = new IdentityHashMap<Object, Object>(16);
		try
		{
			return cloneInternal(o, clones, true);
		} catch (final IllegalAccessException e)
		{
			throw new CloneException("error during cloning of " + o, e);
		}
	}
	
	public <T> T deepClone(final T o, final Map<Object, Object> clones) {
		Assert.notNull(clones);
		try {
	        return cloneInternal(o, clones, true);
        } catch (IllegalAccessException e) {
        	throw new CloneException("error during cloning of " + o, e);
        }
	}
	
	public <T> T deepClone(final T from,final T to) {
		return deepClone(from, to, new IdentityHashMap<Object, Object>(16));
	}
	
    public <T> T deepClone(final T from,final T to,Map<Object, Object> clones){
    	Assert.notNull(clones);
		try {
	        return cloneInternal(from, to, clones, true);
        } catch (IllegalAccessException e) {
        	throw new CloneException("error during cloning of " + from, e);
        }
	}
    
	public <T> T deepCloneDontCloneInstances(final T o, final Object... dontCloneThese) {
		if (o == null) {
			return null;
		}
		final Map<Object, Object> clones = new IdentityHashMap<Object, Object>(16);
		for (final Object dc : dontCloneThese) {
			clones.put(dc, dc);
		}
		try {
			return cloneInternal(o, clones, true);
		} catch (final IllegalAccessException e) {
			throw new CloneException("error during cloning of " + o, e);
		}
	}
	
	public <T> T shallowClone(final T o) {
		if (o == null)
			return null;
		try {
			return cloneInternal(o, null, false);
		} catch (final IllegalAccessException e) {
			throw new CloneException("error during cloning of " + o, e);
		}
	}
	
	public <T> T shallowClone(final T from, final T to) {
		try {
			return cloneInternal(from, to, null, false);
		} catch (final IllegalAccessException e) {
			throw new CloneException("error during cloning of " + from, e);
		}
	}
	
	public <T> T clone(T o, boolean deepClone){
		return clone(o , deepClone ? new IdentityHashMap<Object, Object>(16) : null, deepClone);
	}
	
	public <T> T clone(T o, Map<Object, Object> clones,boolean deepClone) {
		try {
	        return cloneInternal(o, clones, deepClone);
        } catch (IllegalAccessException e) {
        	throw new CloneException("error during cloning of " + o, e);
        }
	}
	
	public void registerStaticFieldsAsImmutable(Class<?>... classes) {
		try {
	        for (final Class<?> c : classes) {
	        	final List<Field> fields = Reflects.getFields(c);
	        	for (final Field field : fields) {
	        		if(!field.isSynthetic() && Modifier.isStatic(field.getModifiers())){
	        			if(!field.isAccessible()){
	        				field.setAccessible(true);
	        			}
		        		final Object value = field.get(null);
		        		
		        		if(null != value && !isImmutable(value.getClass())){
		        			registerImmutableValue(value);
		        		}
	        		}
	        	}
	        }
        } catch (Exception e) {
        	throw Exceptions.uncheck(e);
        }
	}
	
	public void registerImmutableValue(Object immutable){
		immutables.put(immutable,true);
	}
	
	public void registerImmutableValue(final Class<?> c, final String... fields) {
		try {
			for(String name : fields){
				final Field field = c.getDeclaredField(name);
				field.setAccessible(true);
				final Object v = field.get(null);
				immutables.put(v, true);
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void registerImmutableClass(final Class<?>... c) {
		for (final Class<?> cls : c) {
			immutableClasses.add(cls);
		}
	}
	
	public void registerImmutableInstanceOf(final Class<?>... c){
		for(Class<?> cls : c){
			immutableInstanceOf.add(cls);
		}
	}

	public void registerTypeCloner(final Class<?> c, final TypeCloner<?> typeCloner) {
		if (typeCloners.containsKey(c)) {
			throw new IllegalArgumentException(c + " already register type cloner!");
		}
		typeCloners.put(c, typeCloner);
	}
	
	@SuppressWarnings("unchecked")
    protected <T> TypeCloner<T> findObjectCloner(final T o, final Class<?> c){
		TypeCloner<T> typeCloner = typeCloners.get(c);
		
		if(null == typeCloner){
			for(Entry<Class<?>, TypeCloner> entry : typeClonerInstanceOf.entrySet()){
				if(entry.getKey().isAssignableFrom(c)){
					typeCloners.put(c, entry.getValue());
					return entry.getValue();
				}
			}
		}
		
		return typeCloner;
	}
	
	private void init() {
		initImmutableClasses();
		initImmutableConstants();
		initObjectCloners();
	}

	protected void initObjectCloners() {
		typeCloners.put(GregorianCalendar.class, new CalendarCloner());
		
		typeCloners.put(ArrayList.class, new CollectionCloners.ArrayListCloner());
		typeCloners.put(LinkedList.class,new CollectionCloners.LinkedListCloner());
		typeCloners.put(CopyOnWriteArrayList.class,new CollectionCloners.CopyOnWriteArrayListCloner());
		
		typeCloners.put(HashSet.class, new CollectionCloners.HashSetCloner());
		typeCloners.put(LinkedHashSet.class, new CollectionCloners.LinkedHashSetCloner());
		typeCloners.put(TreeSet.class, new CollectionCloners.TreeSetCloner());
		typeCloners.put(CopyOnWriteArraySet.class, new CollectionCloners.CopyOnWriteArraySetCloner());
		
		typeCloners.put(HashMap.class, new MapCloners.HashMapCloner());
		typeCloners.put(TreeMap.class, new MapCloners.TreeMapCloner());
		typeCloners.put(LinkedHashMap.class, new MapCloners.LinkedHashMapCloner());
		typeCloners.put(ConcurrentHashMap.class, new MapCloners.ConcurrentHashMapCloner());
		
		typeCloners.put(Timestamp.class, new DateCloners.SqlTimestampCloner());
		typeCloners.put(Time.class, new DateCloners.SqlTimeCloner());
		typeCloners.put(java.sql.Date.class, new DateCloners.SqlDateCloner());
		typeCloners.put(Date.class, new DateCloners.DateCloner());
		
		typeCloners.put(StringBuilder.class, new BaseCloners.StringBuilderCloner());
		typeCloners.put(StringBuffer.class, new BaseCloners.StringBufferCloner());
		
		typeClonerInstanceOf.put(Collection.class, new CollectionCloners.CollectionCloner<Collection>());
		typeClonerInstanceOf.put(Map.class, new MapCloners.MapCloner<Map>());
	}

	protected void initImmutableClasses() {
		registerImmutableClass(String.class);
		registerImmutableClass(Integer.class);
		registerImmutableClass(Long.class);
		registerImmutableClass(Boolean.class);
		registerImmutableClass(Class.class);
		registerImmutableClass(Float.class);
		registerImmutableClass(Double.class);
		registerImmutableClass(Character.class);
		registerImmutableClass(Byte.class);
		registerImmutableClass(Short.class);
		registerImmutableClass(Void.class);
		registerImmutableClass(BigDecimal.class);
		registerImmutableClass(BigInteger.class);
		registerImmutableClass(URI.class);
		registerImmutableClass(URL.class);
		registerImmutableClass(UUID.class);
		registerImmutableClass(Pattern.class);
		registerImmutableInstanceOf(ClassLoader.class);
		registerImmutableInstanceOf(ThreadLocal.class);
	}

	protected void initImmutableConstants() {
		registerStaticFieldsAsImmutable(TreeSet.class, HashSet.class, HashMap.class, TreeMap.class);
	}

	protected <T> T newInstance(final ReflectClass<T> c)
	{
		if(c.hasDefaultConstructor()){
			return c.newInstance(); 
		}else if(c.canNewInstanceWithoutCallingConstructor()){
			return c.newInstanceWithoutCallingConstructor();
		}else{
			throw new CloneException("cannot new instance of calss '" + c.getName() + "'");
		}
	}

	protected boolean isImmutable(final Class<?> clz) {
		if (immutableClasses.contains(clz)) {
			return true;
		}

		for (final Annotation annotation : clz.getDeclaredAnnotations()) {
			if (annotation.annotationType() == Immutable.class) {
				immutableClasses.add(clz);
				return true;
			}
		}

		Class<?> c = clz.getSuperclass();
		while (c != null && c != Object.class) {
			for (final Annotation annotation : c.getDeclaredAnnotations()) {
				if (annotation.annotationType() == Immutable.class) {
//					final Immutable im = (Immutable) annotation;
//					if (im.subClass()) {
						immutableClasses.add(clz);
						return true;
//					}
				}
			}
			c = c.getSuperclass();
		}
		
		for (final Class<?> icls : immutableInstanceOf) {
			if (icls.isAssignableFrom(clz)) {
				immutableClasses.add(clz);
				return true;
			}
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
    protected <T> T cloneInternal(final T from,final T to,Map<Object,Object> clones,final boolean deepClone) throws IllegalAccessException {
		ReflectClass<T> clazz = (ReflectClass<T>)ReflectClass.get(from.getClass());
		
        for(ReflectField field : clazz.getFields()){
        	if(field.isStatic()){
        		continue;
        	}
        	
    		final Object value  = field.getValue(from, false);
    		final Object cloned = field.isSynthetic() ? value : (deepClone ? cloneInternal(value, clones, deepClone) : value);
    		
    		field.setValue(to, cloned, false);
        }
		
		return to;
	}
	
	@SuppressWarnings("unchecked")
    protected <T> T cloneInternal(final T o,final Map<Object,Object> clones,boolean deepClone) throws IllegalAccessException {
		if (o == null) {
			return null;
		}
		
		if (o == this) {
			return null;
		}
		
		if (o instanceof bingo.lang.Immutable || immutables.containsKey(o)) {
			return o;
		}
		
		final Class<T> clz = (Class<T>) o.getClass();
		
		if(clz.isPrimitive()){
			return o;
		}
		
		if (clz.isEnum()) {
			return o;
		}
		
		// skip cloning ignored classes
		if (nullInstead.contains(clz)) {
			return null;
		}
		
		if (isImmutable(clz)) {
			return o;
		}

		final Object clonedPreviously = clones != null ? clones.get(o) : null;
		if (clonedPreviously != null) {
			return (T) clonedPreviously;
		}
		
		//if shallow clone and object implements Cloneable
		if(!deepClone && o instanceof Cloneable){
			T cloned = Objects.clone(o);
			
			if(null != clones){
				clones.put(o, cloned);
			}
			
			return cloned;
		}
		
		TypeCloner<T> objectCloner = findObjectCloner(o, clz);
		if(null != objectCloner){
			final T cloned = objectCloner.clone(this, o, clones, deepClone);
			
			if(null != clones){
				clones.put(o, cloned);
			}
			
			return cloned;
		}
		
		if (clz.isArray()) {
			final int length    = Array.getLength(o);
			final T newInstance = (T) Array.newInstance(clz.getComponentType(), length);

			if (clones != null) {
				clones.put(o, newInstance);
			}

			for (int i = 0; i < length; i++) {
				final Object v = Array.get(o, i);
				final Object clone = clones != null ? deepClone(v, clones) : v;
				Array.set(newInstance, i, clone);
			}
			return newInstance;
		}
		
		final ReflectClass<T> reflectClass = ReflectClass.get(clz);
		final T newInstance = newInstance(reflectClass);
		if (clones != null) {
			clones.put(o, newInstance);
		}
		return cloneInternal(o, newInstance, clones, deepClone);
	}
}