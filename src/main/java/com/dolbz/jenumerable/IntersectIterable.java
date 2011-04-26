package com.dolbz.jenumerable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.dolbz.jenumerable.util.ComparableHashSet;
import com.dolbz.jenumerable.util.DefaultEqualityComparer;
import com.dolbz.jenumerable.util.EqualityComparer;

class IntersectIterable<TSource> implements Iterable<TSource> {

	private final Iterable<TSource> first;
	private final Iterable<TSource> second;
	private final EqualityComparer<TSource> comparer;

	public IntersectIterable(final Iterable<TSource> first,
			final Iterable<TSource> second,
			final EqualityComparer<TSource> comparer) {
		this.first = first;
		this.second = second;
		if (comparer == null) {
			this.comparer = new DefaultEqualityComparer<TSource>();
		} else {
			this.comparer = comparer;
		}
	}

	public Iterator<TSource> iterator() {
		return new IntersectIterator();
	}

	private class IntersectIterator implements Iterator<TSource> {
		private TSource currentItem;
		private boolean onCheckedMatch = false;
		private final Iterator<TSource> firstIterator = first.iterator();
		private final ComparableHashSet<TSource> potentialElements = new ComparableHashSet<TSource>(
				second, comparer);

		public boolean hasNext() {
			if (onCheckedMatch) {
				return true;
			}
			while (firstIterator.hasNext()) {
				currentItem = firstIterator.next();
				if (potentialElements.remove(currentItem)) {
					onCheckedMatch = true;
					return true;
				}
			}
			return false;
		}

		public TSource next() {
			if (onCheckedMatch) {
				onCheckedMatch = false;
				return currentItem;
			} else {
				while (firstIterator.hasNext()) {
					currentItem = firstIterator.next();
					if (potentialElements.remove(currentItem)) {
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