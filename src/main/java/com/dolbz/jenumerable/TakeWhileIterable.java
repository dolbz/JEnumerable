package com.dolbz.jenumerable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.dolbz.jenumerable.altlambda.IndexPredicate;

class TakeWhileIterable<TSource> implements Iterable<TSource> {

	private final Iterable<TSource> source;
	private final IndexPredicate<TSource> predicate;

	public TakeWhileIterable(final Iterable<TSource> source,
			final IndexPredicate<TSource> predicate) {
		this.source = source;
		this.predicate = predicate;
	}

	public Iterator<TSource> iterator() {
		return new TakeWhileIterator();
	}

	private class TakeWhileIterator implements Iterator<TSource> {

		Iterator<TSource> sourceIterator = source.iterator();
		private Integer index = 0;
		private boolean onCheckedMatch = false;
		private TSource currentItem;

		public boolean hasNext() {
			if (onCheckedMatch) {
				return true;
			} else {
				if (sourceIterator.hasNext()) {
					if (!onCheckedMatch) {
						currentItem = sourceIterator.next();
					}
					if (predicate.check(currentItem, index++)) {
						onCheckedMatch = true;
						return true;
					}
				}
			}
			return false;
		}

		public TSource next() {
			if (onCheckedMatch) {
				onCheckedMatch = false;
				return currentItem;
			} else if (sourceIterator.hasNext()) {
				currentItem = sourceIterator.next();
				if (predicate.check(currentItem, index++)) {
					return currentItem;
				}
			}
			throw new NoSuchElementException();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
