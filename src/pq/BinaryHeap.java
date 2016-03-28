package pq;

/**
 * Implementation of binary heap class (implements priority queue interface)
 * @author Peng Li
 * @author Nan Zhang
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class BinaryHeap<T> implements PQ<T> {

	private static final double RESIZE_FACTOR = 2;

	T[] pq; // internal array to store objects, pq[0] is not used
	Comparator<T> c; // passed c decides heap order (min or max)
	int size; // actual number of element in the heap (size <=
				// pq.length - 1)

	/**
	 * Build a priority queue with a given array q
	 * 
	 * @param q
	 *            initial array of elements to be stored in the heap
	 * 
	 * @param comp
	 *            comparator object to decide heap order (minHeap or maxHeap)
	 */
	public BinaryHeap(T[] q, Comparator<T> comp) {
		size = q.length - 1;
		pq = q;
		c = comp;
		buildHeap();
	}

	/**
	 * Create an empty priority queue of given maximum capacity (not actual
	 * size), the capacity can be extended later during insert() operation
	 * 
	 * @param n
	 *            capacity of the heap without resizing
	 * @param comp
	 *            comparator object to decide heap order (minHeap or maxHeap)
	 */
	@SuppressWarnings("unchecked")
	public BinaryHeap(int n, Comparator<T> comp) {
		pq = (T[]) new Object[n + 1]; // pq[0] not reserved for actual elements
		c = comp;
		size = 0;
	}

	/** wrapper function, equivalent to add(T x) method */
	public void insert(T x) {
		add(x);
	}

	/** wrapper function, equivalent to remove() method */
	public T deleteMin() {
		return remove();
	}

	/** wrapper function, equivalent to peek() method */
	public T min() {
		return peek();
	}

	/**
	 * Insert an element into binary heap while keeping heap property
	 * 
	 * @param x
	 *            element to be inserted
	 */
	public void add(T x) {
		if (size == pq.length - 1) {
			resize(); // in case pq is full
		}
		assign(++size, x); // keep heap structural property
		percolateUp(size); // recover heap order property
	}

	/**
	 * Remove the minimum element from binary heap and return it
	 */
	public T remove() {
		if (size == 0) {
			return null; // heap is already empty
		}
		T min = pq[1];
		assign(1, pq[size--]);// keep heap structural property
		percolateDown(1); // recover heap order property
		return min;
	}

	/**
	 * Return the minimum element from binary heap without deleting it
	 */
	public T peek() {
		if (size == 0) {
			return null; // heap is already empty
		}
		return pq[1];
	}

	/**
	 * Return true is heap is empty
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * toString representation of actual element in the heap, as ordered in the
	 * pq array
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= size; i++) {
			sb.append(pq[i]);
			sb.append(", ");
		}
		sb.replace(sb.length() - 2, sb.length() - 1, ";");
		return sb.toString();
	}

	/**
	 * pq[i] may violate heap order with parent, re-order element with its
	 * parents to keep heap order property
	 * 
	 * @param i
	 *            index of element as in pq[] array that violates heap order
	 *            property with parent
	 */
	protected void percolateUp(int i) {
		assign(0, pq[i]); // for edge case: pq[i] beats every element in the
							// heap
		while (c.compare(pq[i / 2], pq[0]) > 0) {
			assign(i, pq[i / 2]); // percolate the hole up to its parent
			i /= 2;
		}
		assign(i, pq[0]);
	}

	/**
	 * pq[i] may violate heap order with children, re-order element with its
	 * children to keep heap order property
	 * 
	 * @param i
	 *            index of element as in pq[] array that violates heap order
	 *            property with children
	 */
	protected void percolateDown(int i) {
		int child;
		T temp = pq[i]; // for later retrieval
		while (i * 2 <= size) { // still have children
			child = i * 2; // the left child

			if (child < size && c.compare(pq[child], pq[child + 1]) > 0) {
				// has right child and pq[right] < pq[left]
				child++; // move to right child
			}

			if (c.compare(temp, pq[child]) > 0) {
				assign(i, pq[child]); // percolate down
				i = child;
			} else {
				break;
			}
		}
		assign(i, temp);
	}

	/**
	 * Create a heap. Precondition: none. Build heap order from bottom up,
	 * starting from the first none leaf node in the heap. RT = O(n)
	 */
	protected void buildHeap() {
		for (int i = size / 2; i > 0; i--) {
			// from the first none leaf node
			percolateDown(i);
		}
	}

	/**
	 * resize internal array pq when it's full
	 */
	@SuppressWarnings("unchecked")
	protected void resize() {
		// add 1 to original length to avoid zero multiplication
		Object[] newArray = new Object[(int) ((pq.length + 1) * RESIZE_FACTOR)];
		for (int i = 0; i < pq.length; i++) {
			newArray[i] = pq[i];
		}
		pq = (T[]) newArray;
	}

	/**
	 * Assign an element value to the specific index location in the internal
	 * array pq[]. This method is to be overridden in the IndexedHeap subclass
	 * 
	 * @param i
	 *            index of pq[] to be updated
	 * @param x
	 *            value of element to be assigned
	 */
	public void assign(int i, T x) {
		pq[i] = x;
	}

	/**
	 * sort array A[1..n]. A[0] is not used. Sorted order depends on comparator
	 * used to buid heap. min heap ==> descending order max heap ==> ascending
	 * order
	 */
	public static <T> void heapSort(T[] A, Comparator<T> comp) {

		BinaryHeap<T> heap = new BinaryHeap<T>(A, comp);
		T temp;

		/*
		 * Loop invariant: i is at the last element of the heap, everything in
		 * pq[] after i is sorted and are not part of the heap
		 */
		for (int i = heap.size; i > 0; i--) {
			// exchange pq[1] and pq[i]
			temp = heap.pq[1];
			heap.assign(1, heap.pq[i]);
			heap.assign(i, temp);

			heap.size--;
			heap.percolateDown(1);
		}
	}

	/**
	 * Driver function for testing
	 * 
	 * @param args
	 *            argument - input file of list of integers
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;

		if (args.length > 0) {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}

		List<Integer> list = new ArrayList<Integer>();
		list.add(0, 0); // add an dummy value at start
		while (in.hasNextInt()) {
			list.add(in.nextInt());
		}
		in.close();

		Integer[] array = list.toArray(new Integer[list.size()]);

		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		};

		Long start = System.currentTimeMillis();
		heapSort(array, comparator);
		Long end = System.currentTimeMillis();

		System.out.println("Heap sort running time for " + (array.length - 1)
				+ " elements: " + (end - start) + " ms.");
	}

}
