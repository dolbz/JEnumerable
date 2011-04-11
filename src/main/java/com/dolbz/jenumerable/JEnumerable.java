package com.dolbz.jenumerable;

import java.util.Iterator;

import com.dolbz.jenumerable.altlambda.IndexPredicate;
import com.dolbz.jenumerable.altlambda.IndexTranslator;
import com.dolbz.jenumerable.altlambda.Predicate;
import com.dolbz.jenumerable.altlambda.Translator;

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

	public Iterable<TSource> wrappedIterable;

	/**
	 * Constructor to wrap an @Iterable so that we can unlock the JEnumerable
	 * goodness.
	 * 
	 * @param source
	 */
	public JEnumerable(final Iterable<TSource> source) {
		wrappedIterable = source;
	}

	public Iterator<TSource> iterator() {
		return wrappedIterable.iterator();
	}

	/** Where **/

	public JEnumerable<TSource> where(final Predicate<TSource> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("predicate is null");
		}

		return new JEnumerable<TSource>(new WhereIterable<TSource>(predicate,
				this.wrappedIterable));
	}

	public JEnumerable<TSource> where(final IndexPredicate<TSource> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("predicate is null");
		}

		return new JEnumerable<TSource>(new WhereIterable<TSource>(predicate,
				this.wrappedIterable));
	}

	/** Select **/

	public <TResult> JEnumerable<TResult> select(
			final Translator<TSource, TResult> selector) {
		if (selector == null) {
			throw new IllegalArgumentException("selector is null");
		}

		return new JEnumerable<TResult>(new SelectIterable<TSource, TResult>(
				selector, this.wrappedIterable));
	}

	public <TResult> JEnumerable<TResult> select(
			final IndexTranslator<TSource, TResult> selector) {
		if (selector == null) {
			throw new IllegalArgumentException("selector is null");
		}

		return new JEnumerable<TResult>(new SelectIterable<TSource, TResult>(
				selector, this.wrappedIterable));
	}
}
