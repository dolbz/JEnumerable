package com.dolbz.jenumerable.helpers;

import java.util.Iterator;

public class ThrowingIterable<T> implements Iterable<T> {

	public Iterator<T> iterator() {
		return new ThrowingIterator();
	}

	private class ThrowingIterator implements Iterator<T> {

		public boolean hasNext() {
			return true;
		}

		public T next() {
			throw new UnsupportedOperationException();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
}
