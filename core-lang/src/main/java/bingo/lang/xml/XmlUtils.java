package bingo.lang.xml;

import java.util.Stack;

import bingo.lang.Enumerable;
import bingo.lang.Func1;
import bingo.lang.Out;
import bingo.lang.Predicate;
import bingo.lang.Strings;
import bingo.lang.iterable.ImmutableIteratorBase;

final class XmlUtils {

	public static String escapeAttributeValue(String unescaped) {
		return escapeElementValue(unescaped); // TODO for now
	}

	public static String escapeElementValue(String unescaped) {
		return unescaped.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("'", "&apos;").replace("\"", "&quot;");
	}
	
	
	static class DepthFirstIterator<T> extends ImmutableIteratorBase<T> {

		private final Func1<T, Enumerable<T>> childrenFn;
		private final Stack<T> stack = new Stack<T>();

		public DepthFirstIterator(T startingNode, Func1<T, Enumerable<T>> childrenFn) {
			this.childrenFn = childrenFn;
			this.stack.add(startingNode);
		}
		
		@Override
	    protected boolean next(Out<T> out) throws Exception {
			// first child
			for (T child : childrenFn.apply(stack.peek())) {
				stack.push(child);
				return out.returns(child);
			}

			// no children
			while (stack.size() > 1) {
				T currentNode = stack.pop();

				// look for next sibling
				boolean foundSelf = false;
				for (T sibling : childrenFn.apply(stack.peek())) {
					if (foundSelf) {
						stack.push(sibling);
						return out.returns(sibling);
					}
					if (sibling.equals(currentNode)) {
						foundSelf = true;
					}
				}
				// no sibling found, move up and try again

			}

			return false;
	    }
	}
	
	static final class Predicates {

		public static Predicate<String> endsWith(final String suffix) {
			return new Predicate<String>() {
				public boolean apply(String input) {
					return input.endsWith(suffix);
				}
			};
		}

		public static Predicate<String> startsWith(final String prefix) {
			return new Predicate<String>() {
				public boolean apply(String input) {
					return input.startsWith(prefix);
				}
			};
		}

		public static <T extends XNameable> Predicate<T> xnameEquals(final XNameable xname) {
			return new Predicate<T>() {
				public boolean apply(T input) {
					return Strings.equals(xname.prefix(), xname.prefix()) && Strings.equals(xname.name(), xname.name());
				}
			};
		}

		public static <T extends XNameable> Predicate<T> xnameEquals(final String name) {
			return new Predicate<T>() {
				public boolean apply(T input) {
					return Strings.isEmpty(input.prefix()) && Strings.equals(input.name(), name);
				}
			};
		}

		public static <T> Predicate<T> not(final Predicate<T> predicate) {
			return new Predicate<T>() {
				public boolean apply(T input) {
					return !predicate.apply(input);
				}
			};
		}
	}
}
