package HW;

/**
 * Implements a min-heap to store word suggestions along with their importance.
 * The heap is used to efficiently retrieve the top k words with the lowest
 * importance scores.
 */
class WordNode {
	String word;
	int importance;

	/**
	 * Constructor for a WordNode object.
	 *
	 * @param word       the word to store.
	 * @param importance the importance score of the word.
	 */
	public WordNode(String word, int importance) {
		this.word = word;
		this.importance = importance;
	}
}

public class Heap {
	private WordNode[] contents; // Array representing the heap
	private int size; // Current number of elements in the heap
	private int maxsize; // Maximum capacity of the heap

	/**
	 * Constructor to initialize the heap with a specified maximum size.
	 *
	 * @param n the maximum number of elements the heap can hold.
	 */
	public Heap(int n) {
		this.contents = new WordNode[n + 1]; // Index 0 is unused
		this.size = 0;
		this.maxsize = n;
	}

	/**
	 * Checks if the heap is empty.
	 *
	 * @return {@code true} if the heap is empty, {@code false} otherwise.
	 */
	public boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * Checks if the heap is full.
	 *
	 * @return {@code true} if the heap is full, {@code false} otherwise.
	 */
	public boolean isFull() {
		return this.size == this.maxsize;
	}

	/**
	 * Inserts a word with its importance into the heap. If the heap is full and the
	 * importance is greater than the root's importance, the root is replaced.
	 *
	 * @param word       the word to insert.
	 * @param importance the importance score of the word.
	 */
	public void insert(String word, int importance) {
		if (this.size < this.maxsize) {
			// Insert the new word if there is space in the heap
			WordNode newNode = new WordNode(word, importance);
			int index = ++this.size;

			// Percolate up to maintain heap property
			while (index > 1 && this.contents[index / 2].importance > importance) {
				this.contents[index] = this.contents[index / 2];
				index = index / 2;
			}
			this.contents[index] = newNode;
		} else if (importance > this.contents[1].importance) {
			// Replace the root if the new word's importance is higher
			this.contents[1] = new WordNode(word, importance);
			percolateDown(1); // Restore heap property
		}
	}

	/**
	 * Deletes and returns the word with the minimum importance (root of the heap).
	 *
	 * @return the word with the minimum importance, or {@code null} if the heap is
	 *         empty.
	 */
	public String deleteMin() {
		if (isEmpty()) {
			return null;
		}

		WordNode minNode = contents[1]; // Root of the heap
		WordNode lastNode = contents[size]; // Last element in the heap
		size--;

		int index = 1, child;

		// Percolate down to maintain heap property
		while ((index * 2) <= size) {
			child = index * 2;

			// Select the smaller child
			if (child != size && contents[child + 1].importance < contents[child].importance) {
				child++;
			}

			// Stop if the last node's importance is greater than the smaller child
			if (lastNode.importance <= contents[child].importance) {
				break;
			}

			contents[index] = contents[child];
			index = child;
		}

		contents[index] = lastNode;

		return minNode.word;
	}

	/**
	 * Restores the heap property by percolating down the element at the given
	 * index.
	 *
	 * @param index the index of the element to percolate down.
	 */
	private void percolateDown(int index) {
		int child;
		WordNode tmp = contents[index];

		// Continue until the element is in the correct position
		while ((index * 2) <= size) {
			child = index * 2;

			// Select the smaller child
			if (child != size && contents[child + 1].importance < contents[child].importance) {
				child++;
			}

			// Stop if the element's importance is less than or equal to the smaller child
			if (tmp.importance <= contents[child].importance) {
				break;
			}

			contents[index] = contents[child];
			index = child;
		}

		contents[index] = tmp;
	}

	/**
	 * Retrieves the top k words in the heap (those with the lowest importance
	 * scores).
	 *
	 * @return an array of words representing the top k elements in the heap.
	 */
	public String[] getTopKWords() {
		String[] topWords = new String[size];
		for (int i = 0; i < size; i++) {
			topWords[i] = contents[i + 1].word; // Skip index 0
		}
		return topWords;
	}
}
