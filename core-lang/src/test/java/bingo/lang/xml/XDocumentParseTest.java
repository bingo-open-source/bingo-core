package bingo.lang.xml;

import java.io.IOException;

import org.junit.Test;

import bingo.lang.resource.Resource;
import bingo.lang.resource.Resources;

import static org.junit.Assert.*;

public class XDocumentParseTest {

	@Test
	public void testSimpleParseFromString() {
		XDocument doc = XDocument.parse("<a foo='bar' foo2='bax'><b><c/></b><d><b>boom</b></d></a>");

		System.out.println(doc);

		System.out.println("\ndescendants()");
		for (XElement e : doc.descendants()) {
			System.out.println(e);
		}

		System.out.println("\ndescendants(b)");
		for (XElement e : doc.descendants("b")) {
			System.out.println(e);
		}

		System.out.println("\ndoc.element(a)");
		System.out.println(doc.element("a"));
	}

	@Test
	public void testSimpleLoadFromStream() {

		Resource[] resources = Resources.scan("classpath*:xml/**/*.xml");

		for (Resource resource : resources) {

			try {
				System.out.println("#load xml : " + resource.getURL().toString());

				XDocument doc = XDocument.loadUtf8(resource.getInputStream());
				
				assertNotNull(doc);
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
