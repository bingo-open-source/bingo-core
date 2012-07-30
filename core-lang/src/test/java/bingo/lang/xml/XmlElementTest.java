/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bingo.lang.xml;

import junit.framework.TestCase;

public class XmlElementTest extends TestCase {
	
	public void testElementText() {
		XmlDocument doc = XmlDocument.parse("<root><e1>text1</e1><e2> text2 </e2><e3>xxx<![CDATA[\n\t\ttext3\n\t\t]]></e3></root>");
		XmlElement root = doc.rootElement();

		assertEquals("text1", root.childElementText("e1"));
		assertEquals(" text2 ", root.childElementText("e2"));
		assertEquals("xxx\n\t\ttext3\n\t\t", root.childElementText("e3"));
		
		assertEquals("text1", root.childElement("e1").text());
		assertEquals("text2", root.childElement("e2").textTrimmed());
		assertEquals("xxx\n\t\ttext3", root.childElement("e3").textTrimmed());
	}
	
}
