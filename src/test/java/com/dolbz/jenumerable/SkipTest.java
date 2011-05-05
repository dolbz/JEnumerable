package com.dolbz.jenumerable;

import org.junit.Test;

import com.dolbz.jenumerable.helpers.JEnumerableAssert;
import com.dolbz.jenumerable.helpers.ThrowingIterable;

public class SkipTest extends JEnumerableTestBase {

	@Test
	public void executionIsDeferred() {
		new JEnumerable<String>(new ThrowingIterable<String>()).skip(10);
	}

	@Test
	public void negativeCount() {
		JEnumerable<Integer> result = JEnumerable.range(0, 5).skip(-5);

		JEnumerableAssert.assertEqual(JEnumerable.range(0, 5), result);
	}

	@Test
	public void zeroCount() {
		JEnumerable<Integer> result = JEnumerable.range(0, 5).skip(0);

		JEnumerableAssert.assertEqual(JEnumerable.range(0, 5), result);
	}

	@Test
	public void countShorterThanSource() {
		JEnumerable<Integer> result = JEnumerable.range(0, 5).skip(3);

		JEnumerableAssert.assertEqual(new JEnumerable<Integer>(new Integer[] {
				3, 4 }), result);
	}

	@Test
	public void countEqualToSourceLength() {
		JEnumerable<Integer> result = JEnumerable.range(0, 5).skip(5);

		JEnumerableAssert.assertEqual(
				new JEnumerable<Integer>(new Integer[] {}), result);
	}

	@Test
	public void countGreatherThanSourceLength() {
		JEnumerable<Integer> result = JEnumerable.range(0, 5).skip(100);

		JEnumerableAssert.assertEqual(
				new JEnumerable<Integer>(new Integer[] {}), result);
	}
}
