package bingo.lang.xml;

public class XmlText extends XmlNode {

	private String value;

	public XmlText(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	@Override
	public XmlNodeType nodeType() {
		return XmlNodeType.TEXT;
	}

	@Override
	public String toString() {
		return toXml(XmlFormat.NOT_INDENTED);
	}

	@Override
	public String toXml(XmlFormat format) {
		String escaped = XmlUtils.escapeElementValue(value);
		return (format.isIndentEnabled() ? escaped.trim() : escaped);
	}
}
