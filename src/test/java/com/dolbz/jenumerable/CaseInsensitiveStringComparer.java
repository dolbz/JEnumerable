package com.dolbz.jenumerable;

import com.dolbz.jenumerable.util.EqualityComparer;

final class CaseInsensitiveStringComparer implements
		EqualityComparer<String> {
	public int getHashCode(final String obj) {
		return obj.toLowerCase().hashCode();
	}

	public boolean equals(final String x, final String y) {
		return x.toLowerCase().equals(y.toLowerCase());
	}
}