package com.dolbz.jenumerable.interfaces;

import com.dolbz.jenumerable.JEnumerable;

public interface Lookup<TKey, TElement> {
	int getCount();

	JEnumerable<TElement> get(TKey key);

	boolean contains(TKey key);
}
