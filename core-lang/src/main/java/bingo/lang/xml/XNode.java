package bingo.lang.xml;

public abstract class XNode extends XObject {

	public abstract String toString(XFormat format);

	protected String getIndent(XFormat format) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < format.getCurrentIndent(); i++) {
			sb.append(format.getIndentString());
		}
		return sb.toString();
	}

	public void remove() {
		if (parent == null) {
			return;
		}
		parent.removeNode(this);
		this.parent = null;
	}

	private XElement parent;

	void setParent(XElement parent) {
		this.parent = parent;
	}
}
