/*
 * MappedRangeResultV2.java
 *
 * This source file is part of the FoundationDB open source project
 *
 * Copyright 2013-2022 Apple Inc. and the FoundationDB project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apple.foundationdb;

import com.apple.foundationdb.tuple.ByteArrayUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MappedRangeResultV2 {
	final List<MappedKeyValueV2> values;
	final boolean more;

	public MappedRangeResultV2(MappedKeyValueV2[] values, boolean more) {
		this.values = Arrays.asList(values);
		this.more = more;
	}

	MappedRangeResultV2(MappedRangeResultDirectBufferIteratorV2 iterator) {
		iterator.readResultsSummary();
		more = iterator.hasMore();

		int count = iterator.count();
		values = new ArrayList<>(count);

		for (int i = 0; i < count; ++i) {
			values.add(iterator.next());
		}
	}

	public RangeResultSummary getSummary() {
		final int keyCount = values.size();
		final byte[] lastKey = keyCount > 0 ? values.get(keyCount - 1).getKey() : null;
		return new RangeResultSummary(lastKey, keyCount, more);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("MappedRangeResultV2{");
		sb.append("values=").append(values);
		sb.append(", more=").append(more);
		sb.append('}');
		return sb.toString();
	}
}
