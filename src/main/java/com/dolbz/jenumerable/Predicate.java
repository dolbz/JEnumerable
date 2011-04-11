package com.dolbz.jenumerable;

/**
 * Used this awful name so we can get as close to a .NET style lambda predicate as possible i.e. not very ;)
 * 
 * @author nrandle
 *
 */
public abstract class Predicate<TSource> {
	protected abstract boolean check(TSource source);
}
