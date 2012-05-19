package bingo.lang.xml;

public class XProcessingInstruction extends XNode {

	private final String target;
	private final String data;

	public XProcessingInstruction(String target, String data) {
		this.target = target;
		this.data = data;
	}

	public String getTarget() {
		return target;
	}

	public String getData() {
		return data;
	}

	@Override
	public XNodeType nodeType() {
		return XNodeType.PROCESSING_INSTRUCTION;
	}

	@Override
	public String toString(XFormat format) {
		String indent = getIndent(format);
		return indent + "<?" + target + " " + data + ">";
	}
}
