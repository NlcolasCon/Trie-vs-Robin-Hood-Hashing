package HW;

/**
 * Represents a node in the Trie (prefix tree) structure. Each node can have up
 * to 26 child nodes, corresponding to the 26 letters of the English alphabet.
 * Tracks word lengths and importance for words ending at this node.
 */
public class TrieNode {

	/** Array of child nodes, one for each letter of the alphabet. */
	public TrieNode children[];

	/** Word length for the word ending at this node. */
	int wl;

	/** Importance score for the word ending at this node. */
	int importance;

	/** Constructor to initialize a TrieNode with no children and default values. */
	public TrieNode() {
		children = new TrieNode[26];
		for (int i = 0; i < 26; i++) {
			children[i] = null;
		}
		wl = 0;
		importance = 0;
	}

	/**
	 * Inserts a word into the Trie starting from this node.
	 *
	 * @param word the word to insert.
	 * @param i    the current index of the character being processed.
	 */
	public void insertNode(String word, int i) {
		if (i == word.length()) {
			return; // Base case: end of the word
		}

		// If the child node for the current character does not exist, create it
		if (children[word.charAt(i) - 'a'] == null) {
			children[word.charAt(i) - 'a'] = new TrieNode();
		}

		// Recur to insert the next character
		if (children[word.charAt(i) - 'a'] != null) {
			children[word.charAt(i) - 'a'].insertNode(word, i + 1);
		}

		// Update the word length for the node if it's the end of the word
		if (i + 1 == word.length()) {
			wl = word.length();
		}
	}

	/**
	 * Searches for a word in the Trie starting from this node.
	 *
	 * @param word the word to search for.
	 * @param i    the current index of the character being processed.
	 * @return {@code true} if the word is found, {@code false} otherwise.
	 */
	public boolean searchNode(String word, int i) {
		// Base case: if this is the end of the word in the Trie
		if (i + 1 == wl && word.length() == i + 1) {
			return true;
		}

		// If the search index exceeds the word length or the character node doesn't
		// exist
		if (i == word.length() || children[word.charAt(i) - 'a'] == null) {
			return false;
		}

		// Recur to search the next character
		return children[word.charAt(i) - 'a'].searchNode(word, i + 1);
	}

	/**
	 * Retrieves the importance score of a word.
	 *
	 * @param word the word to retrieve the importance for.
	 * @param i    the current index of the character being processed.
	 * @param len  the expected word length.
	 * @return the importance score of the word, or 0 if not found.
	 */
	public int importance(String word, int i, int len) {
		// Base case: if this is the end of the word in the Trie
		if (i + 1 == word.length() && len == i + 1) {
			if (children[(word.charAt(i) - 'a') % 26] != null) {
				return children[(word.charAt(i) - 'a') % 26].importance;
			}
		} else if (i > word.length()) {
			return 0; // Word length exceeds the current node
		}

		// Recur to retrieve importance for the next character
		if (children[((word.charAt(i) - 'a')) % 26] == null) {
			return 0; // Character node doesn't exist
		} else {
			return children[((word.charAt(i) - 'a')) % 26].importance(word, i + 1,
					children[((word.charAt(i) - 'a')) % 26].wl);
		}
	}

	/**
	 * Increments the importance score of a word in the Trie.
	 *
	 * @param word the word whose importance score is to be incremented.
	 * @param i    the current index of the character being processed.
	 */
	public void incImportance(String word, int i) {
		// Base case: if this is the last character of the word
		if (i + 1 == word.length()) {
			if (children[((word.charAt(i) - 'a')) % 26] == null) {
				return; // Character node doesn't exist
			} else {
				children[((word.charAt(i) - 'a')) % 26].importance++;
				return;
			}
		} else if (i > word.length()) {
			return; // Word length exceeds the current node
		}

		// Recur to increment importance for the next character
		if (children[((word.charAt(i) - 'a')) % 26] == null) {
			return; // Character node doesn't exist
		} else {
			children[((word.charAt(i) - 'a')) % 26].incImportance(word, i + 1);
		}
	}
}
