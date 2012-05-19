package bingo.lang.xml;

public class XText extends XNode {

	private String value;

	public XText(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public XNodeType nodeType() {
		return XNodeType.TEXT;
	}

	@Override
	public String toString() {
		return toString(XFormat.NOT_INDENTED);
	}

	@Override
	public String toString(XFormat format) {
		String escaped = XUtils.escapeElementValue(value);
		return (format.isIndentEnabled() ? escaped.trim() : escaped);
	}
}
