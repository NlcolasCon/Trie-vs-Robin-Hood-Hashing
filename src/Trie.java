package HW;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Implements a basic Trie (prefix tree) data structure for storing and managing
 * words. This Trie supports operations such as insertion, searching, importance
 * tracking, and memory size calculation.
 */
public class Trie {

	/** The root node of the Trie. */
	public TrieNode root;

	/** Constructor to initialize the Trie with a root node. */
	public Trie() {
		root = new TrieNode();
	}

	/**
	 * Inserts a word into the Trie.
	 *
	 * @param word the word to insert.
	 */
	public void insert(String word) {
		word = word.toLowerCase();
		root.insertNode(word, 0);
	}

	/**
	 * Searches for a word in the Trie.
	 *
	 * @param word the word to search for.
	 * @return {@code true} if the word is found, {@code false} otherwise.
	 */
	public boolean search(String word) {
		word = word.toLowerCase();
		return root.searchNode(word, 0);
	}

	/**
	 * Retrieves the importance score of a word.
	 *
	 * @param word the word whose importance is to be retrieved.
	 * @return the importance score of the word, or 0 if the word is not found.
	 */
	public int importance(String word) {
		if (!search(word)) {
			return 0;
		}
		word = word.toLowerCase();
		return root.importance(word, 0, root.wl);
	}

	/**
	 * Increments the importance score of a word.
	 *
	 * @param word the word whose importance is to be incremented.
	 */
	public void incImp(String word) {
		if (!search(word)) {
			return;
		}
		word = word.toLowerCase();
		root.incImportance(word, 0);
	}

	/**
	 * Reads a dictionary file and inserts all valid words into the Trie.
	 *
	 * @param inFile the file path of the dictionary.
	 * @throws FileNotFoundException if the file cannot be found.
	 */
	public void readDictionary(String inFile) throws FileNotFoundException {
		File input = new File(inFile);
		Scanner scan = new Scanner(input);
		while (scan.hasNext()) {
			String buffer = scan.next();
			if (filterWord(buffer) != null) {
				insert(filterWord(buffer));
			}
		}
		scan.close();
	}

	/**
	 * Reads a text file and increments the importance of all valid words in the
	 * Trie.
	 *
	 * @param textFile the file path of the text.
	 * @throws FileNotFoundException if the file cannot be found.
	 */
	public void insertText(String textFile) throws FileNotFoundException {
		File text = new File(textFile);
		Scanner scan = new Scanner(text);
		while (scan.hasNext()) {
			String buffer = scan.next();
			if (filterWord(buffer) != null) {
				incImp(filterWord(buffer));
			}
		}
		scan.close();
	}

	/**
	 * Filters a word to ensure it contains only valid alphabetical characters.
	 *
	 * @param input the word to filter.
	 * @return the filtered word, or {@code null} if the word is invalid.
	 */
	private static String filterWord(String input) {
		String output = "";
		input = input.toLowerCase();
		int i = input.length() - 1;

		// Remove trailing non-alphabetical characters
		while (i >= 0) {
			if (input.charAt(i) >= 'a' && input.charAt(i) <= 'z') {
				break;
			}
			i--;
		}
		output = input.substring(0, i + 1);

		// Ensure all characters are alphabetical
		for (int j = 0; j < output.length(); j++) {
			if (!(output.charAt(j) >= 'a' && output.charAt(j) <= 'z')) {
				return null;
			}
		}

		return output.isEmpty() ? null : output;
	}

	/**
	 * Calculates the memory usage of the Trie by traversing all nodes.
	 *
	 * @return the total memory size of the Trie.
	 */
	public int findMemorySize() {
		int[] size = { 0 };
		DFS(root, size);
		return size[0];
	}

	/**
	 * Depth-first traversal of the Trie to calculate its memory usage.
	 *
	 * @param v    the current node being processed.
	 * @param size an array holding the cumulative memory size.
	 */
	private void DFS(TrieNode v, int[] size) {
		// Add memory size of the current node
		size[0] += (2 * 16); // For two references in the TrieNode (children, wl)

		if (v.children != null) {
			size[0] += (26 * 16); // Memory for the 26 child references
			for (int i = 0; i < 26; i++) {
				if (v.children[i] != null) {
					DFS(v.children[i], size); // Recurse into the child nodes
				}
			}
		}
	}
}
