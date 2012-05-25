package bingo.lang.xml;

import java.util.ArrayList;
import java.util.List;

import bingo.lang.Enumerable;
import bingo.lang.Strings;
import bingo.lang.xml.XmlUtils.Predicates;

public class XmlElement extends XmlContainer implements XNameable {

	private final String name;
	private final String prefix;
	private final List<XmlAttribute> attributes = new ArrayList<XmlAttribute>();
	
	public XmlElement(String name, Object... content) {
		this(null,name,content);
	}	

	public XmlElement(String prefix, String name, Object... content) {
		this.prefix = prefix;
		this.name   = name;
		
		for (Object obj : content) {
			add(obj);
		}
	}
	
	public String name() {
	    return name;
    }

	public String prefix() {
	    return prefix;
    }

	public String text() {
		XmlText firstText = childNodes().ofType(XmlText.class).firstOrNull();
		return firstText == null ? null : firstText.value();
	}
	
	public boolean hasAttributes(){
		return !attributes.isEmpty();
	}
	
	public Enumerable<XmlAttribute> attributes() {
		return Enumerable.of(attributes);
	}

	public XmlAttribute attribute(String name) {
		return attributes().firstOrNull(Predicates.<XmlAttribute> xnameEquals(name));
	}
	
	public String attributeValue(String name) {
		XmlAttribute attr = attributes().firstOrNull(Predicates.<XmlAttribute> xnameEquals(name));
		
		return null == attr ? null : attr.value(); 
	}
	
	public String attributeOrText(String attributeName){
		XmlAttribute attr = attribute(attributeName);
		
		return attr == null ? text() : attr.value();
	}
	
	public String childElementText(String name) {
		XmlElement e = childElement(name);
		
		return null == e ? null : e.text();
	}
	
	public XmlElement requiredChildElement(String name) {
		XmlElement e = childElement(name);
		
		if(null == e){
			throw new XmlValidationException("child element '{0}' of parent '{1}' is required in xml : {2}",name, name(),documentUrl());
		}
		
		return e;
	}
	
	public String requiredText() throws XmlValidationException {
		String string = Strings.trim(text());
		
		if(Strings.isEmpty(string)){
			throw new XmlValidationException("text of element '{0}' is required in xml : {1}",name(),documentUrl());
		}
		
		return string;
	}
	
	public XmlAttribute requiredAttribute(String name) throws XmlValidationException {
		XmlAttribute attr = attributes().firstOrNull(Predicates.<XmlAttribute> xnameEquals(name));
		
		if(null == attr){
			throw new XmlValidationException("attribute '{0}' of element '{1}' is required in xml : {2}",name,name(),documentUrl());
		}
		
		return attr.required();
	}
	
	public String requiredAttributeValue(String name) throws XmlValidationException {
		XmlAttribute attr = attribute(name);
		
		String value = Strings.trim(attr.value());
		
		if(Strings.isEmpty(value)){
			throw new XmlValidationException("attribute '{0}' value of element '{1}' is required in xml : {2}",name,name(),documentUrl());
		}
		
		return value;
	}
	
	@Override
	public void add(Object content) {
		if (content instanceof XmlAttribute) {
			attributes.add((XmlAttribute) content);
		} else {
			super.add(content);
		}
	}

	@Override
	public XmlNodeType nodeType() {
		return XmlNodeType.ELEMENT;
	}
	
	@Override
	protected XmlElement element() {
		return this;
	}

	@Override
	public String toString() {
		return toXml(XmlFormat.NOT_INDENTED);
	}

	@Override
	public String toXml(XmlFormat format) {
		boolean enableIndent = format.isIndentEnabled();
		String indent = enableIndent ? getIndent(format) : "";
		String newline = enableIndent ? "\n" : "";

		String tagName = name();

		StringBuilder sb = new StringBuilder();
		sb.append(indent);
		sb.append('<');
		sb.append(tagName);

		for (XmlAttribute att : attributes) {
			sb.append(' ');
			sb.append(att.name());
			sb.append("=\"");
			sb.append(XmlUtils.escapeAttributeValue(att.value()));
			sb.append('"');
		}

		List<XmlNode> nodes = childNodes().toList();
		if (nodes.size() == 0) {
			sb.append(" />");
		} else {
			sb.append('>');
			boolean onlyText = true;
			for (XmlNode node : childNodes()) {
				if (node.nodeType() == XmlNodeType.TEXT) {
					sb.append(node.toXml(format));
				} else {
					onlyText = false;
					sb.append(newline);
					sb.append(node.toXml(format.incrementLevel()));
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
}
