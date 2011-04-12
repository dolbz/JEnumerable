package com.dolbz.jenumerable;

import java.util.Arrays;

import org.junit.Test;

import com.dolbz.jenumerable.helpers.JEnumerableAssert;

public class RangeTest extends JEnumerableTestBase {

	@Test
	public void simpleRangeIsExpectedTest() {
		Integer[] expected = { 5, 6, 7 };

		JEnumerable<Integer> expectedRange = new JEnumerable<Integer>(
				Arrays.asList(expected));

		JEnumerable<Integer> actualRange = JEnumerable.range(5, 3);

		JEnumerableAssert.assertEqual(expectedRange, actualRange);
	}

	@Test
	public void integerOverflowRangeTest() {
		Integer[] expected = { Integer.MAX_VALUE, Integer.MIN_VALUE,
				Integer.MIN_VALUE + 1 };

		JEnumerable<Integer> expectedRange = new JEnumerable<Integer>(
				Arrays.asList(expected));

		JEnumerable<Integer> actualRange = JEnumerable.range(Integer.MAX_VALUE,
				3);

		JEnumerableAssert.assertEqual(expectedRange, actualRange);
	}

	@Test
	public void negativeStartRangeTest() {
		Integer[] expected = { -2, -1, 0, 1, 2 };

		JEnumerable<Integer> expectedRange = new JEnumerable<Integer>(
				Arrays.asList(expected));

		JEnumerable<Integer> actualRange = JEnumerable.range(-2, 5);

		JEnumerableAssert.assertEqual(expectedRange, actualRange);
	}

	@Test
	public void invalidCountTest() {
		exception.expect(IllegalArgumentException.class);
		JEnumerable.range(50, -5);
	}
}
