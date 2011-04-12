package com.dolbz.jenumerable;

import java.util.Collection;
import java.util.Iterator;

import com.dolbz.jenumerable.altlambda.IndexPredicate;
import com.dolbz.jenumerable.altlambda.IndexTranslator;
import com.dolbz.jenumerable.altlambda.Predicate;
import com.dolbz.jenumerable.altlambda.Translator;
import com.dolbz.jenumerable.exceptions.JEnumerableOverflowException;

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
		if (source == null) {
			throw new IllegalArgumentException("source is null");
		}

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

	/** Range **/

	public static JEnumerable<Integer> range(final int start, final int count) {
		if (count < 0) {
			throw new IllegalArgumentException("count is negative");
		}
		// Validating the range doesn't go out of bounds isn't required. Java
		// allows incrementing beyond Integer.MAX_VALUE

		return new JEnumerable<Integer>(new RangeIterable(start, count));
	}

	/** Empty **/
	public static <TResult> JEnumerable<TResult> empty() {
		// TODO requires caching which may be interesting...
		throw new IllegalStateException("Not Implemented yet");
	}

	/** Repeat **/
	public static <TResult> JEnumerable<TResult> repeat(final TResult element,
			final int count) {
		throw new IllegalStateException("Not Implemented yet");
	}

	/** Count **/
	public int count() {
		if (this.wrappedIterable instanceof Collection) {
			return ((Collection<TSource>) wrappedIterable).size();
		} else {
			return count(new Predicate<TSource>() {
				@Override
				public boolean check(final TSource source) {
					return true;
				}
			});
		}
	}

	public int count(final Predicate<TSource> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("predicate is null");
		}

		int count = 0;
		for (TSource item : wrappedIterable) {
			if (predicate.check(item)) {
				if (count == Integer.MAX_VALUE) {
					// Don't overflow back to MIN_VALUE as this could give an
					// incorrect count!
					throw new JEnumerableOverflowException(
							"Count is larger than the maximum value. Try longCount instead");
				} else {
					count++;
				}
			}
		}
		return count;
	}

	/** LongCount **/
	public long longCount() {
		return count(new Predicate<TSource>() {
			@Override
			public boolean check(final TSource source) {
				return true;
			}
		});
	}

	public long longCount(final Predicate<TSource> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("predicate is null");
		}

		long count = 0;
		for (TSource item : wrappedIterable) {
			if (predicate.check(item)) {
				if (count == Long.MAX_VALUE) {
					// Don't overflow back to MIN_VALUE as this could give an
					// incorrect count!
					throw new JEnumerableOverflowException(
							"Count is larger than the maximum value. Try longCount instead");
				} else {
					count++;
				}
			}
		}
		return count;
	}
}
