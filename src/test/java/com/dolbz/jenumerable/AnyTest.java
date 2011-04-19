package com.dolbz.jenumerable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.dolbz.jenumerable.altlambda.Predicate;

public class AnyTest extends JEnumerableTestBase {
	@Test
	public void emptySequenceReturnsFalseWithPredicate() {
		List<Integer> emptyList = new ArrayList<Integer>();
		JEnumerable<Integer> emptyJEnumerable = new JEnumerable<Integer>(
				emptyList);

		boolean result = emptyJEnumerable.any(new Predicate<Integer>() {
			@Override
			public boolean check(final Integer source) {
				// return true as that would fail the test if the predicate was
				// ever executed
				return true;
			}
		});

		Assert.assertFalse(
				"Any didn't return false for an empty sequence. Called with a predicate",
				result);
	}

	@Test
	public void emptySequenceReturnsFalseWithoutPredicate() {
		List<Integer> emptyList = new ArrayList<Integer>();
		JEnumerable<Integer> emptyJEnumerable = new JEnumerable<Integer>(
				emptyList);

		boolean result = emptyJEnumerable.any();

		Assert.assertFalse(
				"Any didn't return false for an empty sequence. Called without a predicate",
				result);
	}

	@Test
	public void nonEmptySequenceReturnsTrueWithoutPredicate() {
		List<Integer> emptyList = new ArrayList<Integer>();
		emptyList.add(1);

		JEnumerable<Integer> emptyJEnumerable = new JEnumerable<Integer>(
				emptyList);

		boolean result = emptyJEnumerable.any();

		Assert.assertTrue(
				"Any didn't return true for a non empty sequence. Called without a predicate",
				result);
	}

	@Test
	public void noSequenceElementsMatchPredicateReturnsFalse() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);

		JEnumerable<Integer> enumerator = new JEnumerable<Integer>(list);

		boolean result = enumerator.any(new Predicate<Integer>() {
			@Override
			public boolean check(final Integer source) {
				return source > 3;
			}
		});

		Assert.assertFalse(
				"Any returned true for a sequence where no elements matched the predicate",
				result);
	}

	@Test
	public void someElementsMatchPredicateReturnsTrue() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

		JEnumerable<Integer> enumerator = new JEnumerable<Integer>(list);

		boolean result = enumerator.any(new Predicate<Integer>() {
			@Override
			public boolean check(final Integer source) {
				return source > 3;
			}
		});

		Assert.assertTrue(
				"Any returned false for a sequence with predicate matches",
				result);
	}
}
