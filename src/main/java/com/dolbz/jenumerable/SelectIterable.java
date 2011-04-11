package com.dolbz.jenumerable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.dolbz.jenumerable.altlambda.IndexTranslator;
import com.dolbz.jenumerable.altlambda.Translator;

class SelectIterable<TSource, TResult> implements Iterable<TResult> {

	private final Iterable<TSource> source;
	private final IndexTranslator<TSource, TResult> selector;

	public SelectIterable(final Translator<TSource, TResult> selector,
			final Iterable<TSource> source) {
		this.source = source;
		this.selector = new IndexTranslator<TSource, TResult>() {

			@Override
			public TResult translate(final TSource source, final Integer index) {
				return selector.translate(source);
			}
		};
	}

	public SelectIterable(final IndexTranslator<TSource, TResult> selector,
			final Iterable<TSource> source) {
		this.source = source;
		this.selector = selector;
	}

	public Iterator<TResult> iterator() {
		return new SelectIterator();
	}

	private class SelectIterator implements Iterator<TResult> {

		Iterator<TSource> sourceIterator = source.iterator();
		Integer index = 0;

		public boolean hasNext() {
			return sourceIterator.hasNext();
		}

		public TResult next() {
			if (sourceIterator.hasNext()) {
				return selector.translate(sourceIterator.next(), index++);
			}
			throw new NoSuchElementException();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
}
