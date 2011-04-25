package com.dolbz.jenumerable.util;

/**
 * Clone of the .NET interface IEqualityComparer<T>. Some of the IEnumerable
 * methods require this in order to complete their work. Works with @ComparableHashSet
 * to allow a hash set with a user defined comparison.
 * 
 * @author nrandle
 * 
 * @param <T>
 */
public interface EqualityComparer<T> {

	boolean equals(T x, T y);

	int getHashCode(T obj);
}
