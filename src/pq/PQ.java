package pq;

/**
 * Priority Queue interface definition
 * 
 * @author Peng Li
 * @author Nan Zhang
 * 
 * @param <T>
 *            object to be stored in Priority Queue
 */

public interface PQ<T> {
	public void insert(T x);

	public T deleteMin();

	public T min();

	public void add(T x); // equivalent to insert(T x)

	public T remove(); // equivalent to deleteMin()

	public T peek(); // equivalent to min()

	public boolean isEmpty(); // return true if PQ is empty, false otherwise
}
