package bingo.lang.xml;

public class XmlDocumentType extends XmlNode {

	private final String name;
	private final String internalSubset;

	public XmlDocumentType(String name, String internalSubset) {
		this.name = name;
		this.internalSubset = internalSubset;
	}

	@Override
	public XmlNodeType nodeType() {
		return XmlNodeType.DOCUMENT_TYPE;
	}

	@Override
	public String toXml(XmlFormat format) {
		String indent = getIndent(format);
		return indent + "<!DOCTYPE " + name + " [\n" + internalSubset + "]>\n";
	}
}
