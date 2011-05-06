package com.dolbz.jenumerable.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.AssertionFailedError;

import com.dolbz.jenumerable.JEnumerable;

public class JEnumerableAssert {

	public static void assertEqual(final JEnumerable<?> expected,
			final JEnumerable<?> actual) {
		Iterator<?> expectedIter = expected.iterator();
		Iterator<?> actualIter = actual.iterator();

		List<Object> actualSeen = new ArrayList<Object>();

		while (expectedIter.hasNext()) {
			if (actualIter.hasNext()) {
				Object expectedNext = expectedIter.next();
				Object actualNext = actualIter.next();
				actualSeen.add(actualNext);
				if (expectedNext == null && actualNext == null) {
					// Both null is fine
				} else if (expectedNext == null || actualNext == null) {
					throw new AssertionFailedError(buildActuallySeenString(
							actualSeen, "Elements are not equal: "));
				} else if (!expectedNext.equals(actualNext)) {
					throw new AssertionFailedError(buildActuallySeenString(
							actualSeen, "Elements are not equal: "));
				}
			} else {
				String message = "Expected has more elements than actual: ";
				message = buildActuallySeenString(actualSeen, message);
				throw new AssertionFailedError(message);
			}
		}

		if (actualIter.hasNext()) {
			throw new AssertionFailedError(
					"Actual has more elements than expected");
		}

	}

	private static String buildActuallySeenString(
			final List<Object> actualSeen, String message) {
		for (Object seen : actualSeen) {
			message += "[" + seen.toString() + "], ";
		}
		return message;
	}
}
