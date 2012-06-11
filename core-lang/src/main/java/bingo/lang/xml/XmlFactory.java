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

import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import bingo.lang.Classes;
import bingo.lang.Reflects;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;

public class XmlFactory {
	
	private static final Log log = LogFactory.get(XmlFactory.class);
	
	private static boolean FOUND_STAX_WRITER;
	
	static {
		tryToFoundValidStaxWriter();
	}
	
	protected XmlFactory(){
		
	}
	
	public static XmlWriter createWriter(Writer out) {
		return FOUND_STAX_WRITER ? createStaxXmlWriter(out) : createBaseXmlWriter(out);
	}
	
	private static XmlWriter createBaseXmlWriter(Writer writer){
		return new XmlWriterBaseImpl(writer);
	}
	
	private static XmlWriter createStaxXmlWriter(Writer writer){
		return new XmlWriterStaxImpl(writer);
	}
	
	private static void tryToFoundValidStaxWriter(){
		Class<?> xmlOutputFactoryClass = Classes.forNameOrNull("javax.xml.stream.XMLOutputFactory");

		if(null != xmlOutputFactoryClass){
			try{
				Method m = Reflects.findMethod(xmlOutputFactoryClass, "newInstance");
				
				m.invoke(null,(Object[])null);
				
				FOUND_STAX_WRITER = true;
			}catch(Throwable e){
				if(e instanceof InvocationTargetException){
					e = ((InvocationTargetException)e).getTargetException();
				}
				log.info("Cannot new instance of 'javax.xml.stream.XMLOutputFactory',Use default xml writer.");
				log.info("Message : {}",e.getMessage());
			}
		}
	}
}