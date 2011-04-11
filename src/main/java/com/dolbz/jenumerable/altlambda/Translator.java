package com.dolbz.jenumerable.altlambda;

public abstract class Translator<TSource, TResult> {
	public abstract TResult translate(TSource source);
}
