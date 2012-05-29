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
import bingo.lang.Exceptions;
import bingo.lang.exceptions.ParseException;
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
	
	public static XmlDocument parse(Reader reader) throws ParseException{
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
			
			return new XmlDocument(document);
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
			
			return doc;
		}catch(IOException e){
			throw new UncheckedIOException(e.getMessage(),e);
		}finally{
			IO.close(inputStream);
		}
	}

	private Resource   resource;
	private Document   domDocument;
	private XmlElement documentElement;
	
	protected XmlDocument(Document document) {
		for (Node childNode : domNodes(document.getChildNodes())) {
			XmlNode xchild = parseNode(this,childNode);
			if (xchild instanceof XmlElement) {
				this.documentElement = (XmlElement) xchild;
			}
			add(xchild);
		}
		this.domDocument = document;;
	}

	protected XmlDocument(XmlElement documentElement) {
		this.documentElement = documentElement;
		this.documentElement.setDocument(this);
		this.add(documentElement);
	}
	
	public XmlElement root() {
		return documentElement;
	}
	
	@Override
	public XmlNodeType nodeType() {
		return XmlNodeType.DOCUMENT;
	}
	
	public String url() {
		try {
	        return null == resource ? "unkow source" : resource.getURL().toString();
        } catch (IOException e) {
	        throw Exceptions.uncheck(e);
        }
	}
	
	@Override
    public XmlDocument document() {
	    return this;
    }
	
	@Override
    public String documentUrl() {
		return this.url();
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