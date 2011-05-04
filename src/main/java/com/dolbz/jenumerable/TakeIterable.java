package com.dolbz.jenumerable;

import java.util.Iterator;
import java.util.NoSuchElementException;

class TakeIterable<TSource> implements Iterable<TSource> {

	private final int count;
	private final Iterable<TSource> source;

	public TakeIterable(final int count, final Iterable<TSource> source) {
		this.count = count > 0 ? count : 0;
		this.source = source;
	}

	public Iterator<TSource> iterator() {
		return new TakeIterator();
	}

	private class TakeIterator implements Iterator<TSource> {

		private int currentIndex = 0;
		Iterator<TSource> sourceIterator = source.iterator();

		public boolean hasNext() {
			if (currentIndex < count) {
				return sourceIterator.hasNext();
			} else {
				return false;
			}
		}

		public TSource next() {
			if (currentIndex < count) {
				currentIndex++;
				return sourceIterator.next();
			}

			throw new NoSuchElementException();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
