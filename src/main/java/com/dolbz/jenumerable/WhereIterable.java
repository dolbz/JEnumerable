package com.dolbz.jenumerable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.dolbz.jenumerable.altlambda.IndexPredicate;
import com.dolbz.jenumerable.altlambda.Predicate;

class WhereIterable<TSource> implements Iterable<TSource> {

	private final Iterable<TSource> source;
	private final IndexPredicate<TSource> predicate;

	public WhereIterable(final Predicate<TSource> predicate,
			final Iterable<TSource> source) {
		this.source = source;
		this.predicate = new IndexPredicate<TSource>() {

			@Override
			public boolean check(final TSource source, final Integer index) {
				return predicate.check(source);
			}
		};
	}

	public WhereIterable(final IndexPredicate<TSource> predicate,
			final Iterable<TSource> source) {
		this.source = source;
		this.predicate = predicate;
	}

	public Iterator<TSource> iterator() {
		return new WhereIterator();
	}

	private class WhereIterator implements Iterator<TSource> {

		private final Iterator<TSource> sourceIterator = source.iterator();
		private Integer index = 0;
		private boolean onCheckedMatch = false;
		private TSource currentItem;

		public boolean hasNext() {
			if (onCheckedMatch) {
				return true;
			} else {
				while (sourceIterator.hasNext()) {
					currentItem = sourceIterator.next();
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
			} else {
				while (sourceIterator.hasNext()) {
					currentItem = sourceIterator.next();
					if (predicate.check(currentItem, index++)) {
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
