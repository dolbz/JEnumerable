package com.dolbz.jenumerable;

public abstract class Translator<TSource, TResult> {
	public abstract TResult translate(TSource source);
}
