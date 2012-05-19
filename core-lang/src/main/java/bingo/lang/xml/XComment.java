package bingo.lang.xml;

public class XComment extends XNode {

	private String value;

	public XComment(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public XNodeType nodeType() {
		return XNodeType.COMMENT;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public String toString(XFormat format) {
		String indent = getIndent(format);
		return indent + "<!--" + value + "-->";
	}
}
