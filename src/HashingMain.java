package HW;

import java.io.IOException;
import java.util.Scanner;

/**
 * Main class for testing the functionality of Robin Hood Trie and a standard
 * Trie. It reads a dictionary file to populate both data structures, optionally
 * allows user interaction for word suggestions, and compares memory usage
 * between the two structures.
 */
public class HashingMain {

	public static void main(String[] args) throws IOException {

		// Create instances of Robin Hood Trie
		RobinHoodTrie trie = new RobinHoodTrie();

		// Read the dictionary file and insert the text to get importance
		trie.readDictionary(args[0]);
		trie.insertText(args[1]);

		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("Enter a word and how many words you want to suggest: ");
			System.out.println("(Input \"stop 0\" to exit)");

			String word = scanner.next(); // Read the input word
			int k = scanner.nextInt(); // Read the number of suggestions desired

			// If "stop 0" is entered, exit the loop and program
			if (word.toLowerCase().equals("stop") && k == 0) {
				System.out.println("Program exit...");
				break;
			}

			// Initialize a min-heap to store suggestions
			Heap minHeap = new Heap(k);

			// Get word suggestions from the Robin Hood Trie
			trie.getPrefixWords(minHeap, word, k);
			trie.getSameLengthWords(minHeap, word, k);
			trie.getDiffLengthWords(minHeap, word, k);

			// Retrieve and print the top k word suggestions
			String words[] = minHeap.getTopKWords();
			for (int i = 0; i < words.length; i++) {
				System.out.print(words[i] + " ");
			}
			System.out.println();
		}
		scanner.close();
		// Close the scanner after use

		// Compare memory usage between Robin Hood Trie
		System.out.println("Memory of RobinHood: " + trie.findMemorySize());

		return;

	}
}
