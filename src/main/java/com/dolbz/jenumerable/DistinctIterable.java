package com.dolbz.jenumerable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.dolbz.jenumerable.util.ComparableHashSet;
import com.dolbz.jenumerable.util.DefaultEqualityComparer;
import com.dolbz.jenumerable.util.EqualityComparer;

class DistinctIterable<TSource> implements Iterable<TSource> {

	private final Iterable<TSource> source;
	private final EqualityComparer<TSource> comparer;

	public DistinctIterable(final Iterable<TSource> source,
			final EqualityComparer<TSource> comparer) {
		this.source = source;
		if (comparer == null) {
			this.comparer = new DefaultEqualityComparer<TSource>();
		} else {
			this.comparer = comparer;
		}
	}

	public Iterator<TSource> iterator() {
		return new DistinctIterator();
	}

	private class DistinctIterator implements Iterator<TSource> {
		private TSource currentItem;
		private boolean onCheckedMatch = false;
		private final Iterator<TSource> sourceIterator = source.iterator();
		private final ComparableHashSet<TSource> seenElements = new ComparableHashSet<TSource>(
				comparer);

		public boolean hasNext() {
			if (onCheckedMatch) {
				return true;
			} else {
				while (sourceIterator.hasNext()) {
					currentItem = sourceIterator.next();
					if (seenElements.add(currentItem)) {
						onCheckedMatch = true;
						return true;
					}
				}
				return false;
			}
		}

		public TSource next() {
			if (onCheckedMatch) {
				onCheckedMatch = false;
				return currentItem;
			} else {
				while (sourceIterator.hasNext()) {
					currentItem = sourceIterator.next();

					if (seenElements.add(currentItem)) {
						return currentItem;
					}
				}
			}
			throw new NoSuchElementException();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
