package com.dolbz.jenumerable.util;

public class DefaultEqualityComparer<T> implements EqualityComparer<T> {
	public boolean equals(final T x, final T y) {
		if (x == null && y == null) {
			return true;
		} else if (x == null || y == null) {
			return false;
		} else {
			return x.equals(y);
		}
	}

	public int getHashCode(final T obj) {
		if (obj == null) {
			return 0;
		} else {
			return obj.hashCode();
		}
	}
}
