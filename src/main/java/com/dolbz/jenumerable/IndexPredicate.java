package com.dolbz.jenumerable;

public abstract class IndexPredicate<TSource> {
	protected abstract boolean check(TSource source, Integer index);
}
