package com.dolbz.jenumerable;

import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.dolbz.jenumerable.altlambda.Translator;
import com.dolbz.jenumerable.helpers.JEnumerableAssert;
import com.dolbz.jenumerable.helpers.ThrowingIterable;
import com.dolbz.jenumerable.util.DefaultEqualityComparer;

public class IntersectTest extends JEnumerableTestBase {
	@Test
	public void nullSecondWithoutComparer() {
		JEnumerable<String> first = new JEnumerable<String>(new String[] {});
		JEnumerable<String> second = null;

		exception.expect(IllegalArgumentException.class);
		first.except(second);
	}

	@Test
	public void nullSecondWithComparer() {
		JEnumerable<String> first = new JEnumerable<String>(new String[] {});
		JEnumerable<String> second = null;

		exception.expect(IllegalArgumentException.class);
		first.except(second, new DefaultEqualityComparer<String>());
	}

	@Test
	public void noComparerSpecified() {
		JEnumerable<String> first = new JEnumerable<String>(new String[] { "A",
				"a", "b", "c", "b", "c" });
		JEnumerable<String> second = new JEnumerable<String>(new String[] {
				"b", "a", "d", "a" });

		JEnumerable<String> expected = new JEnumerable<String>(new String[] {
				"A", "c" });

		JEnumerableAssert.assertEqual(expected, first.except(second));
	}

	@Test
	public void nullComparerSpecified() {
		JEnumerable<String> first = new JEnumerable<String>(new String[] { "A",
				"a", "b", "c", "b", "c" });
		JEnumerable<String> second = new JEnumerable<String>(new String[] {
				"b", "a", "d", "a" });

		JEnumerable<String> expected = new JEnumerable<String>(new String[] {
				"A", "c" });

		JEnumerableAssert.assertEqual(expected, first.except(second));
	}

	@Test
	public void caseInsensitiveComparerSpecified() {
		JEnumerable<String> first = new JEnumerable<String>(new String[] { "A",
				"a", "b", "c", "b" });
		JEnumerable<String> second = new JEnumerable<String>(new String[] {
				"b", "a", "d", "a" });

		JEnumerable<String> expected = new JEnumerable<String>(
				new String[] { "c" });

		JEnumerableAssert.assertEqual(expected,
				first.except(second, new CaseInsensitiveStringComparer()));
	}

	@Test
	@Ignore
	public void noSequencesUsedBeforeIteration() {
		// Ignored as the second sequence is currently evaluated when you
		// request the iterator instead of when you start using it. I don't
		// think this a major issue but I may change my mind...
		JEnumerable<String> first = new JEnumerable<String>(
				new ThrowingIterable<String>());
		JEnumerable<String> second = new JEnumerable<String>(
				new ThrowingIterable<String>());

		JEnumerable<String> result = first.except(second);
		Iterator<String> iterator = result.iterator();
		exception.expect(UnsupportedOperationException.class);
		iterator.next();
	}

	@Test
	public void secondSequenceReadFullyOnFirstResultIteration() {
		JEnumerable<Integer> first = new JEnumerable<Integer>(
				new Integer[] { 1 });
		JEnumerable<Integer> second = new JEnumerable<Integer>(new Integer[] {
				10, 2, 0 });
		JEnumerable<Integer> secondQuery = second
				.select(new Translator<Integer, Integer>() {

					@Override
					public Integer translate(final Integer source) {
						return 10 / source;
					}
				});

		JEnumerable<Integer> result = first.except(secondQuery);
		exception.expect(ArithmeticException.class);
		result.iterator().next();
	}

	@Test
	public void firstSequenceOnlyReadAsResultsAreRead() {
		JEnumerable<Integer> first = new JEnumerable<Integer>(new Integer[] {
				10, 2, 0, 2 });
		JEnumerable<Integer> firstQuery = first
				.select(new Translator<Integer, Integer>() {

					@Override
					public Integer translate(final Integer source) {
						return 10 / source;
					}
				});
		JEnumerable<Integer> second = new JEnumerable<Integer>(
				new Integer[] { 1 });

		JEnumerable<Integer> result = firstQuery.except(second);
		Iterator<Integer> iterator = result.iterator();
		Assert.assertEquals((Integer) 5, iterator.next());
		exception.expect(ArithmeticException.class);
		iterator.next();
	}
}
