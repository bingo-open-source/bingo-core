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
package bingo.lang.http;


public class HttpContentTypes {
	
	public static final String  APPLICATION_SOAP_XML		= "application/soap+xml";

	public static final String	APPLICATION_ATOM_XML	    = "application/atom+xml";

	public static final String	APPLICATION_ATOM_XML_UTF8	= APPLICATION_ATOM_XML + ";charset=utf-8";

	public static final String	APPLICATION_FORM_URLENCODED	= "application/x-www-form-urlencoded";

	public static final String	APPLICATION_JSON	        = "application/json";

	public static final String	APPLICATION_JSON_UTF8	    = APPLICATION_JSON + ";charset=utf-8";

	public static final String	APPLICATION_OCTET_STREAM	= "application/octet-stream";

	public static final String	APPLICATION_SVG_XML	        = "application/svg+xml";

	public static final String	APPLICATION_XHTML_XML	    = "application/xhtml+xml";

	public static final String	APPLICATION_XML	            = "application/xml";

	public static final String	APPLICATION_XML_UTF8	    = APPLICATION_XML + ";charset=utf-8";

	public static final String	MULTIPART_FORM_DATA	        = "multipart/form-data";

	public static final String	TEXT_HTML	                = "text/html";

	public static final String	TEXT_PLAIN	                = "text/plain";

	public static final String	TEXT_XML	                = "text/xml";

	public static final String	WILDCARD	                = "*/*";

	protected HttpContentTypes() {

	}
}