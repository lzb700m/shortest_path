package pq;

/**
 * Subclass for BinaryHeap that keep tracking of index of element in the heap
 * 
 * @author Peng Li
 * @author Nan Zhang
 */
// Ver 1.0:  Wed, Feb 3.  Initial description.
// Ver 1.1:  Thu, Feb 11.  Simplified Index interface

import java.util.Comparator;

public class IndexedHeap<T extends Index> extends BinaryHeap<T> {

	/** Build a priority queue with a given array q */
	public IndexedHeap(T[] q, Comparator<T> comp) {
		super(q, comp);
		/*
		 * assign() function may not be called on all element in the pq, run
		 * putIndex() on all element after heap order is established to ensure
		 * correctness.
		 */
		for (int i = 1; i < q.length; i++) {
			q[i].putIndex(i);
		}
	}

	/** Create an empty priority queue of given maximum size */
	public IndexedHeap(int n, Comparator<T> comp) {
		super(n, comp);
	}

	/** restore heap order property after the priority of x has decreased */
	public void decreaseKey(T x) {
		percolateUp(x.getIndex());
	}

	@Override
	public void assign(int i, T x) {
		super.assign(i, x);
		/*
		 * NOTE: if line 31 is replaced with pq[i] = x, a ClassCastException
		 * will be thrown. Java complained something like
		 * "could not cast Object to Index"
		 */
		x.putIndex(i);
	}
}
