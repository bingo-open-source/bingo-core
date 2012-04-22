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
package bingo.utils;

import java.util.Map;

/**
 * <code>null</code> safe {@link Map} utility.
 * 
 * @author fenghm (fenghm@bingosoft.net)
 * 
 * @since 0.1
 */
public class Maps {

	protected Maps() {

	}

	/**
	 * Return <code>true</code> if the supplied Map is <code>null</code> or empty. Otherwise, return <code>false</code>.
	 * 
	 * @param map the Map to check
	 * 
	 * @return whether the given Map is empty
	 */
	public static boolean isEmpty(Map<?,?> map) {
		return (map == null || map.isEmpty());
	}
}
