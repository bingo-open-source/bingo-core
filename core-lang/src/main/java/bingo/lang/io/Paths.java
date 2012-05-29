/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import bingo.lang.Strings;

public class Paths {

    public static final char  EXTENSION_SEPARATOR 	 = '.';
    public static final String EXTENSION_SEPARATOR_STR = Character.toString(EXTENSION_SEPARATOR);
    
    public static final char   NORMALIZED_SEPERATOR 	  = '/';
    public static final String NORMALIZED_SEPERATOR_STR = "/";

    public static final char 	 UNIX_SEPARATOR 	= '/';
    public static final String UNIX_SEPARATOR_STR = Character.toString(UNIX_SEPARATOR);

    public static final char 	 WINDOWS_SEPARATOR 	   = '\\';
    public static final String WINDOWS_SEPARATOR_STR = Character.toString(WINDOWS_SEPARATOR);

    public static final char SYSTEM_SEPARATOR = File.separatorChar;
    
	public static final String PARENT_PATH = "..";

	public static final String CURRENT_PATH = ".";

    protected Paths() {
        super();
    }

    //-----------------------------------------------------------------------
    
	/**
	 * Normalize the path by suppressing sequences like "path/.." and
	 * inner simple dots.
	 * <p>The result is convenient for path comparison. For other uses,
	 * notice that Windows separators ("\") are replaced by simple slashes.
	 * @param path the original path
	 * @return the normalized path
	 */
	public static String normalize(String path) {
		if (path == null) {
			return Strings.EMPTY;
		}
		String pathToUse = Strings.replace(path, WINDOWS_SEPARATOR, UNIX_SEPARATOR);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			pathToUse = pathToUse.substring(prefixIndex + 1);
		}
		if (pathToUse.startsWith(UNIX_SEPARATOR_STR)) {
			prefix = prefix + UNIX_SEPARATOR;
			pathToUse = pathToUse.substring(1);
		}

		String[] pathArray = Strings.split(pathToUse, UNIX_SEPARATOR_STR, false, false);
		List<String> pathElements = new LinkedList<String>();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			String element = pathArray[i];
			if (CURRENT_PATH.equals(element)) {
				// Points to current directory - drop it.
			}
			else if (PARENT_PATH.equals(element)) {
				// Registering top path found.
				tops++;
			}
			else {
				if (tops > 0) {
					// Merging path element with element corresponding to top path.
					tops--;
				}
				else {
					// Normal path element found.
					pathElements.add(0, element);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, PARENT_PATH);
		}

		return prefix + Strings.join(pathElements, UNIX_SEPARATOR_STR);
	}
	
	/*
	 * Extract the filename from the given path,
	 * e.g. "mypath/myfile.txt" -> "myfile.txt".
	 * @param path the file path (may be <code>null</code>)
	 * @return the extracted filename, or <code>null</code> if none
	 */
	public static String getFileName(String path) {
		if (path == null) {
			return Strings.EMPTY;
		}
		int separatorIndex = indexOfLastSeparator(path);
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
	}

	/**
	 * Extract the filename extension from the given path,
	 * e.g. "mypath/myfile.txt" -> "txt".
	 * @param path the file path (may be <code>null</code>)
	 * @return the extracted filename extension, or <code>null</code> if none
	 */
	public static String getFileExtension(String path) {
		if (path == null) {
			return Strings.EMPTY;
		}
		
		int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		if (extIndex == -1) {
			return Strings.EMPTY;
		}
		
		int folderIndex = indexOfLastSeparator(path);
		if (folderIndex > extIndex) {
			return Strings.EMPTY;
		}
		return path.substring(extIndex + 1);
	}	
	
    /**
     * Returns the index of the last directory separator character.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The position of the last forward or backslash is returned.
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * 
     * @param filename  the filename to find the last path separator in, null returns -1
     * @return the index of the last separator character, or -1 if there
     * is no such character
     */
    static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }
        int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    /**
     * Determines if Windows file system is in use.
     * 
     * @return true if the system is Windows
     */
    static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
    }
}
