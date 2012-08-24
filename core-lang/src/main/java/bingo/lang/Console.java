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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {

	protected Console(){
		
	}
	
	public static String readLine() {
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(System.in));
			return reader.readLine();
		}catch(IOException e){
			throw Exceptions.uncheck(e);
		}
	}
	
	public static String readLine(String promote){
		write(promote);
		return readLine();
	}
	
	public static String readLine(String promote,boolean retryWhenEmpty){
		if(!retryWhenEmpty){
			return readLine(promote);
		}
		
		write(promote);
		String s = null;
		
		while(Strings.isEmpty(s = readLine())){
			;
		}
		
		return s;
	}
	
	public static String readLine(String promote,String defaultValue){
		String value = readLine(promote);
		
		return Strings.isEmpty(value) ? defaultValue : value;
	}
	
	
	public static void write(String string){
		System.out.print(string);
	}
	
	public static void writeLine(){
		System.out.println("");
	}
	
	public static void writeLine(String string){
		System.out.println(string);
	}
}
