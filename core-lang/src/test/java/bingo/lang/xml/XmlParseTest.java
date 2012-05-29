package bingo.lang.xml;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import bingo.lang.resource.Resource;
import bingo.lang.resource.Resources;
import bingo.lang.testing.junit.ConcurrentTestCase;

public class XmlParseTest extends ConcurrentTestCase {

	@Test
	public void testSimpleParseFromString() {
		XmlDocument doc = XmlDocument.parse("<a foo='bar' foo2='bax'><b><c/></b><d><b>boom</b></d></a>");

		System.out.println(doc);

		System.out.println("\ndescendants()");
		for (XmlElement e : doc.descendantElements()) {
			System.out.println(e);
		}

		System.out.println("\ndescendants(b)");
		for (XmlElement e : doc.descendantElements("b")) {
			System.out.println(e);
		}

		System.out.println("\ndoc.element(a)");
		System.out.println(doc.childElement("a"));
	}

	@Test
	public void testSimpleLoadFromStream() {

		Resource[] resources = Resources.scan("classpath*:xml/**/*.xml");

		for (Resource resource : resources) {

			try {
				System.out.println("#load xml : " + resource.getURL().toString());

				XmlDocument doc = XmlDocument.load(resource);
				
				assertNotNull(doc);
				
				String xml = doc.toXml();
				
				System.out.println(xml);
				
				assertNotNull(XmlDocument.parse(xml));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
