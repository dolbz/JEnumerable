package com.dolbz.jenumerable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.dolbz.jenumerable.altlambda.DualTranslator;
import com.dolbz.jenumerable.altlambda.IndexPredicate;
import com.dolbz.jenumerable.altlambda.IndexTranslator;
import com.dolbz.jenumerable.altlambda.Predicate;
import com.dolbz.jenumerable.altlambda.Translator;
import com.dolbz.jenumerable.exceptions.JEnumerableOverflowException;
import com.dolbz.jenumerable.interfaces.Grouping;
import com.dolbz.jenumerable.interfaces.Lookup;
import com.dolbz.jenumerable.util.DefaultEqualityComparer;
import com.dolbz.jenumerable.util.EqualityComparer;

/**
 * Implementation class for JEnumerable. Has/will have equivalent methods to
 * .NET's System.Linq.Enumerable class
 * 
 * @see http://msdn.microsoft.com/en-us/library/system.linq.enumerable.aspx
 * 
 *      Using a wrapper as Java's @java.lang.Iterable can't have extension
 *      methods attached to it :(
 * 
 * @author nrandle
 * 
 */
public class JEnumerable<TSource> implements Iterable<TSource> {

	private final Iterable<TSource> wrappedIterable;

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

	/**
	 * Constructor which takes an array of TSource elements
	 * 
	 * @param source
	 *            the source array
	 */
	public JEnumerable(final TSource[] source) {
		wrappedIterable = Arrays.asList(source);
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

	/** SelectMany **/

	// public <TResult> JEnumerable<TResult> selectMany() {
	// // TODO
	// }

	/** Any **/
	public boolean any() {
		if (wrappedIterable.iterator().hasNext()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean any(final Predicate<TSource> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("predicate is null");
		}

		for (TSource obj : wrappedIterable) {
			if (predicate.check(obj)) {
				return true;
			}
		}
		return false;
	}

	/** All **/
	public boolean all(final Predicate<TSource> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("predicate is null");
		}

		for (TSource obj : wrappedIterable) {
			if (!predicate.check(obj)) {
				return false;
			}
		}
		return true;
	}

	/** First **/
	public TSource first() {
		Iterator<TSource> iterator = wrappedIterable.iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}
		throw new IllegalStateException("Sequence was empty");
	}

	public TSource first(final Predicate<TSource> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("predicate is null");
		}

		for (TSource obj : wrappedIterable) {
			if (predicate.check(obj)) {
				return obj;
			}
		}
		throw new IllegalStateException("No elements matched the predicate");
	}

	/** Last **/
	public TSource last() {
		if (wrappedIterable.iterator().hasNext()) {
			TSource latest = null;
			for (TSource obj : wrappedIterable) {
				latest = obj;
			}
			return latest;
		} else {
			throw new IllegalStateException("Sequence was empty");
		}
	}

	public TSource last(final Predicate<TSource> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("predicate is null");
		}

		TSource latest = null;
		for (TSource obj : wrappedIterable) {
			if (predicate.check(obj)) {
				latest = obj;
			}
		}
		if (latest == null) {
			throw new IllegalStateException("No elements matched the predicate");
		} else {
			return latest;
		}
	}

	/** Single **/
	public TSource single() {
		Iterator<TSource> iterator = wrappedIterable.iterator();
		if (iterator.hasNext()) {
			TSource returnVal = iterator.next();
			if (iterator.hasNext()) {
				throw new IllegalStateException(
						"More than a single element in the sequence");
			} else {
				return returnVal;
			}
		}
		throw new IllegalStateException("Sequence was empty");
	}

	public TSource single(final Predicate<TSource> predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("predicate is null");
		}

		TSource returnCandidate = null;

		for (TSource obj : wrappedIterable) {
			if (predicate.check(obj)) {
				if (returnCandidate != null) {
					throw new IllegalStateException(
							"More than a single element in the sequence");
				}
				returnCandidate = obj;
			}
		}

		if (returnCandidate != null) {
			return returnCandidate;
		} else {
			throw new IllegalStateException("No elements matched the predicate");
		}
	}

	/** Distinct **/

	public JEnumerable<TSource> distinct() {
		return new JEnumerable<TSource>(new DistinctIterable<TSource>(
				wrappedIterable, new DefaultEqualityComparer<TSource>()));
	}

	public JEnumerable<TSource> distinct(
			final EqualityComparer<TSource> comparer) {
		return new JEnumerable<TSource>(new DistinctIterable<TSource>(
				wrappedIterable, comparer));
	}

	/** Union **/

	public JEnumerable<TSource> union(final JEnumerable<TSource> second) {
		if (second == null) {
			throw new IllegalArgumentException("second sequence is null");
		}

		return new JEnumerable<TSource>(
				new UnionIterable<TSource>(wrappedIterable, second,
						new DefaultEqualityComparer<TSource>()));
	}

	public JEnumerable<TSource> union(final JEnumerable<TSource> second,
			final EqualityComparer<TSource> comparer) {
		if (second == null) {
			throw new IllegalArgumentException("second sequence is null");
		}
		return new JEnumerable<TSource>(new UnionIterable<TSource>(
				wrappedIterable, second, comparer));
	}

	/** Intersect **/

	public JEnumerable<TSource> intersect(final JEnumerable<TSource> second) {
		if (second == null) {
			throw new IllegalArgumentException("second sequence is null");
		}

		return new JEnumerable<TSource>(
				new IntersectIterable<TSource>(wrappedIterable, second,
						new DefaultEqualityComparer<TSource>()));
	}

	public JEnumerable<TSource> intersect(final JEnumerable<TSource> second,
			final EqualityComparer<TSource> comparer) {
		if (second == null) {
			throw new IllegalArgumentException("second sequence is null");
		}
		return new JEnumerable<TSource>(new IntersectIterable<TSource>(
				wrappedIterable, second, comparer));
	}

	/** Except **/

	public JEnumerable<TSource> except(final JEnumerable<TSource> second) {
		if (second == null) {
			throw new IllegalArgumentException("second sequence is null");
		}

		return new JEnumerable<TSource>(
				new ExceptIterable<TSource>(wrappedIterable, second,
						new DefaultEqualityComparer<TSource>()));
	}

	public JEnumerable<TSource> except(final JEnumerable<TSource> second,
			final EqualityComparer<TSource> comparer) {
		if (second == null) {
			throw new IllegalArgumentException("second sequence is null");
		}
		return new JEnumerable<TSource>(new ExceptIterable<TSource>(
				wrappedIterable, second, comparer));
	}

	/** ToLookup **/

	public <TKey> Lookup<TKey, TSource> toLookup(
			final Translator<TSource, TKey> keySelector) {
		throw new NotImplementedException();
	}

	public <TKey> Lookup<TKey, TSource> toLookup(
			final Translator<TSource, TKey> keySelector,
			final EqualityComparer<TKey> comparer) {
		throw new NotImplementedException();
	}

	public <TKey, TElement> Lookup<TKey, TElement> toLookup(
			final Translator<TSource, TKey> keySelector,
			final Translator<TSource, TElement> elementSelector) {
		throw new NotImplementedException();
	}

	public <TKey, TElement> Lookup<TKey, TElement> toLookup(
			final Translator<TSource, TKey> keySelector,
			final Translator<TSource, TElement> elementSelector,
			final EqualityComparer<TKey> comparer) {
		throw new NotImplementedException();
	}

	/** Join **/
	// TOuter is the same as TSource. In java we can't choose a different name
	// so sticking with TSource

	public <TInner, TKey, TResult> JEnumerable<TResult> join(
			final JEnumerable<TInner> inner,
			final Translator<TSource, TKey> outerKeySelector,
			final Translator<TSource, TKey> innerKeySelector,
			final DualTranslator<TSource, TInner, TResult> resultSelector) {
		throw new NotImplementedException();
	}

	public <TInner, TKey, TResult> JEnumerable<TResult> join(
			final JEnumerable<TInner> inner,
			final Translator<TSource, TKey> outerKeySelector,
			final Translator<TSource, TKey> innerKeySelector,
			final DualTranslator<TSource, TInner, TResult> resultSelector,
			final EqualityComparer<TKey> comparer) {
		throw new NotImplementedException();
	}

	/** ToList **/

	public List<TSource> toList() {
		List<TSource> result = new ArrayList<TSource>();

		for (TSource elem : wrappedIterable) {
			result.add(elem);
		}
		return result;
	}

	/** GroupBy **/

	public <TKey> JEnumerable<Grouping<TKey, TSource>> groupBy(
			final Translator<TSource, TKey> keySelector) {
		throw new NotImplementedException();
	}

	public <TKey> JEnumerable<Grouping<TKey, TSource>> groupBy(
			final Translator<TSource, TKey> keySelector,
			final EqualityComparer<TKey> comparer) {
		throw new NotImplementedException();
	}

	public <TKey, TElement> JEnumerable<Grouping<TKey, TSource>> groupBy(
			final Translator<TSource, TKey> keySelector,
			final Translator<TSource, TElement> elementSelector) {
		throw new NotImplementedException();
	}

	public <TKey, TElement> JEnumerable<Grouping<TKey, TSource>> groupBy(
			final Translator<TSource, TKey> keySelector,
			final Translator<TSource, TElement> elementSelector,
			final EqualityComparer<TKey> comparer) {
		throw new NotImplementedException();
	}

	public <TKey, TResult> JEnumerable<TResult> groupBy(
			final Translator<TSource, TKey> keySelector,
			final DualTranslator<TKey, JEnumerable<TSource>, TResult> resultSelector) {
		throw new NotImplementedException();
	}

	public <TKey, TResult> JEnumerable<TResult> groupBy(
			final Translator<TSource, TKey> keySelector,
			final DualTranslator<TKey, JEnumerable<TSource>, TResult> resultSelector,
			final EqualityComparer<TKey> comparer) {
		throw new NotImplementedException();
	}

	public <TKey, TElement, TResult> JEnumerable<TResult> groupBy(
			final Translator<TSource, TKey> keySelector,
			final Translator<TSource, TElement> elementSelector,
			final DualTranslator<TKey, JEnumerable<TSource>, TResult> resultSelector) {
		throw new NotImplementedException();
	}

	public <TKey, TElement, TResult> JEnumerable<TResult> groupBy(
			final Translator<TSource, TKey> keySelector,
			final Translator<TSource, TElement> elementSelector,
			final DualTranslator<TKey, JEnumerable<TSource>, TResult> resultSelector,
			final EqualityComparer<TKey> comparer) {
		throw new NotImplementedException();
	}

	/** GroupJoin **/

	public <TInner, TKey, TResult> JEnumerable<TResult> groupJoin(
			final JEnumerable<TInner> inner,
			final Translator<TSource, TKey> outerKeySelector,
			final Translator<TInner, TKey> innerKeySelector,
			final DualTranslator<TSource, JEnumerable<TInner>, TResult> resultSelector) {
		throw new NotImplementedException();
	}

	public <TInner, TKey, TResult> JEnumerable<TResult> groupJoin(
			final JEnumerable<TInner> inner,
			final Translator<TSource, TKey> outerKeySelector,
			final Translator<TInner, TKey> innerKeySelector,
			final DualTranslator<TSource, JEnumerable<TInner>, TResult> resultSelector,
			final EqualityComparer<TKey> comparer) {
		throw new NotImplementedException();
	}

	/** Take **/

	public JEnumerable<TSource> take(final int count) {
		throw new NotImplementedException();
	}

	/** TakeWhile **/

	public JEnumerable<TSource> takeWhile(final Predicate<TSource> predicate) {
		throw new NotImplementedException();
	}

	public JEnumerable<TSource> takeWhile(
			final IndexPredicate<TSource> predicate) {
		throw new NotImplementedException();
	}

	/** Skip **/

	public JEnumerable<TSource> skip(final int count) {
		throw new NotImplementedException();
	}

	/** SkipWhile **/

	public JEnumerable<TSource> skipWhile(final Predicate<TSource> predicate) {
		throw new NotImplementedException();
	}

	public JEnumerable<TSource> skipWhile(
			final IndexPredicate<TSource> predicate) {
		throw new NotImplementedException();
	}

	/** ToArray **/

	public Object[] toArray() {
		// TODO this should return a TSource[]. I don't think this is possible
		// in Java due to type erasure? Could take a clazz parameter here to
		// return the correct array type? Something like...

		/*
		 * public static <T> T[] createArray(List<T> list,Class<T> clazz){ T[]
		 * array = (T[]) Array.newInstance(clazz, list.size()); for(int i = 0; i
		 * < array.length; i++){ array[i] = list.get(i); } return array; }
		 */

		return this.toList().toArray();
	}

	/** ToDictionary **/

	public <TKey> Dictionary<TKey, TSource> toDictionary(
			final Translator<TSource, TKey> keySelector) {
		throw new NotImplementedException();
	}

	public <TKey, TElement> Dictionary<TKey, TElement> toDictionary(
			final Translator<TSource, TKey> keySelector,
			final Translator<TSource, TElement> elementSelector) {
		throw new NotImplementedException();
	}

	public <TKey> Dictionary<TKey, TSource> toDictionary(
			final Translator<TSource, TKey> keySelector,
			final EqualityComparer<TKey> comparer) {
		throw new NotImplementedException();
	}

	public <TKey, TElement> Dictionary<TKey, TElement> toDictionary(
			final Translator<TSource, TKey> keySelector,
			final Translator<TSource, TElement> elementSelector,
			final EqualityComparer<TKey> comparer) {
		throw new NotImplementedException();
	}
}
