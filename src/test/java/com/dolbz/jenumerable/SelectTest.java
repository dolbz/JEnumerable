package com.dolbz.jenumerable;

import java.util.Arrays;

import org.junit.Test;

import com.dolbz.jenumerable.altlambda.IndexTranslator;
import com.dolbz.jenumerable.altlambda.Translator;
import com.dolbz.jenumerable.helpers.JEnumerableAssert;
import com.dolbz.jenumerable.helpers.ThrowingIterable;

public class SelectTest extends JEnumerableTestBase {

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
	public void nullSelectorThrowsIllegalArgumentException() {
		Integer[] source = { 1, 3, 7, 9, 10 };
		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(
				Arrays.asList(source));

		exception.expect(IllegalArgumentException.class);
		sourceWrap.select((IndexTranslator<Integer, String>) null);
	}

	@Test
	public void executionIsDeferred() {
		JEnumerable<Integer> sourceEnumerable = new JEnumerable<Integer>(
				new ThrowingIterable<Integer>());
		sourceEnumerable.select(new Translator<Integer, String>() {
			@Override
			public String translate(final Integer source) {
				return "Irrelevant";
			}
		});
	}
}
