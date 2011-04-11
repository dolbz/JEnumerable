package com.dolbz.jenumerable;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.dolbz.jenumerable.helpers.JEnumerableAssert;

public class SelectTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	// TODO IndexPredicate test

	@Test
	public void simpleProjectionToDifferentType() {
		Integer[] source = { 1, 5, 2 };

		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(
				Arrays.asList(source));

		JEnumerable<String> result = sourceWrap
				.select(new Translator<Integer, String>() {
					@Override
					public String translate(final Integer source) {
						return source.toString();
					}
				});

		JEnumerable<String> expected = new JEnumerable<String>(
				Arrays.asList(new String[] { "1", "5", "2" }));

		JEnumerableAssert.assertEqual(expected, result);
	}

	@Test
	public void NullSelectorThrowsIllegalArgumentException() {
		Integer[] source = { 1, 3, 7, 9, 10 };
		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(
				Arrays.asList(source));

		exception.expect(IllegalArgumentException.class);
		sourceWrap.select((IndexTranslator<Integer, String>) null);
	}
}
