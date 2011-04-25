package com.dolbz.jenumerable;

import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;

import com.dolbz.jenumerable.helpers.JEnumerableAssert;
import com.dolbz.jenumerable.helpers.ThrowingIterable;
import com.dolbz.jenumerable.util.DefaultEqualityComparer;
import com.dolbz.jenumerable.util.EqualityComparer;

public class UnionTest extends JEnumerableTestBase {

	@Test
	public void nullSecondWithoutComparer() {
		JEnumerable<String> first = new JEnumerable<String>(new String[] {});
		JEnumerable<String> second = null;

		exception.expect(IllegalArgumentException.class);
		first.union(second);
	}

	@Test
	public void nullSecondWithComparer() {
		JEnumerable<String> first = new JEnumerable<String>(new String[] {});
		JEnumerable<String> second = null;

		exception.expect(IllegalArgumentException.class);
		first.union(second, new DefaultEqualityComparer<String>());
	}

	@Test
	public void unionWithoutComparer() {
		JEnumerable<String> first = new JEnumerable<String>(new String[] { "a",
				"b", "B", "c", "b" });
		JEnumerable<String> second = new JEnumerable<String>(new String[] {
				"d", "e", "d", "a" });

		JEnumerable<String> expected = new JEnumerable<String>(new String[] {
				"a", "b", "B", "c", "d", "e" });
		JEnumerableAssert.assertEqual(expected, first.union(second));
	}

	@Test
	public void unionWithNullComparer() {
		JEnumerable<String> first = new JEnumerable<String>(new String[] { "a",
				"b", "B", "c", "b" });
		JEnumerable<String> second = new JEnumerable<String>(new String[] {
				"d", "e", "d", "a" });

		JEnumerable<String> expected = new JEnumerable<String>(new String[] {
				"a", "b", "B", "c", "d", "e" });
		JEnumerableAssert.assertEqual(expected, first.union(second, null));
	}

	@Test
	public void unionWithCaseInsensitiveComparer() {
		JEnumerable<String> first = new JEnumerable<String>(new String[] { "a",
				"b", "B", "c", "b" });
		JEnumerable<String> second = new JEnumerable<String>(new String[] {
				"d", "e", "d", "a" });

		JEnumerable<String> expected = new JEnumerable<String>(new String[] {
				"a", "b", "c", "d", "e" });
		JEnumerableAssert.assertEqual(expected,
				first.union(second, new EqualityComparer<String>() {

					public int getHashCode(final String obj) {
						return obj.toLowerCase().hashCode();
					}

					public boolean equals(final String x, final String y) {
						return x.toLowerCase().equals(y.toLowerCase());
					}
				}));
	}

	@Test
	public void unionWithEmptyFirstSequence() {
		JEnumerable<String> first = new JEnumerable<String>(new String[] {});
		JEnumerable<String> second = new JEnumerable<String>(new String[] {
				"d", "e", "d", "a" });

		JEnumerable<String> expected = new JEnumerable<String>(new String[] {
				"d", "e", "a" });
		JEnumerableAssert.assertEqual(expected, first.union(second));
	}

	@Test
	public void unionWithEmptySecondSequence() {
		JEnumerable<String> first = new JEnumerable<String>(new String[] { "a",
				"b", "B", "c", "b" });
		JEnumerable<String> second = new JEnumerable<String>(new String[] {});

		JEnumerable<String> expected = new JEnumerable<String>(new String[] {
				"a", "b", "B", "c" });
		JEnumerableAssert.assertEqual(expected, first.union(second));
	}

	@Test
	public void unionWithTwoEmptySequences() {
		JEnumerable<String> first = new JEnumerable<String>(new String[] {});
		JEnumerable<String> second = new JEnumerable<String>(new String[] {});

		JEnumerable<String> expected = new JEnumerable<String>(new String[] {});
		JEnumerableAssert.assertEqual(expected, first.union(second));
	}

	@Test
	public void firstSequenceIsNotUsedUntilQueryIsIterated() {
		JEnumerable<Integer> first = new JEnumerable<Integer>(
				new ThrowingIterable<Integer>());
		JEnumerable<Integer> second = new JEnumerable<Integer>(
				new Integer[] { 2 });

		JEnumerable<Integer> result = first.union(second);
		Iterator<Integer> iterator = result.iterator();
		exception.expect(UnsupportedOperationException.class);
		iterator.next();
	}

	@Test
	public void secondSequenceIsnotUsedUntilFirstIsExhausted() {
		JEnumerable<Integer> first = new JEnumerable<Integer>(new Integer[] {
				3, 5, 3 });
		JEnumerable<Integer> second = new JEnumerable<Integer>(
				new ThrowingIterable<Integer>());

		Iterator<Integer> iterator = first.union(second).iterator();

		Assert.assertEquals((Integer) 3, iterator.next());
		Assert.assertEquals((Integer) 5, iterator.next());

		exception.expect(UnsupportedOperationException.class);
		iterator.next();
	}
}
