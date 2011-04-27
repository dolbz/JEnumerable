package com.dolbz.jenumerable.altlambda;

public abstract class DualTranslator<TPrimeSource, TSecondSource, TResult> {
	public abstract TResult translate(TPrimeSource primarySource,
			TSecondSource secondSource);
}
