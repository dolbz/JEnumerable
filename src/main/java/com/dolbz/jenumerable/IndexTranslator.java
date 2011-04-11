package com.dolbz.jenumerable;

public abstract class IndexTranslator<TSource, TResult> {
	public abstract TResult translate(TSource source, Integer index);
}
