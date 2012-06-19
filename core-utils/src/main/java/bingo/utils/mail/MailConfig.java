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
package bingo.utils.mail;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bingo.lang.Charsets;
import bingo.lang.Strings;
import bingo.lang.exceptions.NotFoundException;
import bingo.lang.exceptions.UncheckedIOException;
import bingo.lang.xml.XmlException;

public class MailConfig {
	
	public static MailConfig loadFromXml(String xmlResourceLocation) throws NotFoundException,UncheckedIOException,XmlException {
		return Utils.loadFromXml(xmlResourceLocation);
	}
	
	private final String              host;
	private final int                 port;
	private final boolean			    auth;
	private final boolean            debug;
	private final String              username;
	private final String              password;
	private final Recipient      	   from;
	private final Charset			   charset;
	private final int                connectionTimeout;
	private final int                socketTimeout;
	private final Map<String, String> properties;
	
	public MailConfig(String host){
		this.host       = host;
		this.port       = 0;
		this.debug      = false;
		this.auth       = false;
		this.username   = null;
		this.password   = null;
		this.from       = null;
		this.charset    = Charsets.UTF_8; 
		
		this.connectionTimeout = 0;
		this.socketTimeout     = 0;
		this.properties = new HashMap<String, String>();
	}	
	
	public MailConfig(String host,int port){
		this.host       = host;
		this.port       = port;
		this.debug      = false;
		this.auth       = false;
		this.username   = null;
		this.password   = null;
		this.from       = null;
		this.charset    = Charsets.UTF_8;
		
		this.connectionTimeout = 0;
		this.socketTimeout     = 0;		
		this.properties = new HashMap<String, String>();
	}
	
	public MailConfig(String host,int port,String username,String password){
		this.host       = host;
		this.port       = port;
		this.debug      = false;
		this.auth       = !Strings.isEmpty(username) && !Strings.isEmpty(password);
		this.username   = username;
		this.password   = password;
		this.from       = null;
		this.charset    = Charsets.UTF_8;
		
		this.connectionTimeout = 0;
		this.socketTimeout     = 0;
		this.properties = new HashMap<String, String>();		
	}
	
	public MailConfig(String host,int port,boolean auth,boolean debug,String username,String password){
		this.host       = host;
		this.port       = port;
		this.debug      = debug;
		this.auth       = auth;
		this.username   = username;
		this.password   = password;
		this.from       = null;
		this.charset    = Charsets.UTF_8;
		
		this.connectionTimeout = 0;
		this.socketTimeout     = 0;
		this.properties = new HashMap<String, String>();		
	}
	
	public MailConfig(String host,int port,boolean auth,boolean debug,String username,String password,
					   Recipient from,
					   Charset charset,
					   int connectionTimeout,
					   int socketTimeout,
					   Map<String, String> properties){
		
		this.host       = host;
		this.port       = port;
		this.auth       = auth;
		this.debug      = debug;
		this.username   = username;
		this.password   = password;
		this.from       = from;
		this.charset    = charset;
		
		this.connectionTimeout = connectionTimeout;
		this.socketTimeout     = socketTimeout;
		this.properties = new HashMap<String, String>(properties);
	}

	public String getHost() {
		return host;
	}
	
	public int getPort(){
		return port;
	}
	
	public boolean isAuth() {
    	return auth;
    }

	public boolean isDebug() {
    	return debug;
    }

	public String getUsername() {
    	return username;
    }

	public String getPassword() {
    	return password;
    }

	public Recipient getFrom() {
    	return from;
    }
	
	public Charset getCharset() {
    	return charset;
    }
	
	public int getConnectionTimeout() {
    	return connectionTimeout;
    }

	public int getSocketTimeout() {
    	return socketTimeout;
    }

	public Map<String, String> getProperties() {
    	return Collections.unmodifiableMap(properties);
    }
}