package bingo.lang.cloning;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(TYPE)
@Retention(RUNTIME)
@interface CloneImmutable {
	/**
	 * by default all subclasses of the {@link CloneImmutable} class are not immutable. This can override it.
	 */
	boolean subClass() default false;
}
