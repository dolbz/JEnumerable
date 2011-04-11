package com.dolbz.jenumerable.helpers;

import java.util.Iterator;

import junit.framework.AssertionFailedError;

import com.dolbz.jenumerable.JEnumerable;

public class JEnumerableAssert {

	public static void assertEqual(JEnumerable<?> expected, JEnumerable<?> actual) {
		Iterator<?> expectedIter = expected.iterator();
		Iterator<?> actualIter = actual.iterator();
		
		while(expectedIter.hasNext()) {
			if (actualIter.hasNext()) {
				if (!expectedIter.next().equals(actualIter.next())) {
					throw new AssertionFailedError("Elements not equal");
				}
			} else {
				throw new AssertionFailedError("Expected has more elements that actual");
			}
		}
		
		if (actualIter.hasNext()) {
			throw new AssertionFailedError("Actual has more elements than expected");
		}
		
	}
}
