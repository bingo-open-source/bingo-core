package bingo.lang.xml;

import java.util.Stack;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bingo.lang.Enumerable;
import bingo.lang.Func1;
import bingo.lang.Out;
import bingo.lang.Predicate;
import bingo.lang.Strings;
import bingo.lang.Sys;
import bingo.lang.iterable.ImmutableIteratorBase;

final class XmlUtils {
	
	static String getElementText(Element element){
		if(Sys.IS_ANDROID){
	        StringBuilder buffer = new StringBuilder();
	        NodeList childList = element.getChildNodes();
	        for (int i = 0; i < childList.getLength(); i++) {
	            Node child = childList.item(i);
	            if (child.getNodeType() == Node.TEXT_NODE)
	                buffer.append(child.getNodeValue());
	        }
	        return buffer.toString();			
		}else{
			return element.getTextContent();
		}
	}

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

		public static <T extends XmlNamed> Predicate<T> xnameEquals(final XmlNamed xname) {
			return new Predicate<T>() {
				public boolean apply(T input) {
					return Strings.equals(xname.prefix(), input.prefix()) && Strings.equals(xname.name(), input.name());
				}
			};
		}

		public static <T extends XmlNamed> Predicate<T> xnameEquals(final String name) {
			return new Predicate<T>() {
				public boolean apply(T input) {
					return Strings.equals(input.name(), name);
				}
			};
		}
		
		public static <T extends XmlNamed> Predicate<T> xnameEquals(final String prefix,final String name) {
			return new Predicate<T>() {
				public boolean apply(T input) {
					return Strings.equals(prefix, input.prefix()) && Strings.equals(input.name(), name);
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
