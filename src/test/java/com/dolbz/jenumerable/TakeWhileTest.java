package com.dolbz.jenumerable;

import org.junit.Test;

import com.dolbz.jenumerable.altlambda.IndexPredicate;
import com.dolbz.jenumerable.altlambda.Predicate;
import com.dolbz.jenumerable.helpers.JEnumerableAssert;
import com.dolbz.jenumerable.helpers.ThrowingIterable;

public class TakeWhileTest extends JEnumerableTestBase {

	@Test
	public void executionIsDeferred() {
		new JEnumerable<String>(new ThrowingIterable<String>())
				.takeWhile(new Predicate<String>() {

					@Override
					public boolean check(final String source) {
						return true;
					}
				});
	}

	@Test
	public void nullPredicateNoIndex() {
		exception.expect(IllegalArgumentException.class);
		JEnumerable.range(0, 5).takeWhile((Predicate<Integer>) null);
	}

	@Test
	public void nullPredicateWithIndex() {
		exception.expect(IllegalArgumentException.class);
		JEnumerable.range(0, 5).takeWhile((IndexPredicate<Integer>) null);
	}

	@Test
	public void predicateFailingFirstElement() {
		JEnumerable<String> source = new JEnumerable<String>(new String[] {
				"zero", "one", "two", "three", "four", "five", "six" });

		JEnumerable<String> result = source.takeWhile(new Predicate<String>() {
			@Override
			public boolean check(final String source) {
				return source.length() > 4;
			}
		});

		JEnumerableAssert.assertEqual(new JEnumerable<String>(new String[] {}),
				result);
	}

	@Test
	public void predicateWithIndexFailingFirstElement() {
		JEnumerable<String> source = new JEnumerable<String>(new String[] {
				"zero", "one", "two", "three", "four", "five", "six" });

		JEnumerable<String> result = source
				.takeWhile(new IndexPredicate<String>() {
					@Override
					public boolean check(final String source,
							final Integer index) {
						return index + source.length() > 4;
					}
				});

		JEnumerableAssert.assertEqual(new JEnumerable<String>(new String[] {}),
				result);
	}

	@Test
	public void predicateMatchingSomeElements() {
		JEnumerable<String> source = new JEnumerable<String>(new String[] {
				"zero", "one", "two", "three", "four", "five", "six" });

		JEnumerable<String> result = source.takeWhile(new Predicate<String>() {
			@Override
			public boolean check(final String source) {
				return source.length() < 5;
			}
		});

		JEnumerableAssert.assertEqual(new JEnumerable<String>(new String[] {
				"zero", "one", "two" }), result);
	}

	@Test
	public void predicateWithIndexMatchingSomeElements() {
		JEnumerable<String> source = new JEnumerable<String>(new String[] {
				"zero", "one", "two", "three", "four", "five", "six" });

		JEnumerable<String> result = source
				.takeWhile(new IndexPredicate<String>() {
					@Override
					public boolean check(final String source,
							final Integer index) {
						return source.length() > index;
					}
				});

		JEnumerableAssert.assertEqual(new JEnumerable<String>(new String[] {
				"zero", "one", "two", "three" }), result);
	}

	@Test
	public void predicateMatchingAllElements() {
		JEnumerable<String> source = new JEnumerable<String>(new String[] {
				"zero", "one", "two", "three", "four", "five", "six" });

		JEnumerable<String> result = source.takeWhile(new Predicate<String>() {
			@Override
			public boolean check(final String source) {
				return source.length() < 100;
			}
		});

		JEnumerableAssert
				.assertEqual(new JEnumerable<String>(new String[] { "zero",
						"one", "two", "three", "four", "five", "six" }), result);
	}

	@Test
	public void predicateWithIndexMatchingAllElements() {
		JEnumerable<String> source = new JEnumerable<String>(new String[] {
				"zero", "one", "two", "three", "four", "five", "six" });

		JEnumerable<String> result = source
				.takeWhile(new IndexPredicate<String>() {
					@Override
					public boolean check(final String source,
							final Integer index) {
						return source.length() + index < 100;
					}
				});

		JEnumerableAssert
				.assertEqual(new JEnumerable<String>(new String[] { "zero",
						"one", "two", "three", "four", "five", "six" }), result);
	}
}
