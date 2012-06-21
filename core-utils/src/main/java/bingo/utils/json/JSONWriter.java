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
package bingo.utils.json;

import java.util.Date;

public interface JSONWriter {
	
	JSONWriter nullValue();
	
	JSONWriter key(String key);
	
	JSONWriter value(boolean bool);

	JSONWriter value(byte b);

	JSONWriter value(byte[] bytes);

	JSONWriter value(char c);
	
	JSONWriter value(short s);
	
	JSONWriter value(int i);
	
	JSONWriter value(long l);
	
	JSONWriter value(float f);
	
	JSONWriter value(double d);

	JSONWriter value(Number number);

	JSONWriter value(Date date);

	JSONWriter value(String string);
	
	JSONWriter seperator();
	
	JSONWriter raw(String string);

	JSONWriter startObject();

	JSONWriter property(String key, String stringValue);
	
	JSONWriter propertyOptional(String key, String stringValue);

	JSONWriter property(String key, boolean boolValue);

	JSONWriter property(String key, byte byteValue);

	JSONWriter property(String key, short shortValue);

	JSONWriter property(String key, int intValue);

	JSONWriter property(String key, long longValue);

	JSONWriter property(String key, float floatValue);

	JSONWriter property(String key, double doubleValue);
	
	JSONWriter property(String key, Number numberValue);

	JSONWriter property(String key, Date dateValue);

	JSONWriter endObject();
	
	JSONWriter array(String... stringArray);
	
	JSONWriter arrayIgnoreEmpty(String... stringArray);
	
	JSONWriter array(short... array);
	
	JSONWriter array(int... array);
	
	JSONWriter array(long... array);
	
	JSONWriter array(float... array);
	
	JSONWriter array(double... array);
	
	JSONWriter array(Number... array);
	
	JSONWriter array(Date... array);
	
	JSONWriter startArray();

	JSONWriter endArray();
}