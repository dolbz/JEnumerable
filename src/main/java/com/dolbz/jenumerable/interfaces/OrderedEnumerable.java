package com.dolbz.jenumerable.interfaces;

import java.util.Comparator;

import com.dolbz.jenumerable.altlambda.Translator;

public interface OrderedEnumerable<TElement> {
	<TKey> OrderedEnumerable<TElement> createOrderedEnumerable(
			Translator<TElement, TKey> keySelector, Comparator<TKey> comparer,
			boolean descending);
}
