package com.dolbz.jenumerable;

import java.util.Iterator;

class SkipIterable<TSource> implements Iterable<TSource> {

	private final int count;
	private final Iterable<TSource> source;

	public SkipIterable(final int count, final Iterable<TSource> source) {
		this.count = count > 0 ? count : 0;
		this.source = source;
	}

	public Iterator<TSource> iterator() {
		return new SkipIterator();
	}

	private class SkipIterator implements Iterator<TSource> {

		private int currentIndex = 0;
		Iterator<TSource> sourceIterator = source.iterator();

		public boolean hasNext() {
			skipToStart();
			return sourceIterator.hasNext();
		}

		public TSource next() {
			skipToStart();
			return sourceIterator.next();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		private void skipToStart() {
			while (currentIndex < count) {
				if (sourceIterator.hasNext()) {
					sourceIterator.next();
				}
				currentIndex++;
			}
		}
	}
}
