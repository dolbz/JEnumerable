package com.dolbz.jenumerable;

import java.util.Iterator;

class RangeIterable implements Iterable<Integer> {

	private final int start;
	private final int count;

	public RangeIterable(final int start, final int count) {
		this.start = start;
		this.count = count;
	}

	public Iterator<Integer> iterator() {
		return new RangeIterator();
	}

	private class RangeIterator implements Iterator<Integer> {

		private int currentCount = 0;

		public boolean hasNext() {
			return currentCount < count;
		}

		public Integer next() {
			return start + currentCount++;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
