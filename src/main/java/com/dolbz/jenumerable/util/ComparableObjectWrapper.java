package com.dolbz.jenumerable.util;

/**
 * Wrapper class for any object to support {@link EqualityComparer} when it
 * wouldn't normally be possible. e.g. in a HashSet
 * 
 * @author nrandle
 * 
 */
class ComparableObjectWrapper<T> {

	private final T wrappedObj;
	private final EqualityComparer<T> comparer;

	public ComparableObjectWrapper(final T source,
			final EqualityComparer<T> comparer) {
		this.wrappedObj = source;
		this.comparer = comparer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final Object obj) {
		return comparer.equals(wrappedObj,
				((ComparableObjectWrapper<T>) obj).wrappedObj);
	}

	@Override
	public int hashCode() {
		return comparer.getHashCode(wrappedObj);
	}

}
