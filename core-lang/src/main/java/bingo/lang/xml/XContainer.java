package bingo.lang.xml;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import bingo.lang.Enumerable;
import bingo.lang.Func;
import bingo.lang.Func1;
import bingo.lang.Out;
import bingo.lang.iterable.ImmutableIteratorBase;
import bingo.lang.xml.XUtils.DepthFirstIterator;
import bingo.lang.xml.XUtils.Predicates;

public abstract class XContainer extends XNode {

	private static final Func1<XElement, Enumerable<XElement>> CHILD_ELEMENTS = new Func1<XElement, Enumerable<XElement>>() {
		public Enumerable<XElement> apply(XElement input) {
			return input.elements();
		}
	};

	protected static XElement parse(Element domElement) {
		if (domElement == null) {
			return null;
		}
		XElement rt = new XElement(domElement.getTagName());
		for (int i = 0; i < domElement.getAttributes().getLength(); i++) {
			Attr attr = (Attr) domElement.getAttributes().item(i);
			XAttribute xatt = new XAttribute(attr.getName(), attr.getValue());
			rt.add(xatt);
		}
		for (Node childNode : domNodes(domElement.getChildNodes())) {
			XNode xchild = parseNode(childNode);
			rt.add(xchild);
		}
		return rt;
	}

	protected static XNode parseNode(Node domNode) {
		if (domNode instanceof Element) {
			return XElement.parse((Element) domNode);
		}
		if (domNode instanceof Text) {
			return new XText(((Text) domNode).getTextContent());
		}
		if (domNode instanceof Comment) {
			return new XComment(((Comment) domNode).getTextContent());
		}
		if (domNode instanceof ProcessingInstruction) {
			return new XProcessingInstruction(((ProcessingInstruction) domNode).getTarget(), ((ProcessingInstruction) domNode).getData());
		}
		if (domNode instanceof DocumentType) {
			return new XDocumentType(((DocumentType) domNode).getName(), ((DocumentType) domNode).getInternalSubset());
		}
		throw new UnsupportedOperationException("implement " + domNode);
	}

	private List<XNode> childNodes = new ArrayList<XNode>();

	protected XContainer() {
		
	}

	protected abstract XElement element();

	public Enumerable<XElement> elements() {
		return nodes().ofType(XElement.class);
	}

	public Enumerable<XElement> elements(String name) {
		return nodes().ofType(XElement.class).where(Predicates.<XElement> xnameEquals(name));
	}

	public XElement element(String name) {
		return elements().firstOrNull(Predicates.<XElement> xnameEquals(name));
	}

	public Enumerable<XNode> nodes() {
		return Enumerable.of(childNodes);
	}

	public Enumerable<XElement> descendants() {
		return Enumerable.of(new Func<Iterator<XElement>>() {
			public Iterator<XElement> apply() {
				return new DepthFirstIterator<XElement>(element(), CHILD_ELEMENTS);
			}
		});
	}

	public Enumerable<XElement> descendants(final String name) {
		return descendants().where(Predicates.<XElement> xnameEquals(name));
	}

	void removeNode(XNode xNode) {
		childNodes.remove(xNode);
	}

	public void add(Object... content) {
		for (Object obj : content) {
			add(obj);
		}
	}

	public void add(Object content) {
		XNode node = toNode(content);
		childNodes.add(node);
		if (this instanceof XElement) {
			node.setParent((XElement) this);
		}
	}

	public void addFirst(Object... content) {
		for (Object obj : Enumerable.of(content).reverse()) {
			addFirst(obj);
		}
	}

	public void addFirst(Object content) {
		XNode node = toNode(content);
		childNodes.add(0, node);
		if (this instanceof XElement) {
			node.setParent((XElement) this);
		}
	}

	@Override
	public String toString() {
		return element().toString();
	}

	@Override
	public String toString(XFormat format) {
		return element().toString(format);
	}

	protected static Enumerable<Node> domNodes(final NodeList nodes) {
		return Enumerable.of(new Func<Iterator<Node>>() {
			public Iterator<Node> apply() {
				return new NodeListIterator(nodes);
			}
		});
	}

	protected static String domPrettyPrint(Node xml, int indent) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		domPrettyPrint(xml, indent, baos);
		try {
			return new String(baos.toByteArray(), "UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	protected static void domPrettyPrint(Node xml, int indent, OutputStream out) {
		try {
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
			tf.transform(new DOMSource(xml), new StreamResult(out));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private XNode toNode(Object content) {
		if (content instanceof XNode) {
			return (XNode) content;
		}
		if (content instanceof String) {
			return new XText((String) content);
		}
		throw new UnsupportedOperationException("Unknown content: " + content);
	}
	
	@SuppressWarnings("unused")
	private static Enumerable<Element> domElements(Element parent) {
		return domNodes(parent.getChildNodes()).ofType(Element.class);
	}
	
	private static class NodeListIterator extends ImmutableIteratorBase<Node> {

		private final NodeList nodes;
		private int index = 0;

		public NodeListIterator(NodeList nodes) {
			this.nodes = nodes;
		}
		
		@Override
        protected boolean next(Out<Node> out) throws Exception {
			Node node = nodes.item(index++);
			if (node == null) {
				return false;
			}
			return out.returns(node);
        }
	}
}