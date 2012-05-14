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
package bingo.lang;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import bingo.lang.exceptions.UnsupportedException;

public class Encoding {
	
	public static final Encoding ISO_8859_1 = create(Charsets.ISO_8859_1);

	public static final Encoding US_ASCII = create(Charsets.US_ASCII);

	public static final Encoding UTF_8 = create(Charsets.UTF_8);

	public static final Encoding UTF_16 = create(Charsets.UTF_16);
    
	public static Encoding create(String name) {
		try {
	        return new Encoding(name);
        } catch (Exception e) {
        	System.err.println(Strings.format("error create encoding '{0}', unsupported charset error",name));
        	e.printStackTrace();
        	return new UnsupportedEncoding(name);
        }
	}  
	
	public static Encoding create(Charset charset) {
        return new Encoding(charset);
	}
	
	protected Charset charset;
	
	protected Encoding(){
		
	}
	
	protected Encoding(Charset charset){
		this.charset = charset;
	}

	protected Encoding(String name) {
		this.charset = Charset.forName(name);
	}
	
	public byte[] getBytes(String string) {
		try {
	        return null == string ? Arrays.EMPTY_BYTE_ARRAY : string.getBytes(charset.name());
        } catch (UnsupportedEncodingException e) {
        	throw new UnsupportedException(e.getMessage(),e);
        }
	}
	
	public String getString(byte[] bytes){
		try {
			return null == bytes || bytes.length == 0 ? Strings.EMPTY : new String(bytes,charset.name());
        } catch (UnsupportedEncodingException e) {
        	throw new UnsupportedException(e.getMessage(),e);
        }
	}
	
	public String name(){
		return charset.name();
	}
	
	public Charset charset(){
		return charset;
	}
	
	private static final class UnsupportedEncoding extends Encoding {
		
		private static final String message = "unsupported charset '{0}'";
		
		private String name;
		
		public UnsupportedEncoding(String name) {
	        this.name = name;
        }

		@Override
        public byte[] getBytes(String string) {
			throw new UnsupportedException(message,name);
        }
		
		@Override
        public String getString(byte[] bytes) {
			throw new UnsupportedException(message,name);
        }
		
		@Override
        public Charset charset() {
	        throw new UnsupportedException(message,name);
        }
	}
}