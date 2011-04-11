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

		Iterator<TSource> sourceIterator = source.iterator();
		Integer index = 0;
		boolean onCheckedMatch = false;
		private TSource currentItem;

		public boolean hasNext() {
			// There is no concept of hasNext for IEnumerable. Have to jump
			// through hoops to ensure hasNext() and next() work nicely with the
			// deferred execution
			while (sourceIterator.hasNext()) {
				if (!onCheckedMatch) {
					currentItem = sourceIterator.next();
				}
				if (predicate.check(currentItem, index++)) {
					onCheckedMatch = true;
					return true;
				}
			}
			return false;
		}

		public TSource next() {
			while (onCheckedMatch || sourceIterator.hasNext()) {
				if (!onCheckedMatch) {
					currentItem = sourceIterator.next();
				}
				if (predicate.check(currentItem, index++)) {
					onCheckedMatch = false;
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
