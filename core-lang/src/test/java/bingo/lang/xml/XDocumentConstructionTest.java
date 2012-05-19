package bingo.lang.xml;

import org.junit.Test;

import static org.junit.Assert.*;

public class XDocumentConstructionTest {

	@Test
	public void testSimpleCreateDocument(){
		XDocument doc = new XDocument(new XElement("foo", new XAttribute("a", "1"), new XElement("bar")));
		
		assertNotNull(doc);
		
		assertEquals("foo", doc.root().getName().getLocalName()) ;
	}
    
}
