package bingo.lang.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.UnsupportedCharsetException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import bingo.lang.Charsets;
import bingo.lang.exceptions.UncheckedIOException;
import bingo.lang.io.IO;
import bingo.lang.resource.Resource;
import bingo.lang.resource.Resources;

public class XmlDocument extends XmlContainer {
	
	public static XmlDocument parse(String text) throws UncheckedIOException {
		return parse(new StringReader(text));
	}
	
	public static XmlDocument parse(InputStream inputStream) throws UncheckedIOException {
		return parse(new InputStreamReader(inputStream,Charsets.UTF_8));
	}	
	
	public static XmlDocument parse(InputStream inputStream,String encoding) throws UncheckedIOException,UnsupportedCharsetException {
		return parse(new InputStreamReader(inputStream,Charsets.forName(encoding)));
	}
	
	public static XmlDocument parse(InputStream inputStream,String encoding,String location) throws UncheckedIOException,UnsupportedCharsetException {
		return parse(new InputStreamReader(inputStream,Charsets.forName(encoding)),location);
	}
	
	public static XmlDocument parse(Reader reader) throws UncheckedIOException {
		return parse(reader,null);
	}
	
	public static XmlDocument parse(Reader reader,String location) throws UncheckedIOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			
            builder.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
					return new InputSource(new StringReader(""));
				}
			});
			
			Document document = builder.parse(new InputSource(reader));
			
			return new XmlDocument(document,location);
		} catch (Exception e) {
			throw new UncheckedIOException(e.getMessage(),e);
		}
	}
	
	public static XmlDocument load(String resourceFile) throws UncheckedIOException {
		return load(Resources.getResource(resourceFile));
	}

	public static XmlDocument load(Resource resource) throws UncheckedIOException {
		InputStream inputStream = null;
		try{
			inputStream = resource.getInputStream();
			
			XmlDocument doc = parse(inputStream);
			
			doc.resource = resource;
			doc.location = null == resource.getURL() ? null : resource.getURL().toString();
			return doc;
		}catch(IOException e){
			throw new UncheckedIOException(e.getMessage(),e);
		}finally{
			IO.close(inputStream);
		}
	}

	private String     location;
	private Resource   resource;
	private Document   domDocument;
	private XmlElement documentElement;
	
	protected XmlDocument(Document document) {
		this(document,null);
	}
	
	protected XmlDocument(Document document,String location) {
		for (Node childNode : domNodes(document.getChildNodes())) {
			XmlNode xchild = parseNode(this,childNode);
			if (xchild instanceof XmlElement) {
				this.documentElement = (XmlElement) xchild;
			}
			add(xchild);
		}
		this.domDocument = document;
		this.location    = location;
	}

	protected XmlDocument(XmlElement documentElement) {
		this(documentElement,null);
	}
	
	protected XmlDocument(XmlElement documentElement,String location) {
		this.documentElement = documentElement;
		this.documentElement.setDocument(this);
		this.add(documentElement);
		this.location = location;
	}
	
	public XmlElement rootElement() {
		return documentElement;
	}
	
	@Override
	public XmlNodeType nodeType() {
		return XmlNodeType.DOCUMENT;
	}
	
	public String location() {
		return location;
	}
	
	public String locationOrUnknow(){
		return null == location ? "unknow location" : location;
	}
	
	@Override
    public XmlDocument document() {
	    return this;
    }

	protected Resource resource(){
		return resource;
	}
	
	protected Document domDocument(){
		return domDocument;
	}
	
	@Override
	protected XmlElement element() {
		return documentElement;
	}	
	
	@Override
	public String toString() {
		return toXml(XmlFormat.NOT_INDENTED);
	}

	@Override
	public String toXml(XmlFormat format) {
		StringBuilder sb = new StringBuilder();
		for (XmlNode node : childNodes()) {
			if (node instanceof XmlProcessingInstruction || node instanceof XmlElement || node instanceof XmlComment || node instanceof XmlDocumentType) {
				sb.append(node.toXml(format));
			} else {
				throw new UnsupportedOperationException("implement " + node);
			}
		}
		return sb.toString();
	}
}