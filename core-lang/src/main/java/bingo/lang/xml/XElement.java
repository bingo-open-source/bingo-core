package bingo.lang.xml;

import java.util.ArrayList;
import java.util.List;

import bingo.lang.Enumerable;
import bingo.lang.xml.XUtils.Predicates;

public class XElement extends XContainer implements XNameable {

	private XName xname;
	private final List<XAttribute> attributes = new ArrayList<XAttribute>();

	public XElement(String name, Object... content) {
		this.xname = new XName(null, name);
		for (Object obj : content) {
			add(obj);
		}
	}
	
	public String getLocalName() {
	    return null != xname ? xname.getLocalName() : null;
    }

	@Override
	public void add(Object content) {
		if (content instanceof XAttribute) {
			attributes.add((XAttribute) content);
		} else {
			super.add(content);
		}
	}

	@Override
	public XNodeType nodeType() {
		return XNodeType.ELEMENT;
	}

	public Enumerable<XAttribute> attributes() {
		return Enumerable.of(attributes);
	}

	public XAttribute attribute(String name) {
		return attributes().firstOrNull(Predicates.<XAttribute> xnameEquals(name));
	}

	@Override
	protected XElement element() {
		return this;
	}

	public XName getName() {
		return xname;
	}

	@Override
	public String toString() {
		return toString(XFormat.NOT_INDENTED);
	}

	@Override
	public String toString(XFormat format) {
		boolean enableIndent = format.isIndentEnabled();
		String indent = enableIndent ? getIndent(format) : "";
		String newline = enableIndent ? "\n" : "";

		String tagName = getName().getLocalName();

		StringBuilder sb = new StringBuilder();
		sb.append(indent);
		sb.append('<');
		sb.append(tagName);

		for (XAttribute att : attributes) {
			sb.append(' ');
			sb.append(att.getName().getLocalName());
			sb.append("=\"");
			sb.append(XUtils.escapeAttributeValue(att.getValue()));
			sb.append('"');
		}

		List<XNode> nodes = nodes().toList();
		if (nodes.size() == 0) {
			sb.append(" />");
		} else {
			sb.append('>');
			boolean onlyText = true;
			for (XNode node : nodes()) {
				if (node.nodeType() == XNodeType.TEXT) {
					sb.append(node.toString(format));
				} else {
					onlyText = false;
					sb.append(newline);
					sb.append(node.toString(format.incrementLevel()));
				}
			}
			if (!onlyText) {
				sb.append(newline + indent);
			}
			sb.append("</");
			sb.append(tagName);
			sb.append('>');
		}
		return sb.toString();
	}

	public String getValue() {
		XText firstText = nodes().ofType(XText.class).firstOrNull();
		return firstText == null ? null : firstText.getValue();
	}
}
