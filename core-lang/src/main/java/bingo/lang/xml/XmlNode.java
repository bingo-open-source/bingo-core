package bingo.lang.xml;

public abstract class XmlNode extends XmlObject {
	
	private XmlElement  parent;
	private XmlDocument document;
	
	public XmlElement parent(){
		return parent;
	}
	
	public XmlDocument document(){
		return document;
	}
	
	public String documentUrl(){
		return null == document ? "unknow source" : document.url();
	}
	
	public void remove() {
		if (parent == null) {
			return;
		}
		parent.remove(this);
		this.parent = null;
	}
	
	public String toXml(){
		return toXml(XmlFormat.NOT_INDENTED);
	}

	public abstract String toXml(XmlFormat format);
	
	protected String getIndent(XmlFormat format) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < format.getCurrentIndent(); i++) {
			sb.append(format.getIndentString());
		}
		return sb.toString();
	}	

	protected void setParent(XmlElement parent) {
		this.parent = parent;
	}
	
	protected void setDocument(XmlDocument document) {
		this.document = document;
	}
}