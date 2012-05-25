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
package bingo.lang.io;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import bingo.lang.Charsets;
import bingo.lang.ThrowableAction1;
import bingo.lang.ThrowableFunc1;
import bingo.lang.exceptions.UncheckedIOException;

//from apache commons io

public class IO {

	private static final int EOF = -1;

	/**
	 * The default buffer size ({@value} ) to use for {@link #copy(InputStream, OutputStream)} and
	 * {@link #copy(Reader, Writer)}
	 */
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	protected IO() {

	}
	
	//Execute
	//-----------------------------------------------------------------------
	
	public static <T extends Closeable> void execute(T closable,ThrowableAction1<T> action) throws UncheckedIOException {
		try{
			action.execute(closable);
		} catch(Throwable e){
			throw new UncheckedIOException(e.getMessage(),e);
		} finally{
			close(closable);
		}
	}
	
	public static <T extends Closeable,R> R execute(T closable,ThrowableFunc1<T,R> func) throws UncheckedIOException {
		try{
			return func.apply(closable);
		} catch(Throwable e){
			throw new UncheckedIOException(e.getMessage(),e);
		} finally{
			close(closable);
		}
	}	

	// Close
	//-----------------------------------------------------------------------
	/**
	 * Unconditionally close an <code>Reader</code>.
	 * <p>
	 * Equivalent to {@link Reader#close()}, except any exceptions will be ignored. This is typically used in finally
	 * blocks.
	 * <p>
	 * Example code:
	 * 
	 * <pre>
	 * char[] data = new char[1024];
	 * Reader in = null;
	 * try {
	 * 	in = new FileReader(&quot;foo.txt&quot;);
	 * 	in.read(data);
	 * 	in.close(); //close errors are handled
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	IOUtils.closeQuietly(in);
	 * }
	 * </pre>
	 * 
	 * @param input the Reader to close, may be null or already closed
	 */
	public static void close(Reader input) {
		close((Closeable) input);
	}

	/**
	 * Unconditionally close a <code>Writer</code>.
	 * <p>
	 * Equivalent to {@link Writer#close()}, except any exceptions will be ignored. This is typically used in finally
	 * blocks.
	 * <p>
	 * Example code:
	 * 
	 * <pre>
	 * Writer out = null;
	 * try {
	 * 	out = new StringWriter();
	 * 	out.write(&quot;Hello World&quot;);
	 * 	out.close(); //close errors are handled
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	IOUtils.close(out);
	 * }
	 * </pre>
	 * 
	 * @param output the Writer to close, may be null or already closed
	 */
	public static void close(Writer output) {
		close((Closeable) output);
	}

	/**
	 * Unconditionally close an <code>InputStream</code>.
	 * <p>
	 * Equivalent to {@link InputStream#close()}, except any exceptions will be ignored. This is typically used in
	 * finally blocks.
	 * <p>
	 * Example code:
	 * 
	 * <pre>
	 * byte[] data = new byte[1024];
	 * InputStream in = null;
	 * try {
	 * 	in = new FileInputStream(&quot;foo.txt&quot;);
	 * 	in.read(data);
	 * 	in.close(); //close errors are handled
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	IOUtils.close(in);
	 * }
	 * </pre>
	 * 
	 * @param input the InputStream to close, may be null or already closed
	 */
	public static void close(InputStream input) {
		close((Closeable) input);
	}

	/**
	 * Unconditionally close an <code>OutputStream</code>.
	 * <p>
	 * Equivalent to {@link OutputStream#close()}, except any exceptions will be ignored. This is typically used in
	 * finally blocks.
	 * <p>
	 * Example code:
	 * 
	 * <pre>
	 * byte[] data = &quot;Hello, World&quot;.getBytes();
	 * 
	 * OutputStream out = null;
	 * try {
	 * 	out = new FileOutputStream(&quot;foo.txt&quot;);
	 * 	out.write(data);
	 * 	out.close(); //close errors are handled
	 * } catch (IOException e) {
	 * 	// error handling
	 * } finally {
	 * 	IOUtils.close(out);
	 * }
	 * </pre>
	 * 
	 * @param output the OutputStream to close, may be null or already closed
	 */
	public static void close(OutputStream output) {
		close((Closeable) output);
	}

	/**
	 * Unconditionally close a <code>Closeable</code>.
	 * <p>
	 * Equivalent to {@link Closeable#close()}, except any exceptions will be ignored. This is typically used in finally
	 * blocks.
	 * <p>
	 * Example code:
	 * 
	 * <pre>
	 * Closeable closeable = null;
	 * try {
	 * 	closeable = new FileReader(&quot;foo.txt&quot;);
	 * 	// process closeable
	 * 	closeable.close();
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	IOUtils.close(closeable);
	 * }
	 * </pre>
	 * 
	 * @param closeable the object to close, may be null or already closed
	 */
	public static void close(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	// read
	//-----------------------------------------------------------------------
	/**
	 * Read characters from an input character stream. This implementation guarantees that it will read as many
	 * characters as possible before giving up; this may not always be the case for subclasses of {@link Reader}.
	 * 
	 * @param input where to read input from
	 * @param buffer destination
	 * @param offset inital offset into buffer
	 * @param length length to read, must be >= 0
	 * @return actual length read; may be less than requested if EOF was reached
	 * @throws IOException if a read error occurs
	 */
	public static int read(Reader input, char[] buffer, int offset, int length) throws IOException {
		if (length < 0) {
			throw new IllegalArgumentException("Length must not be negative: " + length);
		}
		int remaining = length;
		while (remaining > 0) {
			int location = length - remaining;
			int count = input.read(buffer, offset + location, remaining);
			if (EOF == count) { // EOF
				break;
			}
			remaining -= count;
		}
		return length - remaining;
	}

	/**
	 * Read characters from an input character stream. This implementation guarantees that it will read as many
	 * characters as possible before giving up; this may not always be the case for subclasses of {@link Reader}.
	 * 
	 * @param input where to read input from
	 * @param buffer destination
	 * @return actual length read; may be less than requested if EOF was reached
	 * @throws IOException if a read error occurs
	 */
	public static int read(Reader input, char[] buffer) throws IOException {
		return read(input, buffer, 0, buffer.length);
	}

	/**
	 * Read bytes from an input stream. This implementation guarantees that it will read as many bytes as possible
	 * before giving up; this may not always be the case for subclasses of {@link InputStream}.
	 * 
	 * @param input where to read input from
	 * @param buffer destination
	 * @param offset inital offset into buffer
	 * @param length length to read, must be >= 0
	 * @return actual length read; may be less than requested if EOF was reached
	 * @throws IOException if a read error occurs
	 */
	public static int read(InputStream input, byte[] buffer, int offset, int length) throws IOException {
		if (length < 0) {
			throw new IllegalArgumentException("Length must not be negative: " + length);
		}
		int remaining = length;
		while (remaining > 0) {
			int location = length - remaining;
			int count = input.read(buffer, offset + location, remaining);
			if (EOF == count) { // EOF
				break;
			}
			remaining -= count;
		}
		return length - remaining;
	}

	/**
	 * Read bytes from an input stream. This implementation guarantees that it will read as many bytes as possible
	 * before giving up; this may not always be the case for subclasses of {@link InputStream}.
	 * 
	 * @param input where to read input from
	 * @param buffer destination
	 * @return actual length read; may be less than requested if EOF was reached
	 * @throws IOException if a read error occurs
	 */
	public static int read(InputStream input, byte[] buffer) throws IOException {
		return read(input, buffer, 0, buffer.length);
	}

	// read toByteArray
	//-----------------------------------------------------------------------
	/**
	 * Get the contents of an <code>InputStream</code> as a <code>byte[]</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @return the requested byte array
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	/**
	 * Get contents of an <code>InputStream</code> as a <code>byte[]</code>. Use this method instead of
	 * <code>toByteArray(InputStream)</code> when <code>InputStream</code> size is known. <b>NOTE:</b> the method checks
	 * that the length can safely be cast to an int without truncation before using
	 * {@link IO#toByteArray(java.io.InputStream, int)} to read into the byte array. (Arrays can have no more than
	 * Integer.MAX_VALUE entries anyway)
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param size the size of <code>InputStream</code>
	 * @return the requested byte array
	 * @throws IOException if an I/O error occurs or <code>InputStream</code> size differ from parameter size
	 * @throws IllegalArgumentException if size is less than zero or size is greater than Integer.MAX_VALUE
	 * @see IO#toByteArray(java.io.InputStream, int)
	 * @since 2.1
	 */
	public static byte[] toByteArray(InputStream input, long size) throws IOException {

		if (size > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
		}

		return toByteArray(input, (int) size);
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a <code>byte[]</code>. Use this method instead of
	 * <code>toByteArray(InputStream)</code> when <code>InputStream</code> size is known
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param size the size of <code>InputStream</code>
	 * @return the requested byte array
	 * @throws IOException if an I/O error occurs or <code>InputStream</code> size differ from parameter size
	 * @throws IllegalArgumentException if size is less than zero
	 */
	public static byte[] toByteArray(InputStream input, int size) throws IOException {

		if (size < 0) {
			throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
		}

		if (size == 0) {
			return new byte[0];
		}

		byte[] data = new byte[size];
		int offset = 0;
		int readed;

		while (offset < size && (readed = input.read(data, offset, size - offset)) != EOF) {
			offset += readed;
		}

		if (offset != size) {
			throw new IOException("Unexpected readed size. current: " + offset + ", excepted: " + size);
		}

		return data;
	}

	// read toString
	//-----------------------------------------------------------------------
	/**
	 * Get the contents of an <code>InputStream</code> as a String using the specified character encoding.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
	 * </p>
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param encoding the encoding to use, null means platform default
	 * @return the requested String
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static String toString(InputStream input, Charset encoding) throws IOException {
		StringWriter sw = new StringWriter();
		copy(input, sw, encoding);
		return sw.toString();
	}

	/**
	 * Get the contents of a <code>Reader</code> as a String.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a <code>BufferedReader</code>.
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @return the requested String
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static String toString(Reader input) throws IOException {
		StringWriter sw = new StringWriter();
		copy(input, sw);
		return sw.toString();
	}

	// copy from InputStream
	//-----------------------------------------------------------------------
	/**
	 * Copy bytes from an <code>InputStream</code> to an <code>OutputStream</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
	 * <p>
	 * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static long copy(InputStream input, OutputStream output) throws IOException {
		return copy(input, output, new byte[DEFAULT_BUFFER_SIZE]);
	}

	/**
	 * Copy bytes from an <code>InputStream</code> to an <code>OutputStream</code>.
	 * <p>
	 * This method uses the provided buffer, so there is no need to use a <code>BufferedInputStream</code>.
	 * <p>
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @param buffer the buffer to use for the copy
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 * @since 2.2
	 */
	public static long copy(InputStream input, OutputStream output, byte[] buffer) throws IOException {
		long count = 0;
		int n = 0;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * Copy bytes from an <code>InputStream</code> to chars on a <code>Writer</code> using the default character
	 * encoding of the platform.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
	 * <p>
	 * This method uses {@link InputStreamReader}.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>Writer</code> to write to
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 * @since 1.1
	 */
	public static void copy(InputStream input, Writer output) throws IOException {
		copy(input, output, Charset.defaultCharset());
	}

	/**
	 * Copy bytes from an <code>InputStream</code> to chars on a <code>Writer</code> using the specified character
	 * encoding.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
	 * <p>
	 * This method uses {@link InputStreamReader}.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>Writer</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 * @since 2.3
	 */
	public static void copy(InputStream input, Writer output, Charset encoding) throws IOException {
		InputStreamReader in = new InputStreamReader(input, Charsets.getOrDefault(encoding));
		copy(in, output);
	}

	/**
	 * Copy bytes from an <code>InputStream</code> to chars on a <code>Writer</code> using the specified character
	 * encoding.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a <code>BufferedInputStream</code>.
	 * <p>
	 * Character encoding names can be found at <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method uses {@link InputStreamReader}.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>Writer</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 * @throws UnsupportedCharsetException thrown instead of {@link UnsupportedEncodingException} in version 2.2 if the
	 *             encoding is not supported.
	 * @since 1.1
	 */
	public static void copy(InputStream input, Writer output, String encoding) throws IOException {
		copy(input, output, Charsets.getOrDefault(encoding));
	}

	// copy from Reader
	//-----------------------------------------------------------------------	
	/**
	 * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a <code>BufferedReader</code>.
	 * <p>
	 * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @param output the <code>Writer</code> to write to
	 * @return the number of characters copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static long copy(Reader input, Writer output) throws IOException {
		return copy(input, output, new char[DEFAULT_BUFFER_SIZE]);
	}

	/**
	 * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
	 * <p>
	 * This method uses the provided buffer, so there is no need to use a <code>BufferedReader</code>.
	 * <p>
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @param output the <code>Writer</code> to write to
	 * @param buffer the buffer to be used for the copy
	 * @return the number of characters copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static long copy(Reader input, Writer output, char[] buffer) throws IOException {
		long count = 0;
		int n = 0;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
}
