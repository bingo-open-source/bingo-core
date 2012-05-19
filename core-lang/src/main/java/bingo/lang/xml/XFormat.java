package bingo.lang.xml;

public class XFormat {

	public static final XFormat NOT_INDENTED = new XFormat(false, " ", 0);
	public static final XFormat INDENTED = new XFormat(true, "  ", 0);
	private final boolean indentEnabled;
	private final String indentString;
	private final int currentIndent;

	private XFormat(boolean indentEnabled, String indentString, int currentIndent) {
		this.indentEnabled = indentEnabled;
		this.indentString = indentString;
		this.currentIndent = currentIndent;
	}

	public int getCurrentIndent() {
		return currentIndent;
	}

	public String getIndentString() {
		return indentString;
	}

	public boolean isIndentEnabled() {
		return indentEnabled;
	}

	public XFormat incrementLevel() {
		return new XFormat(indentEnabled, indentString, currentIndent + 1);
	}
}