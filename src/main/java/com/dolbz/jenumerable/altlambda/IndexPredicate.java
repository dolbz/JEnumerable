package com.dolbz.jenumerable.altlambda;

public abstract class IndexPredicate<TSource> {
	public abstract boolean check(TSource source, Integer index);
}
