package com.dolbz.jenumerable;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.dolbz.jenumerable.altlambda.Predicate;

public class CountTest extends JEnumerableTestBase {

	@Test
	public void testCountWithoutPredicateOptimized() {
		JEnumerable<String> collection = new JEnumerable<String>(
				Arrays.asList(new String[] { "Item 1", "Item 2", "Item 3" }));

		Assert.assertEquals(3, collection.count());
	}

	@Test
	public void testCountWithoutPredicateNotOptimized() {
		JEnumerable<Integer> collection = JEnumerable.range(10, 6);

		Assert.assertEquals(6, collection.count());
	}

	@Test
	public void testCountWithPredicate() {
		JEnumerable<String> collection = new JEnumerable<String>(
				Arrays.asList(new String[] { "Item 1", "Item 2", "Item 3" }));

		Assert.assertEquals(1, collection.count(new Predicate<String>() {

			@Override
			public boolean check(final String source) {
				return source.equals("Item 2");
			}
		}));
	}

	// TODO longCount stuff
}
