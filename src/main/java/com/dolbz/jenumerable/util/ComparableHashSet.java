package com.dolbz.jenumerable.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ComparableHashSet<E> implements Serializable, Cloneable,
		Iterable<E>, Collection<E>, Set<E> { // extends
												// HashSet<ComparableObjectWrapper<E>>
												// {

	private static final long serialVersionUID = 1L;
	private final EqualityComparer<E> comparer;
	private final HashSet<ComparableObjectWrapper<E>> internalHashSet;

	public ComparableHashSet(final EqualityComparer<E> comparer) {
		this.comparer = comparer;
		this.internalHashSet = new HashSet<ComparableObjectWrapper<E>>();
	}

	public ComparableHashSet(final Iterable<? extends E> arg0,
			final EqualityComparer<E> comparer) {
		this.comparer = comparer;

		Collection<ComparableObjectWrapper<E>> collectionOfCorrectType = new ArrayList<ComparableObjectWrapper<E>>();

		for (E item : arg0) {
			collectionOfCorrectType.add(new ComparableObjectWrapper<E>(item,
					comparer));
		}

		this.internalHashSet = new HashSet<ComparableObjectWrapper<E>>(
				collectionOfCorrectType);
	}

	public ComparableHashSet(final int arg0, final float arg1,
			final EqualityComparer<E> comparer) {
		this.comparer = comparer;
		this.internalHashSet = new HashSet<ComparableObjectWrapper<E>>(arg0,
				arg1);
	}

	public ComparableHashSet(final int arg0, final EqualityComparer<E> comparer) {
		this.comparer = comparer;
		this.internalHashSet = new HashSet<ComparableObjectWrapper<E>>(arg0);
	}

	public boolean add(final E e) {
		return internalHashSet.add(new ComparableObjectWrapper<E>(e, comparer));
	}

	public boolean addAll(final Collection<? extends E> c) {
		Collection<ComparableObjectWrapper<E>> collectionOfCorrectType = new ArrayList<ComparableObjectWrapper<E>>();

		for (E item : c) {
			collectionOfCorrectType.add(new ComparableObjectWrapper<E>(item,
					comparer));
		}

		return internalHashSet.addAll(collectionOfCorrectType);
	}

	public void clear() {
		internalHashSet.clear();
	}

	public boolean contains(final Object o) {
		return internalHashSet.contains(o);
	}

	public boolean containsAll(final Collection<?> c) {
		return internalHashSet.containsAll(c);
	}

	public boolean isEmpty() {
		return internalHashSet.isEmpty();
	}

	public boolean remove(final Object o) {
		return internalHashSet.remove(new ComparableObjectWrapper<E>((E) o,
				comparer));
	}

	public boolean removeAll(final Collection<?> c) {
		return internalHashSet.removeAll(c);
	}

	public boolean retainAll(final Collection<?> c) {
		return internalHashSet.retainAll(c);
	}

	public int size() {
		return internalHashSet.size();
	}

	public Object[] toArray() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public <T> T[] toArray(final T[] a) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	};
}
