package com.dolbz.jenumerable;

import org.junit.Test;

import com.dolbz.jenumerable.altlambda.Translator;
import com.dolbz.jenumerable.helpers.JEnumerableAssert;
import com.dolbz.jenumerable.helpers.ThrowingIterable;

public class TakeTest extends JEnumerableTestBase {

	@Test
	public void executionIsDeferred() {
		new JEnumerable<String>(new ThrowingIterable<String>()).take(10);
	}

	@Test
	public void negativeCount() {
		JEnumerable<Integer> result = JEnumerable.range(0, 5).take(-5);

		JEnumerableAssert.assertEqual(
				new JEnumerable<Integer>(new Integer[] {}), result);
	}

	@Test
	public void zeroCount() {
		JEnumerable<Integer> result = JEnumerable.range(0, 5).take(0);

		JEnumerableAssert.assertEqual(
				new JEnumerable<Integer>(new Integer[] {}), result);
	}

	@Test
	public void countShorterThanSource() {
		JEnumerable<Integer> result = JEnumerable.range(0, 5).take(3);

		JEnumerableAssert.assertEqual(new JEnumerable<Integer>(new Integer[] {
				0, 1, 2 }), result);
	}

	@Test
	public void countEqualToSourceLength() {
		JEnumerable<Integer> result = JEnumerable.range(0, 5).take(5);

		JEnumerableAssert.assertEqual(new JEnumerable<Integer>(new Integer[] {
				0, 1, 2, 3, 4 }), result);
	}

	@Test
	public void countGreatherThanSourceLength() {
		JEnumerable<Integer> result = JEnumerable.range(0, 5).take(100);

		JEnumerableAssert.assertEqual(new JEnumerable<Integer>(new Integer[] {
				0, 1, 2, 3, 4 }), result);
	}

	@Test
	public void onlyEnumerateTheGivenNumberOfElements() {
		JEnumerable<Integer> source = new JEnumerable<Integer>(new Integer[] {
				1, 2, 0 });

		JEnumerable<Integer> query = source
				.select(new Translator<Integer, Integer>() {
					@Override
					public Integer translate(final Integer source) {
						return 10 / source;
					}
				});

		JEnumerable<Integer> result = query.take(2);

		JEnumerableAssert.assertEqual(new JEnumerable<Integer>(new Integer[] {
				10, 5 }), result);
	}
}
