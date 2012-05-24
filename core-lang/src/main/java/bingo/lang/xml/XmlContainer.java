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

import bingo.lang.Collections;
import bingo.lang.Enumerable;
import bingo.lang.Func;
import bingo.lang.Func1;
import bingo.lang.Out;
import bingo.lang.iterable.ImmutableIteratorBase;
import bingo.lang.xml.XmlUtils.DepthFirstIterator;
import bingo.lang.xml.XmlUtils.Predicates;

abstract class XmlContainer extends XmlNode {

	private static final Func1<XmlElement, Enumerable<XmlElement>> CHILD_ELEMENTS = new Func1<XmlElement, Enumerable<XmlElement>>() {
		public Enumerable<XmlElement> apply(XmlElement input) {
			return input.childElements();
		}
	};

	private List<XmlNode> childNodes = new ArrayList<XmlNode>();

	protected XmlContainer() {
		
	}
	
	public boolean hasChilds(){
		return !childNodes.isEmpty();
	}

	public Enumerable<XmlElement> childElements() {
		return childNodes().ofType(XmlElement.class);
	}

	public Enumerable<XmlElement> childElements(String name) {
		return childNodes().ofType(XmlElement.class).where(Predicates.<XmlElement> xnameEquals(name));
	}

	public XmlElement childElement(String name) {
		return childElements().firstOrNull(Predicates.<XmlElement> xnameEquals(name));
	}
	
	public XmlElement fistChildElement(){
		return childElements().firstOrNull();
	}

	public Enumerable<XmlNode> childNodes() {
		return Enumerable.of(childNodes);
	}
	
	public XmlNode firstChildNode(){
		return Collections.firstOrNull(childNodes);
	}

	public Enumerable<XmlElement> descendantElements() {
		return Enumerable.of(new Func<Iterator<XmlElement>>() {
			public Iterator<XmlElement> apply() {
				return new DepthFirstIterator<XmlElement>(element(), CHILD_ELEMENTS);
			}
		});
	}

	public Enumerable<XmlElement> descendantElements(final String name) {
		return descendantElements().where(Predicates.<XmlElement> xnameEquals(name));
	}

	public boolean remove(XmlNode xNode) {
		return childNodes.remove(xNode);
	}

	public void add(Object... content) {
		for (Object obj : content) {
			add(obj);
		}
	}

	public void add(Object content) {
		XmlNode node = toNode(content);
		childNodes.add(node);
		if (this instanceof XmlElement) {
			node.setParent((XmlElement) this);
		}
		node.setDocument(document());
	}

	public void addFirst(Object... content) {
		for (Object obj : Enumerable.of(content).reverse()) {
			addFirst(obj);
		}
	}

	public void addFirst(Object content) {
		XmlNode node = toNode(content);
		childNodes.add(0, node);
		if (this instanceof XmlElement) {
			node.setParent((XmlElement) this);
		}
		node.setDocument(document());
	}
	
	protected abstract XmlElement element();

	@Override
	public String toString() {
		return element().toString();
	}

	@Override
	public String toXml(XmlFormat format) {
		return element().toXml(format);
	}
	
	private XmlNode toNode(Object content) {
		if (content instanceof XmlNode) {
			return (XmlNode) content;
		}
		
		if (content instanceof String) {
			return new XmlText((String) content);
		}
		
		throw new UnsupportedOperationException("Unknown content: " + content);
	}
	
	protected static XmlElement parse(XmlDocument document, Element domElement) {
		if (domElement == null) {
			return null;
		}
		
		XmlElement rt = new XmlElement(domElement.getPrefix(),domElement.getTagName());
		
		rt.setDocument(document);
		
		for (int i = 0; i < domElement.getAttributes().getLength(); i++) {
			Attr attr = (Attr) domElement.getAttributes().item(i);
			XmlAttribute xatt = new XmlAttribute(attr.getName(), attr.getValue());
			rt.add(xatt);
		}
		
		for (Node childNode : domNodes(domElement.getChildNodes())) {
			XmlNode xchild = parseNode(document,childNode);
			rt.add(xchild);
		}
		
		return rt;
	}

	protected static XmlNode parseNode(XmlDocument document, Node domNode) {
		if (domNode instanceof Element) {
			return XmlElement.parse(document,(Element) domNode);
		}
		if (domNode instanceof Text) {
			return new XmlText(((Text) domNode).getTextContent());
		}
		if (domNode instanceof Comment) {
			return new XmlComment(((Comment) domNode).getTextContent());
		}
		if (domNode instanceof ProcessingInstruction) {
			return new XmlProcessingInstruction(((ProcessingInstruction) domNode).getTarget(), ((ProcessingInstruction) domNode).getData());
		}
		if (domNode instanceof DocumentType) {
			return new XmlDocumentType(((DocumentType) domNode).getName(), ((DocumentType) domNode).getInternalSubset());
		}
		throw new UnsupportedOperationException("implement " + domNode);
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