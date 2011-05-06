package com.dolbz.jenumerable;

import java.util.Iterator;

import com.dolbz.jenumerable.altlambda.IndexPredicate;

class SkipWhileIterable<TSource> implements Iterable<TSource> {

	private final Iterable<TSource> source;
	private final IndexPredicate<TSource> predicate;

	public SkipWhileIterable(final Iterable<TSource> source,
			final IndexPredicate<TSource> predicate) {
		this.source = source;
		this.predicate = predicate;
	}

	public Iterator<TSource> iterator() {
		return new SkipWhileIterator();
	}

	private class SkipWhileIterator implements Iterator<TSource> {

		Iterator<TSource> sourceIterator = source.iterator();
		private int currentIndex = 0;
		private TSource currentItem;
		private boolean alreadySkipped = false;
		private boolean onCheckedMatch = false;

		public boolean hasNext() {
			if (onCheckedMatch) {
				return true;
			}
			skipToStart();
			return sourceIterator.hasNext();
		}

		public TSource next() {
			if (onCheckedMatch) {
				onCheckedMatch = false;
				return currentItem;
			}
			skipToStart();
			return sourceIterator.next();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		private void skipToStart() {
			while (!alreadySkipped && sourceIterator.hasNext()) {
				currentItem = sourceIterator.next();
				if (!predicate.check(currentItem, currentIndex++)) {
					onCheckedMatch = true;
					alreadySkipped = true;
				}
			}
		}
	}
}
