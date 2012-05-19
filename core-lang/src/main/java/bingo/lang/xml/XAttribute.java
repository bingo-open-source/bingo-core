package bingo.lang.xml;

public class XAttribute extends XObject implements XNameable {

	private XName  name;
	private String value;

	public XAttribute(String name, Object value) {
		this.name = new XName(null, name);
		this.value = value.toString();
	}

	public String getValue() {
		return value;
	}

	public XName getName() {
		return name;
	}

	public String getLocalName() {
		return null != name ? name.getLocalName() : null;
	}

	@Override
	public XNodeType nodeType() {
		return XNodeType.ATTRIBUTE;
	}

	public void setValue(String value) {
		this.value = value;
	}
}