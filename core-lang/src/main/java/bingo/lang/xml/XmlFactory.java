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

import java.io.Reader;
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
	private static boolean FOUND_STAX_READER;
	private static boolean FOUND_PULL_PARSER;
	
	static {
		tryToFoundValidStaxWriter();
		tryToFoundValidStaxReader();
		tryToFoundValidPullParser();
	}
	
	protected XmlFactory(){
		
	}
	
	public static XmlWriter createWriter(Writer out) {
		return FOUND_STAX_WRITER ? createStaxWriter(out) : createBaseWriter(out);
	}
	
	public static XmlReader createReader(Reader in) {
		return FOUND_PULL_PARSER ? createPullReader(in) : ( FOUND_STAX_READER ? createStaxReader(in) : createDomReader(in));
	}
	
	static XmlWriter createBaseWriter(Writer out){
		return new XmlWriterBaseImpl(out);
	}
	
	static XmlWriter createStaxWriter(Writer out) throws XmlException {
		if(!FOUND_STAX_WRITER){
			throw new XmlException("cannot create stax writer : 'javax.xml.stream.XMLOutputFactory' is invalid");
		}
		return new XmlWriterStaxImpl(out);
	}
	
	static XmlReader createPullReader(Reader in){
		return new XmlReaderPullImpl(in);
	}
	
	static XmlReader createStaxReader(Reader in){
		return new XmlReaderStaxImpl(in);
	}	
	
	static XmlReader createDomReader(Reader in){
		return new XmlReaderDomImpl(in);
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
				log.debug("Cannot new instance of 'javax.xml.stream.XMLOutputFactory',Use default xml writer.");
				log.debug("Message : {}",e.getMessage());
			}
		}
	}
	
	private static void tryToFoundValidStaxReader(){
		Class<?> xmlInputFactoryClass = Classes.forNameOrNull("javax.xml.stream.XMLInputFactory");

		if(null != xmlInputFactoryClass){
			try{
				Method m = Reflects.findMethod(xmlInputFactoryClass, "newInstance");
				
				m.invoke(null,(Object[])null);
				
				FOUND_STAX_READER = true;
			}catch(Throwable e){
				if(e instanceof InvocationTargetException){
					e = ((InvocationTargetException)e).getTargetException();
				}
				log.debug("Cannot new instance of 'javax.xml.stream.XMLInputFactory',Use other xml reader.");
				log.debug("Message : {}",e.getMessage());
			}
		}
	}
	
	private static void tryToFoundValidPullParser(){
		Class<?> xmlPullParserFactoryClass = Classes.forNameOrNull("org.xmlpull.v1.XmlPullParserFactory");

		if(null != xmlPullParserFactoryClass){
			try{
				Method m = Reflects.findMethod(xmlPullParserFactoryClass, "newInstance");
				
				m.invoke(null,(Object[])null);
				
				FOUND_PULL_PARSER = true;
			}catch(Throwable e){
				if(e instanceof InvocationTargetException){
					e = ((InvocationTargetException)e).getTargetException();
				}
				log.debug("Cannot new instance of 'org.xmlpull.v1.XmlPullParserFactory',Use other xml reader.");
				log.debug("Message : {}",e.getMessage());
			}
		}
	}
}