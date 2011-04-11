package com.dolbz.jenumerable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation class for JEnumerable. Has/will have equivalent methods to
 * .NET's System.Linq.Enumerable class
 * 
 * @see http://msdn.microsoft.com/en-us/library/system.linq.enumerable.aspx
 * 
 *      Using an interface as Java's @java.lang.Iterable can't have extension
 *      methods attached to it :( JEnumerable implementers will probably wrap
 *      Iterable's
 * 
 * @author nrandle
 * 
 */
public class JEnumerable<TSource> implements Iterable<TSource> {

	public Iterable<TSource> wrappedIter;

	public JEnumerable(final Iterable<TSource> source) {
		wrappedIter = source;
	}

	public Iterator<TSource> iterator() {
		return wrappedIter.iterator();
	}

	JEnumerable<TSource> where(final Predicate<TSource> predicate) {
		// Identical logic to the indexed where overload. Pass it on with an
		// index predicate
		return where(new IndexPredicate<TSource>() {

			@Override
			protected boolean check(final TSource source, final Integer index) {
				return predicate.check(source);
			}
		});
	}

	JEnumerable<TSource> where(final IndexPredicate<TSource> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("predicate is null");
		}

		List<TSource> resultList = new ArrayList<TSource>();

		int index = 0;
		for (TSource item : wrappedIter) {
			if (predicate.check(item, index)) {
				resultList.add(item);
			}
			;
			index++;
		}

		return new JEnumerable<TSource>(resultList);
	}

	public <TResult> JEnumerable<TResult> select(
			final Translator<TSource, TResult> selector) {
		return select(new IndexTranslator<TSource, TResult>() {
			@Override
			public TResult translate(final TSource source, final Integer index) {
				return selector.translate(source);
			}
		});
	}

	public <TResult> JEnumerable<TResult> select(
			final IndexTranslator<TSource, TResult> selector) {
		if (selector == null) {
			throw new IllegalArgumentException("selector is null");
		}

		List<TResult> resultList = new ArrayList<TResult>();

		int index = 0;
		for (TSource item : wrappedIter) {
			resultList.add(selector.translate(item, index));
			index++;
		}

		return new JEnumerable<TResult>(resultList);
	}
}
