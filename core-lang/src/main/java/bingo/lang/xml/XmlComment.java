package bingo.lang.xml;

public class XmlComment extends XmlNode {

	private String value;

	public XmlComment(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	@Override
	public XmlNodeType nodeType() {
		return XmlNodeType.COMMENT;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public String toXml(XmlFormat format) {
		String indent = getIndent(format);
		return indent + "<!--" + value + "-->";
	}
}
