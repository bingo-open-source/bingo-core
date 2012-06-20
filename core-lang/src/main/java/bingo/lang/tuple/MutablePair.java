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

/**
 * <p>
 * A mutable pair consisting of two {@code Object} elements.
 * </p>
 */
public class MutablePair<L, R> extends PairBase<L, R> {

	private static final long	serialVersionUID	= 4954918890077093841L;

	public static <L, R> MutablePair<L, R> of(L left, R right) {
		return new MutablePair<L, R>(left, right);
	}

	private L	left;
	private R	right;

	public MutablePair() {
		super();
	}

	public MutablePair(L left, R right) {
		super();
		this.left = left;
		this.right = right;
	}

	public L getLeft() {
		return left;
	}

	public void setLeft(L left) {
		this.left = left;
	}

	public R getRight() {
		return right;
	}

	public void setRight(R right) {
		this.right = right;
	}
}
