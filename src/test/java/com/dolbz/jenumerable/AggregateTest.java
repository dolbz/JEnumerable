package com.dolbz.jenumerable;

import junit.framework.Assert;

import org.junit.Test;

import com.dolbz.jenumerable.altlambda.DualTranslator;
import com.dolbz.jenumerable.altlambda.Translator;

public class AggregateTest extends JEnumerableTestBase {

	@Test
	public void nullFuncUnseeded() {
		JEnumerable<Integer> source = new JEnumerable<Integer>(new Integer[] {
				1, 3 });

		exception.expect(IllegalArgumentException.class);
		source.aggregate(null);
	}

	@Test
	public void unseededAggregation() {
		JEnumerable<Integer> source = new JEnumerable<Integer>(new Integer[] {
				1, 4, 5 });

		Assert.assertEquals((Integer) 17, source
				.aggregate(new DualTranslator<Integer, Integer, Integer>() {

					@Override
					public Integer translate(final Integer primarySource,
							final Integer secondSource) {
						return primarySource * 2 + secondSource;
					}
				}));
	}

	@Test
	public void nullFuncSeeded() {
		JEnumerable<Integer> source = new JEnumerable<Integer>(new Integer[] {
				1, 3 });
		exception.expect(IllegalArgumentException.class);
		source.aggregate(5, null);
	}

	@Test
	public void seededAggregation() {
		JEnumerable<Integer> source = new JEnumerable<Integer>(new Integer[] {
				1, 4, 5 });

		Assert.assertEquals((Integer) 57, source.aggregate(5,
				new DualTranslator<Integer, Integer, Integer>() {

					@Override
					public Integer translate(final Integer primarySource,
							final Integer secondSource) {
						return primarySource * 2 + secondSource;
					}
				}));
	}

	@Test
	public void nullFuncSeededWithResultSelector() {
		JEnumerable<Integer> source = new JEnumerable<Integer>(new Integer[] {
				1, 3 });
		exception.expect(IllegalArgumentException.class);
		source.aggregate(5, null, new Translator<Integer, String>() {

			@Override
			public String translate(final Integer source) {
				return source.toString();
			}
		});
	}

	@Test
	public void nullProjectionSeededWithResultSelector() {
		JEnumerable<Integer> source = new JEnumerable<Integer>(new Integer[] {
				1, 3 });
		exception.expect(IllegalArgumentException.class);
		source.aggregate(5, new DualTranslator<Integer, Integer, Integer>() {

			@Override
			public Integer translate(final Integer primarySource,
					final Integer secondSource) {
				return primarySource + secondSource;
			}
		}, null);
	}

	@Test
	public void seededAggregationWithResultSelector() {
		JEnumerable<Integer> source = new JEnumerable<Integer>(new Integer[] {
				1, 4, 5 });

		Assert.assertEquals("57", source.aggregate(5,
				new DualTranslator<Integer, Integer, Integer>() {

					@Override
					public Integer translate(final Integer primarySource,
							final Integer secondSource) {
						return primarySource * 2 + secondSource;
					}
				}, new Translator<Integer, String>() {

					@Override
					public String translate(final Integer source) {
						return source.toString();
					}
				}));
	}

	@Test
	public void differentSourceAndAccumulatorTypes() {
		int largeValue = 2000000000;
		JEnumerable<Integer> source = new JEnumerable<Integer>(new Integer[] {
				largeValue, largeValue, largeValue });
		long sum = source.aggregate(0L,
				new DualTranslator<Long, Integer, Long>() {

					@Override
					public Long translate(final Long primarySource,
							final Integer secondSource) {
						return primarySource + secondSource;
					}
				});
		Assert.assertEquals(6000000000L, sum);
		// Just to prove we haven't missed off a zero...
		Assert.assertTrue(sum > Integer.MAX_VALUE);
	}

	@Test
	public void emptySequenceUnseeded() {
		JEnumerable<Integer> source = new JEnumerable<Integer>(new Integer[] {});

		exception.expect(IllegalStateException.class);
		source.aggregate(new DualTranslator<Integer, Integer, Integer>() {

			@Override
			public Integer translate(final Integer primarySource,
					final Integer secondSource) {
				return primarySource + secondSource;
			}
		});
	}

	@Test
	public void emptySequenceSeeded() {
		JEnumerable<Integer> source = new JEnumerable<Integer>(new Integer[] {});

		Assert.assertEquals((Integer) 5, source.aggregate(5,
				new DualTranslator<Integer, Integer, Integer>() {

					@Override
					public Integer translate(final Integer primarySource,
							final Integer secondSource) {
						return primarySource + secondSource;
					}
				}));
	}

	@Test
	public void emptySequenceSeededWithResultSelector() {
		JEnumerable<Integer> source = new JEnumerable<Integer>(new Integer[] {});

		Assert.assertEquals("5", source.aggregate(5,
				new DualTranslator<Integer, Integer, Integer>() {

					@Override
					public Integer translate(final Integer primarySource,
							final Integer secondSource) {
						return primarySource + secondSource;
					}
				}, new Translator<Integer, String>() {

					@Override
					public String translate(final Integer source) {
						return source.toString();
					}

				}));
	}

	@Test
	public void firstElementOfInputIsUsedAsSeedForUnseededOverload() {
		JEnumerable<Integer> source = new JEnumerable<Integer>(new Integer[] {
				5, 3, 2 });

		Assert.assertEquals((Integer) 30, source
				.aggregate(new DualTranslator<Integer, Integer, Integer>() {

					@Override
					public Integer translate(final Integer primarySource,
							final Integer secondSource) {
						return primarySource * secondSource;
					}
				}));
	}
}
