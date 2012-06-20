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
package bingo.lang.tuple;

import java.io.Serializable;
import java.util.Map;

import bingo.lang.Objects;
import bingo.lang.builder.CompareToBuilder;

/**
 * <p>
 * A pair consisting of two elements.
 * </p>
 * 
 * <p>
 * This class is an abstract implementation defining the basic API. It refers to the elements as 'left' and 'right'. It
 * also implements the {@code Map.Entry} interface where the key is 'left' and the value is 'right'.
 * </p>
 * 
 * <p>
 * Subclass implementations may be mutable or immutable. However, there is no restriction on the type of the stored
 * objects that may be stored. If mutable objects are stored in the pair, then the pair itself effectively becomes
 * mutable.
 * </p>
 */
abstract class PairBase<L, R> implements Comparable<Pair<L, R>>, Serializable, Pair<L, R> {

	private static final long	serialVersionUID	= 4954918890077093841L;

	public int compareTo(Pair<L, R> other) {
		return new CompareToBuilder().append(getLeft(), other.getLeft()).append(getRight(), other.getRight()).toComparison();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Map.Entry<?, ?>) {
			Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
			return Objects.equals(getLeft(), other.getKey()) && Objects.equals(getRight(), other.getValue());
		}
		if (obj instanceof PairBase<?, ?>) {
			Pair<?, ?> other = (Pair<?, ?>) obj;
			return Objects.equals(getLeft(), other.getLeft()) && Objects.equals(getRight(), other.getRight());
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		// see Map.Entry API specification
		return (getLeft() == null ? 0 : getLeft().hashCode()) ^ (getRight() == null ? 0 : getRight().hashCode());
	}

	@Override
	public String toString() {
		return new StringBuilder().append('(').append(getLeft()).append(',').append(getRight()).append(')').toString();
	}
}
