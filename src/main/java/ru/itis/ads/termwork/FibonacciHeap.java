package ru.itis.ads.termwork;

import java.util.ArrayList;

public class FibonacciHeap {

	public static Boolean ENABLE_PROFILING = false;
	public static int ITERATIONS = 0;

	/** Points to the minimum node in the heap. */
	private Node min;
	/** Number of nodes in the heap. If the type is ever widened,
	 * (e.g. changed to long) then recalcuate the maximum degree
	 * value used in the consolidate() method. */
	private int n;

	/**
	 * Removes all elements from this heap.
	 *
	 * <p><em>Running time: O(1)</em></p>
	 */
	public void clear() {
		min = null;
		n = 0;
	}

	/**
	 * Consolidates the trees in the heap by joining trees of equal
	 * degree until there are no more trees of equal degree in the
	 * root list.
	 *
	 * <p><em>Running time: O(log n) amortized</em></p>
	 */
	private void consolidate() {
		// The magic 45 comes from log base phi of Integer.MAX_VALUE,
		// which is the most elements we will ever hold, and log base
		// phi represents the largest degree of any root list node.
		Node[] A = new Node[45];

		// For each root list node look for others of the same degree.
		Node start = min;
		Node w = min;
		do {
			ITERATIONS++;
			Node x = w;
			// Because x might be moved, save its sibling now.
			Node nextW = w.right;
			int d = x.degree;
			while (A[d] != null) {
				ITERATIONS++;
				// Make one of the nodes a child of the other.
				Node y = A[d];
				if (x.key.compareTo(y.key) > 0) {
					Node temp = y;
					y = x;
					x = temp;
				}
				if (y == start) {
					// Because removeMin() arbitrarily assigned the min
					// reference, we have to ensure we do not miss the
					// end of the root node list.
					start = start.right;
				}
				if (y == nextW) {
					// If we wrapped around we need to check for this case.
					nextW = nextW.right;
				}
				// Node y disappears from root list.
				y.link(x);
				// We've handled this degree, go to next one.
				A[d] = null;
				d++;
			}
			// Save this node for later when we might encounter another
			// of the same degree.
			A[d] = x;
			// Move forward through list.
			w = nextW;
		} while (w != start);

		// The node considered to be min may have been changed above.
		min = start;
		// Find the minimum key again.
		for (Node a : A) {
			if (a != null && a.key.compareTo(min.key) < 0) {
				min = a;
				ITERATIONS++;
			}
		}
	}

	/**
	 * Decreases the key value for a heap node, given the new value
	 * to take on. The structure of the heap may be changed, but will
	 * not be consolidated.
	 *
	 * <p><em>Running time: O(1) amortized</em></p>
	 *
	 * @param  x  node to decrease the key of
	 * @param  k  new key value for node x
	 * @exception  IllegalArgumentException
	 *             if k is larger than x.key value.
	 */
	public void decreaseKey(Node x, Object newData, Comparable k) {
		decreaseKey(x, newData, k, false);
	}

	/**
	 * Decrease the key value of a node, or simply bubble it up to the
	 * top of the heap in preparation for a delete operation.
	 *
	 * @param  x       node to decrease the key of.
	 * @param  k       new key value for node x.
	 * @param  delete  true if deleting node (in which case, k is ignored).
	 */
	private void decreaseKey(Node x, Object newData, Comparable k, boolean delete) {
		ITERATIONS++;
		if (!delete && k.compareTo(x.key) > 0) {
			throw new IllegalArgumentException("cannot increase key value");
		}
		x.key = k;
		x.data = newData;
		Node y = x.parent;
		if (y != null && (delete || k.compareTo(y.key) < 0)) {
			y.cut(x, min);
			y.cascadingCut(min);
		}
		if (delete || k.compareTo(min.key) < 0) {
			min = x;
		}
	}

	/**
	 * Deletes a node from the heap given the reference to the node.
	 * The trees in the heap will be consolidated, if necessary.
	 *
	 * <p><em>Running time: O(log n) amortized</em></p>
	 *
	 * @param  x  node to remove from heap.
	 */
	public void delete(Node x) {
		// make x as small as possible
		decreaseKey(x, x.data, 0, true);
		// remove the smallest, which decreases n also
		removeMin();
	}

	/**
	 * Tests if the Fibonacci heap is empty or not. Returns true if
	 * the heap is empty, false otherwise.
	 *
	 * <p><em>Running time: O(1)</em></p>
	 *
	 * @return  true if the heap is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return min == null;
	}

	/**
	 * Inserts a new data element into the heap. No heap consolidation
	 * is performed at this time, the new node is simply inserted into
	 * the root list of this heap.
	 *
	 * <p><em>Running time: O(1)</em></p>
	 *
	 * @param  x    data object to insert into heap.
	 * @param  key  key value associated with data object.
	 * @return newly created heap node.
	 */
	public Node insert(Object x, Comparable key) {
		ITERATIONS++;
		Node node = new Node(x, key);
		// concatenate node into min list
		if (min != null) {
			node.right = min;
			node.left = min.left;
			min.left = node;
			node.left.right = node;
			if (key.compareTo(min.key) < 0) {
				min = node;
			}
		} else {
			min = node;
		}
		n++;
		return node;
	}

	/**
	 * Returns the smallest element in the heap. This smallest element
	 * is the one with the minimum key value.
	 *
	 * <p><em>Running time: O(1)</em></p>
	 *
	 * @return  heap node with the smallest key, or null if empty.
	 */
	public Node min() {
		ITERATIONS++;
		return min;
	}

	/**
	 * Removes the smallest element from the heap. This will cause
	 * the trees in the heap to be consolidated, if necessary.
	 *
	 * <p><em>Running time: O(log n) amortized</em></p>
	 *
	 * @return  data object with the smallest key.
	 */
	public Object removeMin() {
		Node z = min;
		if (z == null) {
			return null;
		}
		if (z.child != null) {
			z.child.parent = null;
			// for each child of z do...
			for (Node x = z.child.right; x != z.child; x = x.right) {
				ITERATIONS++;
				// set parent[x] to null
				x.parent = null;
			}
			// merge the children into root list
			Node minleft = min.left;
			Node zchildleft = z.child.left;
			min.left = zchildleft;
			zchildleft.right = min;
			z.child.left = minleft;
			minleft.right = z.child;
		}
		// remove z from root list of heap
		z.left.right = z.right;
		z.right.left = z.left;
		if (z == z.right) {
			min = null;
		} else {
			min = z.right;
			consolidate();
		}
		// decrement size of heap
		n--;
		return z.data;
	}

	/**
	 * Returns the size of the heap which is measured in the
	 * number of elements contained in the heap.
	 *
	 * <p><em>Running time: O(1)</em></p>
	 *
	 * @return  number of elements in the heap.
	 */
	public int size() {
		return n;
	}

	public int count() {
		return n;
	}

	/**
	 * Joins two Fibonacci heaps into a new one. No heap consolidation is
	 * performed at this time. The two root lists are simply joined together.
	 *
	 * <p><em>Running time: O(1)</em></p>
	 *
	 * @param  H1  first heap
	 * @param  H2  second heap
	 * @return  new heap containing H1 and H2
	 */
	public static FibonacciHeap union(FibonacciHeap H1, FibonacciHeap H2) {
		ITERATIONS++;
		FibonacciHeap H = new FibonacciHeap();
		if (H1 != null && H2 != null) {
			H.min = H1.min;
			if (H.min != null) {
				if (H2.min != null) {
					H.min.right.left = H2.min.left;
					H2.min.left.right = H.min.right;
					H.min.right = H2.min;
					H2.min.left = H.min;
					if (H2.min.key.compareTo(H1.min.key) < 0) {
						H.min = H2.min;
					}
				}
			} else {
				H.min = H2.min;
			}
			H.n = H1.n + H2.n;
		}
		return H;
	}

	public ArrayList<Node> nodeList() {
		ArrayList<Node> l = new ArrayList<Node>();
		if (min != null) min.addToList(l);
		return l;
	}

	/**
	 * Implements a node of the Fibonacci heap. It holds the information
	 * necessary for maintaining the structure of the heap. It acts as
	 * an opaque handle for the data element, and serves as the key to
	 * retrieving the data from the heap.
	 *
	 * @author  Nathan Fiedler
	 */
	public static class Node {
		/** Data object for this node, holds the key value. */
		private Object data;
		/** Key value for this node. */
		private Comparable key;
		/** Parent node. */
		private Node parent;
		/** First child node. */
		private Node child;
		/** Right sibling node. */
		private Node right;
		/** Left sibling node. */
		private Node left;
		/** Number of children of this node. */
		private int degree;
		/** True if this node has had a child removed since this node was
		 * added to its parent. */
		private boolean mark;

		/**
		 * Two-arg constructor which sets the data and key fields to the
		 * passed arguments. It also initializes the right and left pointers,
		 * making this a circular doubly-linked list.
		 *
		 * @param  data  data object to associate with this node
		 * @param  key   key value for this data object
		 */
		public Node(Object data, Comparable key) {
			this.data = data;
			this.key = key;
			right = this;
			left = this;
		}

		public Comparable getKey() {return key;}
		public Object getData() {return data;}

		@Override
		public String toString() {
			return data.toString();
		}

		/**
		 * Performs a cascading cut operation. Cuts this from its parent
		 * and then does the same for its parent, and so on up the tree.
		 *
		 * <p><em>Running time: O(log n)</em></p>
		 *
		 * @param  min  the minimum heap node, to which nodes will be added.
		 */
		public void cascadingCut(Node min) {
			Node z = parent;
			// if there's a parent...
			ITERATIONS++;
			if (z != null) {
				if (mark) {
					// it's marked, cut it from parent
					z.cut(this, min);
					// cut its parent as well
					z.cascadingCut(min);
				} else {
					// if y is unmarked, set it marked
					mark = true;
				}
			}


		}

		/**
		 * The reverse of the link operation: removes x from the child
		 * list of this node.
		 *
		 * <p><em>Running time: O(1)</em></p>
		 *
		 * @param  x    child to be removed from this node's child list
		 * @param  min  the minimum heap node, to which x is added.
		 */
		public void cut(Node x, Node min) {
			ITERATIONS++;
			// remove x from childlist and decrement degree
			x.left.right = x.right;
			x.right.left = x.left;
			degree--;
			// reset child if necessary
			if (degree == 0) {
				child = null;
			} else if (child == x) {
				child = x.right;
			}
			// add x to root list of heap
			x.right = min;
			x.left = min.left;
			min.left = x;
			x.left.right = x;
			// set parent[x] to nil
			x.parent = null;
			// set mark[x] to false
			x.mark = false;
		}

		/**
		 * Make this node a child of the given parent node. All linkages
		 * are updated, the degree of the parent is incremented, and
		 * mark is set to false.
		 *
		 * @param  parent  the new parent node.
		 */
		public void link(Node parent) {
			// Note: putting this code here in Node makes it 7x faster
			// because it doesn't have to use generated accessor methods,
			// which add a lot of time when called millions of times.
			// remove this from its circular list
			left.right = right;
			right.left = left;
			// make this a child of x
			this.parent = parent;
			if (parent.child == null) {
				parent.child = this;
				right = this;
				left = this;
			} else {
				left = parent.child;
				right = parent.child.right;
				parent.child.right = this;
				right.left = this;
			}
			// increase degree[x]
			parent.degree++;
			// set mark false
			mark = false;
			ITERATIONS++;
		}

		public void addToList(ArrayList<Node> l) {
			Node cur = this;
			do {
				l.add(cur);
				if (cur.child != null) cur.child.addToList(l);
				cur = cur.right;
			} while (cur != this);
		}
	}
}