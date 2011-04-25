package com.dolbz.jenumerable;

import junit.framework.Assert;

import org.junit.Test;

import com.dolbz.jenumerable.helpers.JEnumerableAssert;
import com.dolbz.jenumerable.util.DefaultEqualityComparer;
import com.dolbz.jenumerable.util.EqualityComparer;

public class DistinctTest extends JEnumerableTestBase {

	private static String testString1 = "test";
	private static String testString2 = new String(testString1.toCharArray());

	@Test
	public void NullElementsArePassedToComparer() {
		EqualityComparer<String> comparer = new EqualityComparer<String>() {

			public boolean equals(final String x, final String y) {
				return x.equals(y);
			}

			public int getHashCode(final String obj) {
				return obj.hashCode();
			}

		};

		boolean seenException = false;
		try {
			comparer.getHashCode(null);
		} catch (NullPointerException e) {
			seenException = true;
		} finally {
			if (!seenException) {
				Assert.fail();
			}
		}

		seenException = false;
		try {
			comparer.equals(null, "xyz");
		} catch (NullPointerException e) {
			seenException = true;
		} finally {
			if (!seenException) {
				Assert.fail();
			}
		}

		String[] source = new String[] { "xyz", null, "xyz", null, "abc" };
		JEnumerable<String> wrappedSource = new JEnumerable<String>(source);

		seenException = false;
		try {
			wrappedSource.distinct(comparer).count();
		} catch (NullPointerException e) {
			seenException = true;
		} finally {
			if (!seenException) {
				Assert.fail();
			}
		}
	}

	@Test
	public void HashsetCopesWithNullElementsIfComparerDoes() {
		EqualityComparer<String> comparer = new DefaultEqualityComparer<String>();

		Assert.assertEquals(comparer.getHashCode(null),
				comparer.getHashCode(null));
		Assert.assertTrue(comparer.equals(null, null));

		JEnumerable<String> source = new JEnumerable<String>(new String[] {
				"xyz", null, "xyz", null, "abc" });

		JEnumerable<String> expected = new JEnumerable<String>(new String[] {
				"xyz", null, "abc" });
		JEnumerableAssert.assertEqual(expected, source.distinct(comparer));
	}

	@Test
	public void NoComparerSpecifiedUsesDefault() {
		JEnumerable<String> source = new JEnumerable<String>(new String[] {
				"xyz", testString1, "XYZ", testString2, "def" });

		JEnumerable<String> result = source.distinct();

		JEnumerable<String> expected = new JEnumerable<String>(new String[] {
				"xyz", testString1, "XYZ", "def" });
		JEnumerableAssert.assertEqual(expected, result);
	}

	@Test
	public void NullComparerUsesDefault() {
		JEnumerable<String> source = new JEnumerable<String>(new String[] {
				"xyz", testString1, "XYZ", testString2, "def" });

		JEnumerable<String> result = source.distinct(null);

		JEnumerable<String> expected = new JEnumerable<String>(new String[] {
				"xyz", testString1, "XYZ", "def" });
		JEnumerableAssert.assertEqual(expected, result);
	}

	@Test
	public void DistinctStringsWithCaseInsensitiveComparer() {
		JEnumerable<String> source = new JEnumerable<String>(new String[] {
				"xyz", testString1, "XYZ", testString2, "def" });

		JEnumerable<String> result = source
				.distinct(new EqualityComparer<String>() {

					public int getHashCode(final String obj) {
						return obj.toLowerCase().hashCode();
					}

					public boolean equals(final String x, final String y) {
						return x.toLowerCase().equals(y.toLowerCase());
					}
				});

		JEnumerable<String> expected = new JEnumerable<String>(new String[] {
				"xyz", testString1, "def" });
		JEnumerableAssert.assertEqual(expected, result);
	}

	@Test
	public void DistinctStringsCustomComparer() {
		JEnumerable<String> source = new JEnumerable<String>(new String[] {
				"xyz", testString1, "XYZ", testString2, testString1 });

		JEnumerable<String> result = source
				.distinct(new EqualityComparer<String>() {

					public int getHashCode(final String obj) {
						return System.identityHashCode(obj);
					}

					public boolean equals(final String x, final String y) {
						return x == y;
					}
				});

		JEnumerable<String> expected = new JEnumerable<String>(new String[] {
				"xyz", testString1, "XYZ", testString2 });
		JEnumerableAssert.assertEqual(expected, result);
	}
}
