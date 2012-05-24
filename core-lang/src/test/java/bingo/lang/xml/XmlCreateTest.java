package bingo.lang.xml;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlCreateTest {

	@Test
	public void testSimpleCreateDocument(){
		XmlDocument doc = new XmlDocument(new XmlElement("foo", new XmlAttribute("a", "1"), new XmlElement("bar")));
		
		assertNotNull(doc);
		
		assertEquals("foo", doc.root().name()) ;
	}
    
}
