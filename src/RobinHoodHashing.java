package HW;

/**
 * Implements a custom hash table using the Robin Hood hashing technique. This
 * structure supports insertion, searching, updating importance, rehashing, and
 * retrieving similar words based on prefixes or lengths.
 */
class RobinHoodHashing {

	/** The array representing the hash table. */
	Element[] table;

	/** The current capacity of the hash table. */
	int capacity;

	/** The current number of elements in the hash table. */
	int size;

	/** The maximum probe length encountered during insertion. */
	int maxProbeLenght;

	/**
	 * Default constructor initializing the hash table with a default capacity of 5.
	 */
	public RobinHoodHashing() {
		table = new Element[5];
		capacity = 5;
		size = 0;
		maxProbeLenght = 0;
	}

	/**
	 * Constructor initializing the hash table with a specified capacity.
	 *
	 * @param num the initial capacity of the hash table.
	 */
	public RobinHoodHashing(int num) {
		table = new Element[num];
		capacity = num;
		size = 0;
		maxProbeLenght = 0;
	}

	/**
	 * Inserts a word into the hash table using Robin Hood hashing.
	 *
	 * @param word the word to insert.
	 * @param i    the index of the character being processed.
	 */
	public void insertWord(String word, int i) {
		if (i == word.length()) {
			return;
		}

		Element tmp = new Element();
		tmp.key = word.charAt(i);
		tmp.probeLength = 0;

		boolean swap = false;

		// Handle collisions and apply Robin Hood swapping if necessary
		while (table[((tmp.key - 'a') + tmp.probeLength) % capacity] != null
				&& table[((tmp.key - 'a') + tmp.probeLength) % capacity].key != tmp.key) {

			if (table[((tmp.key - 'a') + tmp.probeLength) % capacity].probeLength < tmp.probeLength) {
				swap = true;

				Element s = tmp;
				tmp = table[((s.key - 'a') + s.probeLength) % capacity];
				table[((s.key - 'a') + s.probeLength) % capacity] = s;

				if (maxProbeLenght < s.probeLength) {
					maxProbeLenght = s.probeLength;
				}

				if (s.next == null) {
					s.next = new RobinHoodHashing();
					s.next.insertWord(word, i + 1);
					if (i + 1 == word.length()) {
						s.wl = word.length();
					}
				}

				tmp.probeLength++;
			} else {
				tmp.probeLength++;
			}
		}

		// Insert the element into the table
		if (swap && table[((tmp.key - 'a') + tmp.probeLength) % capacity] == null) {
			table[((tmp.key - 'a') + tmp.probeLength) % capacity] = tmp;
			size++;
		} else if (table[((tmp.key - 'a') + tmp.probeLength) % capacity] == null) {
			table[((tmp.key - 'a') + tmp.probeLength) % capacity] = tmp;
			size++;
			table[((tmp.key - 'a') + tmp.probeLength) % capacity].next = new RobinHoodHashing();
			table[((tmp.key - 'a') + tmp.probeLength) % capacity].next.insertWord(word, i + 1);
		} else {
			table[((tmp.key - 'a') + tmp.probeLength) % capacity].next.insertWord(word, i + 1);
		}

		if (i + 1 == word.length()) {
			table[((tmp.key - 'a') + tmp.probeLength) % capacity].wl = word.length();
		}

		if ((((double) size / (double) capacity) * 100.0) >= 90.0) {
			rehash();
		}
	}

	/**
	 * Searches for a word in the hash table.
	 *
	 * @param word the word to search for.
	 * @param i    the index of the character being processed.
	 * @return {@code true} if the word is found, {@code false} otherwise.
	 */
	public boolean searchWord(String word, int i) {
		if (i + 1 == word.length()) {
			int tries = 0;
			while (tries <= maxProbeLenght) {
				if (table[((word.charAt(i) - 'a') + tries) % capacity] == null) {
					tries++;
				} else if (table[((word.charAt(i) - 'a') + tries) % capacity].key == word.charAt(i)
						&& table[((word.charAt(i) - 'a') + tries) % capacity].wl == word.length()) {
					return true;
				} else {
					tries++;
				}
			}
		} else if (i >= word.length()) {
			return false;
		}

		int tries = 0;
		while (tries < capacity) {
			if (table[((word.charAt(i) - 'a') + tries) % capacity] == null) {
				return false;
			} else if (table[((word.charAt(i) - 'a') + tries) % capacity].key == word.charAt(i)) {
				return table[((word.charAt(i) - 'a') + tries) % capacity].next.searchWord(word, i + 1);
			} else {
				tries++;
			}
		}
		return false;
	}

	/**
	 * Retrieves the importance score of a given word.
	 *
	 * @param word the word whose importance is to be retrieved.
	 * @param i    the index of the character being processed.
	 * @return the importance score of the word.
	 */
	public int importance(String word, int i) {
		if (i + 1 == word.length()) {
			int tries = 0;
			while (tries <= maxProbeLenght) {
				if (table[((word.charAt(i) - 'a') + tries) % capacity] == null) {
					tries++;
				} else if (table[((word.charAt(i) - 'a') + tries) % capacity].key == word.charAt(i)
						&& table[((word.charAt(i) - 'a') + tries) % capacity].wl == word.length()) {
					return table[((word.charAt(i) - 'a') + tries) % capacity].importance;
				} else {
					tries++;
				}
			}
		} else if (i == word.length()) {
			return 0;
		}

		int tries = 0;
		while (tries <= maxProbeLenght) {
			if (table[((word.charAt(i) - 'a') + tries) % capacity] == null) {
				return 0;
			} else if (table[((word.charAt(i) - 'a') + tries) % capacity].key == word.charAt(i)) {
				return table[((word.charAt(i) - 'a') + tries) % capacity].next.importance(word, i + 1);
			} else {
				tries++;
			}
		}
		return 0;
	}

	/**
	 * Increments the importance score of a word.
	 *
	 * @param word the word whose importance is to be incremented.
	 * @param i    the index of the character being processed.
	 */
	public void incImportance(String word, int i) {
		if (i + 1 == word.length()) {
			int tries = 0;
			while (tries <= maxProbeLenght) {
				if (table[((word.charAt(i) - 'a') + tries) % capacity] == null) {
					tries++;
				} else if (table[((word.charAt(i) - 'a') + tries) % capacity].key == word.charAt(i)
						&& table[((word.charAt(i) - 'a') + tries) % capacity].wl == word.length()) {
					table[((word.charAt(i) - 'a') + tries) % capacity].importance++;
					return;
				} else {
					tries++;
				}
			}
		} else if (i == word.length()) {
			return;
		}
		int tries = 0;
		while (tries <= maxProbeLenght) {
			if (table[((word.charAt(i) - 'a') + tries) % capacity] == null) {
				return;
			} else if (table[((word.charAt(i) - 'a') + tries) % capacity].key == word.charAt(i)) {
				table[((word.charAt(i) - 'a') + tries) % capacity].next.incImportance(word, i + 1);
				return;
			} else {
				tries++;
			}
		}
		return;
	}

	/**
	 * Retrieves all words from the hash table that start with the specified prefix.
	 * The matching words are added to the provided min-heap, ordered by their
	 * importance.
	 *
	 * @param minHeap the heap used to store matching words along with their
	 *                importance scores.
	 * @param word    the prefix to match words against.
	 * @param k       the maximum number of words to retrieve.
	 */
	public void getPrefixWords(Heap minHeap, String word, int k) {
		for (int index = 0; index < capacity; index++) {
			// Check if the current table index contains a matching word
			if (table[index] != null && table[index].wl == word.length() + 1
					&& (searchWord(word + table[index].key, word.length()))) {
				// Insert the matching word into the heap
				minHeap.insert(word + table[index].key, importance(word + table[index].key, word.length()));

				// Recursively search for more matching words in the next hash table level
				if (table[index].next != null) {
					table[index].next.getPrefixWords(minHeap, (word + table[index].key), k);
				}
			} else if (table[index] != null && table[index].wl == 0) {
				// Traverse deeper into the chain for words matching the prefix
				if (table[index].next != null) {
					table[index].next.getPrefixWords(minHeap, (word + table[index].key), k);
				}
			}
		}
	}

	/**
	 * Navigates the hash table to reach the given prefix and retrieves words
	 * starting from that prefix. Once the prefix is found, it calls
	 * {@code getPrefixWords} to retrieve matching words.
	 *
	 * @param minHeap the heap used to store words that match the prefix along with
	 *                their importance scores.
	 * @param word    the prefix to match words against.
	 * @param k       the maximum number of words to retrieve.
	 * @param i       the current character index being processed in the prefix.
	 */
	public void reachWordPrefix(Heap minHeap, String word, int k, int i) {

		// If the entire prefix has been processed
		if (i + 1 == word.length()) {
			int tries = 0;

			// Search for the prefix in the hash table
			while (tries <= maxProbeLenght) {
				if (table[((word.charAt(i) - 'a') + tries) % capacity] == null) {
					tries++; // Move to the next probe length
				} else if (table[((word.charAt(i) - 'a') + tries) % capacity].key == word.charAt(i)) {
					// If the prefix exists and has a next chain, retrieve words starting with this
					// prefix
					if (table[((word.charAt(i) - 'a') + tries) % capacity].next != null) {
						table[((word.charAt(i) - 'a') + tries) % capacity].next.getPrefixWords(minHeap, word, k);
						return;
					}
					return; // Prefix found, but no further chains exist
				} else {
					tries++; // Continue searching with a different probe length
				}
			}
			return;
		}

		// Continue searching for the next character of the prefix in the hash table
		int tries = 0;
		while (tries <= maxProbeLenght) {
			if (table[((word.charAt(i) - 'a') + tries) % capacity] == null) {
				tries++; // Move to the next probe length
			} else if (table[((word.charAt(i) - 'a') + tries) % capacity].key == word.charAt(i)) {
				// Recur to the next level to process the remaining prefix
				table[((word.charAt(i) - 'a') + tries) % capacity].next.reachWordPrefix(minHeap, word, k, i + 1);
				return;
			} else {
				tries++; // Continue searching with a different probe length
			}
		}
	}

	/**
	 * Retrieves words from the hash table that have the same length as the given
	 * word. These words are added to a min-heap if they are similar to the given
	 * word.
	 *
	 * @param minHeap the heap used to store matching words along with their
	 *                importance scores.
	 * @param word    the word to compare lengths and similarity against.
	 * @param k       the maximum number of words to retrieve.
	 * @param index   the current index in the hash table being processed.
	 * @param buffer  the constructed word during traversal.
	 */
	public void getSameLengthWords(Heap minHeap, String word, int k, int index, String buffer) {
		// Base case: If the constructed buffer matches the length of the word
		if (buffer.length() + 1 == word.length()) {
			// Check if the buffer is similar to the target word
			for (int i = 0; i < capacity; i++) {
				if (table[i] != null) {
					if (isSimilar(buffer + table[i].key, word)) {
						minHeap.insert(buffer + table[i].key, importance(buffer + table[i].key, index));
					}
				}
			}
			return;

		} else if (buffer.length() > word.length()) {
			// Stop processing if the buffer exceeds the target word's length
			return;
		}

		// Traverse through the hash table
		for (int i = 0; i < capacity; i++) {
			if (table[i] != null && table[i].next != null) {
				// Recur into the next level of the hash table with the current key appended to
				// the buffer
				table[i].next.getSameLengthWords(minHeap, word, k, index + 1, buffer + table[i].key);
			} else if (table[i] != null) {
				// Append the current key to the buffer
				buffer = buffer + table[i].key;

				// Check if the buffer matches the target word's length
				if (buffer.length() + 1 == word.length()) {
					// Check if the buffer is similar to the target word
					for (int j = 0; j < capacity; j++) {
						if (table[j] != null) {
							if (isSimilar(buffer + table[j].key, word)) {
								minHeap.insert(buffer + table[i].key, importance(buffer + table[i].key, index));
							}
						}
					}
					return;
				}

			}
		}
	}

	/**
	 * Determines if two words are similar based on a maximum allowable difference
	 * of two characters. A word is considered similar if the number of differing
	 * characters between them is less than or equal to 2.
	 *
	 * @param buffer the word being compared.
	 * @param word   the target word to compare against.
	 * @return {@code true} if the words are similar (differ by 2 or fewer
	 *         characters), {@code false} otherwise.
	 */
	public boolean isSimilar(String buffer, String word) {
		int diff = 0; // Tracks the number of differing characters
		int i = 0; // Pointer for the target word
		int c = 0; // Pointer for the buffer word
		boolean exists[] = new boolean[26];

		// Compare characters of both words
		while (i < word.length() && c < buffer.length()) {
			if (buffer.charAt(c) == word.charAt(i)) {
				// Characters match, move both pointers
				exists[(buffer.charAt(c) - 'a') % 26] = true;
				i++;
				c++;
			} else if (!word.contains(buffer.charAt(c) + "") && !exists[(buffer.charAt(c) - 'a') % 26]) {
				// Characters differ, increment the difference count
				diff++;
				if (diff > 2) {
					return false; // Too many differences, words are not similar
				}
				c++; // Skip to the next character in the buffer
			} else {
				i++;
			}
		}

		// Add remaining unmatched characters to the difference count
		diff += buffer.length() - c;
		diff += word.length() - i;

		// Return true if the total difference is within the allowable limit
		return (diff <= 2);
	}

	/**
	 * Retrieves words from the hash table that have lengths slightly longer or
	 * shorter than the given word. Words that differ in length by -1, +1, or +2 are
	 * considered, and they are added to a min-heap if they are similar to the given
	 * word based on specific criteria.
	 *
	 * @param minHeap the heap used to store matching words along with their
	 *                importance scores.
	 * @param word    the target word to compare lengths and similarity against.
	 * @param k       the maximum number of words to retrieve.
	 * @param index   the current index in the hash table being processed.
	 * @param buffer  the constructed word during traversal.
	 */
	public void getDiffLengthWords(Heap minHeap, String word, int k, int index, String buffer) {
		// Check if the buffer is slightly longer than the word (+1 or +2)
		if (buffer.length() == word.length()) {
			for (int i = 0; i < capacity; i++) {
				if (table[i] != null) {
					if (isSimilar(buffer + table[i].key, word)) {
						minHeap.insert(buffer + table[i].key, importance(buffer + table[i].key, index));
					}
				}
			}
		} else if (buffer.length() == word.length() + 1) {
			for (int i = 0; i < capacity; i++) {
				if (table[i] != null) {
					if (isSimilar(buffer + table[i].key, word)) {
						minHeap.insert(buffer + table[i].key, importance(buffer + table[i].key, index));
					}
				}
			}
		}
		// Check if the buffer is shorter than the word (-1 in length)
		else if (buffer.length() == word.length() - 2) {
			for (int i = 0; i < capacity; i++) {
				if (table[i] != null) {
					if (isLess1(buffer + table[i].key, word)) {
						minHeap.insert(buffer + table[i].key, importance(buffer + table[i].key, index));
					}
				}
			}
		}
		// Stop processing if the buffer exceeds the allowable length difference
		else if (buffer.length() > word.length() + 2) {
		}

		// Traverse through the hash table
		for (int j = 0; j < capacity; j++) {
			if (table[j] != null && table[j].next != null) {
				// Recur into the next level of the hash table with the current key appended to
				// the buffer
				table[j].next.getDiffLengthWords(minHeap, word, k, index + 1, buffer + table[j].key);
			} else if (table[j] != null) {
				// Append the current key to the buffer
				buffer = buffer + table[j].key;

				// Check again for length conditions after adding the current key
				if (buffer.length() == word.length()) {
					for (int i = 0; i < capacity; i++) {
						if (table[i] != null) {
							if (isSimilar(buffer + table[i].key, word)) {
								minHeap.insert(buffer + table[i].key, importance(buffer + table[i].key, index));
							}
						}
					}
				} else if (buffer.length() == word.length() + 1) {
					for (int i = 0; i < capacity; i++) {
						if (table[i] != null) {
							if (isSimilar(buffer + table[i].key, word)) {
								minHeap.insert(buffer + table[i].key, importance(buffer + table[i].key, index));
							}
						}
					}
				}
				// Check if the buffer is shorter than the word (-1 in length)
				else if (buffer.length() == word.length() - 2) {
					for (int i = 0; i < capacity; i++) {
						if (table[i] != null) {
							if (isLess1(buffer + table[i].key, word)) {
								minHeap.insert(buffer + table[i].key, importance(buffer + table[i].key, index));
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Determines if the given buffer is a shortened version of the target word. A
	 * word is considered a shortened version if its length is exactly one less than
	 * the target word, and their characters align with at most one missing
	 * character in the buffer.
	 *
	 * @param word   the target word to compare against.
	 * @param buffer the shorter word being compared.
	 * @return {@code true} if the buffer is a shortened version of the word,
	 *         {@code false} otherwise.
	 */
	public boolean isLess1(String word, String buffer) {
		boolean[] exists = new boolean[26]; // Tracks the characters encountered in the word
		int diff = 0; // Tracks the number of differing characters
		int i = 0; // Pointer for the target word
		int c = 0; // Pointer for the buffer

		// Compare characters of both the word and the buffer
		while (i < word.length() && c < buffer.length()) {
			if (buffer.charAt(c) == word.charAt(i)) {
				// Characters match, mark as encountered and move both pointers
				exists[(word.charAt(i) - 'a') % 26] = true;
				i++;
				c++;
			} else {
				// Characters differ, increment the difference count
				diff++;
				if (diff > 2) {
					return false; // More than two differences, not a valid shortened version
				}
				c++; // Move the buffer pointer to check the next character
			}
		}

		// Account for remaining unmatched characters
		diff += buffer.length() - c;
		diff += word.length() - i;

		// Check if the buffer is exactly one character shorter than the word
		if (buffer.length() == word.length() - 1) {
			return diff == 0; // Return true if no unmatched characters remain
		} else {
			return diff <= 2; // Return true if differences are within the threshold
		}
	}

	/**
	 * Rebuilds the hash table with a larger capacity to handle increased load and
	 * maintain efficiency. This involves transferring all existing elements into a
	 * new table while recalculating their positions based on the new capacity and
	 * resetting probe lengths.
	 */
	public void rehash() {
		int prevCapacity = capacity; // Store the current capacity
		int newCapacity = getNextCapacity(capacity); // Determine the new capacity
		Element[] tmp = new Element[size]; // Temporary array to store existing elements
		int count = 0; // Counter for the number of elements copied

		// Transfer all non-null elements from the current table to the temporary array
		for (int i = 0; i < prevCapacity; i++) {
			if (table[i] != null) {
				tmp[count] = new Element();
				tmp[count] = table[i];
				tmp[count].probeLength = 0; // Reset probe length for rehashing
				count++;
			}
		}

		// Reinitialize the hash table with the new capacity
		table = new Element[newCapacity];
		capacity = newCapacity;
		maxProbeLenght = 0; // Reset the maximum probe length
		size = 0; // Reset the size counter

		// Reinsert all elements from the temporary array into the new table
		for (int i = 0; i < count; i++) {
			while (true) {
				// Calculate the new index for the element
				int index = ((tmp[i].key - 'a') + tmp[i].probeLength) % capacity;

				if (table[index] == null) {
					// Insert the element into the new table
					table[index] = new Element();
					table[index] = tmp[i];

					// Update the maximum probe length if necessary
					if (maxProbeLenght < table[index].probeLength) {
						maxProbeLenght = table[index].probeLength;
					}
					size++;
					break; // Exit the loop after successful insertion
				} else if (table[index].probeLength < tmp[i].probeLength) {
					// Handle Robin Hood swapping
					Element change = table[index];
					table[index] = tmp[i];
					tmp[i] = change;
					tmp[i].probeLength++;
				} else {
					tmp[i].probeLength++; // Increment the probe length and retry
				}
			}
		}
	}

	/**
	 * Determines the next capacity for the hash table during rehashing. If the
	 * current capacity is smaller than one of the predefined prime numbers, the
	 * method selects the next largest prime. Otherwise, the capacity is doubled.
	 *
	 * @param currentCapacity the current capacity of the hash table.
	 * @return the next capacity for the hash table.
	 */
	private int getNextCapacity(int currentCapacity) {
		int[] primes = { 11, 19, 29 }; // Predefined prime numbers for capacity scaling

		// Select the next prime number greater than the current capacity
		for (int prime : primes) {
			if (prime > currentCapacity) {
				return prime;
			}
		}

		// If no suitable prime is found, double the current capacity
		return currentCapacity * 2;
	}
}
