package com.dolbz.jenumerable;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.dolbz.jenumerable.altlambda.IndexPredicate;
import com.dolbz.jenumerable.altlambda.Predicate;
import com.dolbz.jenumerable.helpers.JEnumerableAssert;
import com.dolbz.jenumerable.helpers.ThrowingIterable;

public class WhereTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	// TODO IndexPredicate test

	@Test
	public void simpleFiltering() {
		Integer[] source = { 1, 3, 4, 2, 8, 1 };

		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(
				Arrays.asList(source));

		JEnumerable<Integer> result = sourceWrap
				.where(new Predicate<Integer>() {
					@Override
					public boolean check(final Integer source) {
						return source < 4;
					}
				});

		JEnumerable<Integer> expected = new JEnumerable<Integer>(
				Arrays.asList(new Integer[] { 1, 3, 2, 1 }));

		JEnumerableAssert.assertEqual(expected, result);
	}

	@Test
	public void NullPredicateThrowsIllegalArgumentException() {
		Integer[] source = { 1, 3, 7, 9, 10 };
		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(
				Arrays.asList(source));

		exception.expect(IllegalArgumentException.class);
		sourceWrap.where((IndexPredicate<Integer>) null);
	}

	@Test
	public void ExecutionIsDeferred() {
		JEnumerable<Integer> sourceEnumerable = new JEnumerable<Integer>(
				new ThrowingIterable<Integer>());

		sourceEnumerable.where(new Predicate<Integer>() {
			@Override
			public boolean check(final Integer source) {
				return true;
			}
		});
	}
}
