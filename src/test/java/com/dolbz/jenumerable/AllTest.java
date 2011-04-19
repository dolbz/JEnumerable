package com.dolbz.jenumerable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.dolbz.jenumerable.altlambda.Predicate;

public class AllTest extends JEnumerableTestBase {
	@Test
	public void emptySequenceReturnsTrue() {
		List<Integer> emptyList = new ArrayList<Integer>();
		JEnumerable<Integer> emptyJEnumerable = new JEnumerable<Integer>(
				emptyList);

		boolean result = emptyJEnumerable.all(new Predicate<Integer>() {
			@Override
			public boolean check(final Integer source) {
				// return false as that would fail the all if it was ever
				// executed
				return false;
			}
		});

		Assert.assertTrue("All didn't return true for an empty sequence",
				result);
	}

	@Test
	public void noSequenceElementsMatchPredicateReturnsFalse() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);

		JEnumerable<Integer> enumerator = new JEnumerable<Integer>(list);

		boolean result = enumerator.all(new Predicate<Integer>() {
			@Override
			public boolean check(final Integer source) {
				return source > 3;
			}
		});

		Assert.assertFalse(
				"All returned true for a sequence where no elements matched the predicate",
				result);
	}

	@Test
	public void someElementsMatchPredicateReturnsFalse() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

		JEnumerable<Integer> enumerator = new JEnumerable<Integer>(list);

		boolean result = enumerator.all(new Predicate<Integer>() {
			@Override
			public boolean check(final Integer source) {
				return source > 3;
			}
		});

		Assert.assertFalse(
				"All returned true for a sequence with predicate no-matches",
				result);
	}

	@Test
	public void allElementsMatchPredicateReturnsTrue() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

		JEnumerable<Integer> enumerator = new JEnumerable<Integer>(list);

		boolean result = enumerator.all(new Predicate<Integer>() {
			@Override
			public boolean check(final Integer source) {
				return source > 0;
			}
		});

		Assert.assertTrue(
				"All returned false for a sequence with all predicate matches",
				result);
	}
}
