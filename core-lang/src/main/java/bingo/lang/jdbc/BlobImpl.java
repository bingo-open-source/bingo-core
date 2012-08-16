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
package bingo.lang.jdbc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class BlobImpl implements Blob {

	private InputStream stream;
	private long 	    length;
	private boolean     needsReset = false;

	public BlobImpl(byte[] bytes) {
		this.stream = new ByteArrayInputStream(bytes);
		this.length = bytes.length;
	}

	public BlobImpl(InputStream stream, long length) {
		this.stream = stream;
		this.length = length;
	}
	
	public long length() throws SQLException {
	    return length;
    }

	public byte[] getBytes(long pos, int length) throws SQLException {
		unsupported();
		return null;
    }

	public InputStream getBinaryStream() throws SQLException {
		try {
			if (needsReset) {
				stream.reset();
			}
		}
		catch ( IOException ioe) {
			throw new SQLException("could not reset reader");
		}
		needsReset = true;
		return stream;
    }

	public long position(byte[] pattern, long start) throws SQLException {
		unsupported();
	    return 0;
    }

	public long position(Blob pattern, long start) throws SQLException {
		unsupported();
	    return 0;
    }

	public int setBytes(long pos, byte[] bytes) throws SQLException {
		unsupported();
	    return 0;
    }

	public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
		unsupported();
	    return 0;
    }

	public OutputStream setBinaryStream(long pos) throws SQLException {
		unsupported();
	    return null;
    }

	public void truncate(long len) throws SQLException {
		unsupported();
    }
	
	private static void unsupported(){
		throw new UnsupportedOperationException("operation not supported in this blob");
	}	
}
