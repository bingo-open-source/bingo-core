package bingo.lang;

import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import bingo.lang.iterable.ReadOnlyIterator;

public class EnumeratorTest extends TestCase {

	@Test
	public void testEnumerable() {

		Assert.assertEquals(5, Enumerator.range(1, 5).size());
		Assert.assertEquals((Integer) 1, Enumerator.range(1, 5).first());
		Assert.assertEquals((Integer) 5, Enumerator.range(1, 5).last());
		Assert.assertEquals((Integer) 3, Enumerator.range(1, 5).elementAt(2));
		Assert.assertEquals(null, Enumerator.empty(Integer.class).firstOrNull());
		Assert.assertEquals("1", Enumerator.create(1).join(","));

		Assert.assertEquals("1,2,3,4,5", Enumerator.range(1, 5).join(","));
		Assert.assertEquals("5,4,3,2,1", Enumerator.range(1, 5).reverse().join(","));
		Assert.assertEquals("10", Enumerator.range(10, 1).join(","));
		Assert.assertEquals("1", Enumerator.range(1, 1000000).take(1).join(","));
		Assert.assertEquals("3,4,5", Enumerator.range(1, 5).skip(2).join(","));
		Assert.assertEquals("2,3,4,5", Enumerator.range(1, 5).skipWhile(IS_ODD).join(","));
		Assert.assertEquals((Integer) 10, Enumerator.range(1, 4).sum(Integer.class));

		Enumerator<Integer> one = Enumerator.create(5, 3, 9, 7, 5, 9, 3, 7);
		Enumerator<Integer> two = Enumerator.create(8, 3, 6, 4, 4, 9, 1, 0);

		Assert.assertEquals((Integer) 0, two.min(IDENTITY));
		Assert.assertEquals((Integer) 9, two.max(IDENTITY));
		Assert.assertEquals("5,3,9,7", one.distinct().join(","));
		Assert.assertEquals("5,3,9,7,8,6,4,1,0", one.union(two).join(","));
		Assert.assertEquals("5,3,9,7,5,9,3,7,8,3,6,4,4,9,1,0", one.concat(two).join(","));
		Assert.assertEquals("3,9", one.intersect(two).join(","));

		Assert.assertEquals("3,9,1", two.where(IS_ODD).join(","));
		Assert.assertEquals("8,6,4,4,0", two.where(not(IS_ODD)).join(","));
		Assert.assertEquals("2,4,6,8,10", Enumerator.range(1, 5).select(TIMES_TWO).join(","));

		Assert.assertEquals("onetwothree", Enumerator.create("one", "two", "three").selectMany(CHARS).join(""));

		// test using an infinite iterator - none of these methods should materialize the enumerable
		Assert.assertEquals("1,1", infinite(1).skip(100).take(2).join(","));
		Assert.assertEquals(true, infinite(1).any(IS_ODD));
		Assert.assertEquals(true, infinite(1).contains(1));
		Assert.assertEquals((Integer) 1, infinite(1).first());
		Assert.assertEquals((Integer) 1, infinite(1).elementAt(100));
		Assert.assertEquals((Integer) 2, infinite(1).select(TIMES_TWO).first());
		Assert.assertEquals((Integer) 1, infinite(1).where(IS_ODD).first());
		Assert.assertEquals((Integer) 1, infinite(1).cast(Integer.class).first());
		Assert.assertEquals("oneone", infinite("one").selectMany(CHARS).take(6).join(""));
		Assert.assertEquals("1,1", infinite(1).concat(infinite(1)).take(2).join(","));
	}

	private static <T> Enumerator<T> infinite(final T value) {
		return Enumerator.create(new Func<Iterator<T>>() {
			public Iterator<T> evaluate() {
				return new InfiniteIterator<T>(value);
			}
		});
	}

	private static class InfiniteIterator<T> extends ReadOnlyIterator<T> {

		private final T value;

		public InfiniteIterator(T value) {
			this.value = value;
		}
		
		@Override
        protected boolean next(Out<T> out) throws Exception {
	        out.setValue(value);
	        return true;
        }
	}

	private static final Func1<Integer, Integer> IDENTITY = identity(Integer.class);

	private static final Func1<Integer, Integer> TIMES_TWO = new Func1<Integer, Integer>() {
		public Integer evaluate(Integer input) {
			return input * 2;
		}
	};

	private static final Predicate<Integer> IS_ODD = new Predicate<Integer>() {
		public boolean evaluate(Integer input) {
			return input % 2 == 1;
		}
	};

	private static final Func1<String, Enumerator<Character>> CHARS = new Func1<String, Enumerator<Character>>() {
		public Enumerator<Character> evaluate(String input) {
			return chars(input);
		}
	};

	private static Enumerator<Character> chars(String value) {
		return chars(value.toCharArray());
	}

	private static Enumerator<Character> chars(char[] chars) {
		Character[] rt = new Character[chars.length];
		for (int i = 0; i < chars.length; i++) {
			rt[i] = chars[i];
		}
		return Enumerator.create(rt);
	}

	private static <T> Predicate<T> not(final Predicate<T> predicate) {
		return new Predicate<T>() {
			public boolean evaluate(T input) {
				return !predicate.evaluate(input);
			}
		};
	}

	private static <TResult> Func1<TResult, TResult> identity(Class<TResult> clazz) {
		return new Func1<TResult, TResult>() {
			public TResult evaluate(TResult input) {
				return input;
			}
		};
	}
}
