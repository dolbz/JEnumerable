package com.dolbz.jenumerable;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.dolbz.jenumerable.altlambda.Predicate;

public class LastTest extends JEnumerableTestBase {
	@Test
	public void nullPredicateThrowsIllegalArgumentException() {
		Integer[] source = { 1, 3, 7, 9, 10 };
		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(
				Arrays.asList(source));

		exception.expect(IllegalArgumentException.class);
		sourceWrap.last((Predicate<Integer>) null);
	}

	@Test
	public void emptySequenceWithoutPredicateThrowsIllegalStateException() {
		Integer[] source = {};
		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(
				Arrays.asList(source));

		exception.expect(IllegalStateException.class);
		sourceWrap.last();
	}

	@Test
	public void emptySequenceWithPredicateThrowsIllegalStateException() {
		Integer[] source = {};
		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(source);

		exception.expect(IllegalStateException.class);
		sourceWrap.last(new Predicate<Integer>() {

			@Override
			public boolean check(final Integer source) {
				throw new UnsupportedOperationException(
						"This code shouldn't be executed in the test");
			}
		});
	}

	@Test
	public void singleElementSequenceWithoutPredicateReturnsElement() {
		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(
				new Integer[] { 7 });

		Integer last = sourceWrap.last();

		Assert.assertEquals((Integer) 7, last);
	}

	@Test
	public void singleElementSequenceWithMatchingPredicateReturnsElement() {
		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(
				new Integer[] { 7 });

		Integer firstItem = sourceWrap.last(new Predicate<Integer>() {
			@Override
			public boolean check(final Integer source) {
				return source == 7;
			}

		});

		Assert.assertEquals((Integer) 7, firstItem);
	}

	@Test
	public void singleElementSequenceWithNonMatchingPredicateThrowsIllegalStateException() {
		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(
				new Integer[] { 7 });

		exception.expect(IllegalStateException.class);
		sourceWrap.last(new Predicate<Integer>() {
			@Override
			public boolean check(final Integer source) {
				return false;
			}

		});
	}

	@Test
	public void multipleElementSequenceNoPredicateReturnsLastElement() {
		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(
				new Integer[] { 21, 14, 7 });
		Assert.assertEquals((Integer) 7, sourceWrap.last());
	}

	@Test
	public void multipleElementSequenceNonMatchingPredicate() {
		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(
				new Integer[] { 21, 14, 7 });

		exception.expect(IllegalStateException.class);
		sourceWrap.last(new Predicate<Integer>() {
			@Override
			public boolean check(final Integer source) {
				return false;
			}
		});
	}

	@Test
	public void multipleElementSequenceSingleMatchingPredicate() {
		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(
				new Integer[] { 21, 14, 7 });

		Assert.assertEquals((Integer) 14,
				sourceWrap.last(new Predicate<Integer>() {
					@Override
					public boolean check(final Integer source) {
						return source == 14;
					}
				}));
	}

	@Test
	public void multipleElementSequenceMultipleMatchingPredicate() {
		JEnumerable<Integer> sourceWrap = new JEnumerable<Integer>(
				new Integer[] { 90, 14, 7, 15, 17 });

		Assert.assertEquals((Integer) 17,
				sourceWrap.last(new Predicate<Integer>() {
					@Override
					public boolean check(final Integer source) {
						return source > 14;
					}
				}));
	}
}
