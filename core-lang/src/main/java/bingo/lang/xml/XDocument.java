package bingo.lang.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import bingo.lang.Charsets;

public class XDocument extends XContainer {
	
	public static XDocument parse(String text) {
		return load(new StringReader(text));
	}
	
	public static XDocument load(InputStream inputStream) {
		try {
			return load(new InputStreamReader(inputStream,Charsets.UTF_8));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	
	
	public static XDocument load(InputStream inputStream,String encoding) {
		try {
			return load(new InputStreamReader(inputStream,Charsets.forName(encoding)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static XDocument loadUtf8(InputStream inputStream) {
		try {
			return load(new InputStreamReader(inputStream, "UTF8"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static XDocument load(Reader reader) {
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
			return new XDocument(document);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private XElement documentElement;

	public XDocument(XElement documentElement) {
		this.documentElement = documentElement;
		this.add(documentElement);
	}

	@Override
	public String toString() {
		return toString(XFormat.NOT_INDENTED);
	}

	@Override
	public String toString(XFormat format) {
		StringBuilder sb = new StringBuilder();
		for (XNode node : nodes()) {
			if (node instanceof XProcessingInstruction || node instanceof XElement || node instanceof XComment || node instanceof XDocumentType) {
				sb.append(node.toString(format));
			} else {
				throw new UnsupportedOperationException("implement " + node);
			}
		}
		return sb.toString();
	}

	public XElement root() {
		return documentElement;
	}
	
	@Override
	protected XElement element() {
		return documentElement;
	}

	@Override
	public XNodeType nodeType() {
		return XNodeType.DOCUMENT;
	}
	
	private XDocument(Document document) {
		for (Node childNode : domNodes(document.getChildNodes())) {
			XNode xchild = parseNode(childNode);
			if (xchild instanceof XElement) {
				this.documentElement = (XElement) xchild;
			}
			add(xchild);
		}
	}
}