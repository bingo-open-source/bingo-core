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
package bingo.lang.convert;

import java.lang.reflect.Type;
import java.sql.Blob;

import bingo.lang.Charsets;
import bingo.lang.Converts;
import bingo.lang.Out;
import bingo.lang.io.IO;
import bingo.lang.jdbc.BlobImpl;

public class BlobConverter extends AbstractConverter<Blob> {

	@Override
    public boolean convertFrom(Object value, Class<?> targetType, Type genericType, Out<Object> out) throws Throwable {
		if(value instanceof byte[]){
			return out.returns(new BlobImpl((byte[])value));
		}
		return false;
    }

	@Override
    public boolean convertTo(Blob value, Class<?> targetType, Type genericType, Out<Object> out) throws Throwable {
		//TODO : close inputstream ?
	    byte[] data = IO.toByteArray(value.getBinaryStream());
	    
	    return out.returns(Converts.convert(data, targetType, genericType));
    }

	@Override
    public String convertToString(Blob value) throws Throwable {
	    return IO.toString(value.getBinaryStream(), Charsets.UTF_8);
    }
}
