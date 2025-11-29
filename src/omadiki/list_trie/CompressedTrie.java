package omadiki.list_trie;

/**
 * Implements a Compressed Trie (also known as a Radix Trie or Patricia Tree)
 * where edges from a node are managed using a {@code SinglyLinkedList}.
 * <p>
 * This structure optimizes space by compressing chains of single-child nodes.
 */
public class CompressedTrie {

	/**
	 * Represents a node in the Compressed Trie.
	 * Each node holds a list of outgoing edges and a flag indicating
	 * if it marks the end of a word.
	 */
	protected static class CompressedTrieNode {
		/** The list of edges (children) emanating from this node. */
		private SinglyLinkedList list;
		/** Flag indicating if this node is the end of a complete word. */
		public boolean isEndOfWord;

		/**
		 * Constructs a new, non-word-ending node with an empty edge list.
		 */
		public CompressedTrieNode() {
			list = new SinglyLinkedList();
			isEndOfWord = false;
		}

		/**
		 * Inserts a new edge into the node's list of outgoing edges.
		 *
		 * @param edge The edge to insert.
		 */
		public void insertEdge(SinglyLinkedList.Edge edge) {
			//this.isEndOfWord = false;
			list.insert(edge);
		}

	}

	/** The root node of the Compressed Trie. */
	CompressedTrieNode root;

	/**
	 * Constructs an empty Compressed Trie.
	 */
	public CompressedTrie() {
		root = new CompressedTrieNode();
	}

	/**
	 * Inserts a word into the Compressed Trie.
	 *
	 * @param word The word to insert.
	 */
	public void insert(String word) {
		insertRec(this.root, word);
	}

	/**
	 * Recursive helper method for inserting a word into the trie.
	 *
	 * @param node The current node being examined.
	 * @param word The remaining part of the word to insert.
	 */
	private void insertRec(CompressedTrieNode node, String word) {
		SinglyLinkedList.Edge parent = node.list.getEdge(word.charAt(0));

		if (parent == null) {
			// Case 1: No existing edge starts with the first char.
			CompressedTrieNode a = new CompressedTrieNode();
			a.isEndOfWord = true;
			SinglyLinkedList.Edge e = new SinglyLinkedList.Edge(word, a);
			node.insertEdge(e);
			return;
		}

		if (parent.label.equals(word)) {
			// Case 2: An existing edge's label exactly matches the remaining word.
			parent.child.isEndOfWord = true;
			return;
		}

		String common = word.substring(0, findCommon(parent.label, word));

		String wordSubstring = word.substring(common.length());
		String parentSubstring = parent.label.substring(common.length());

		if (parent.label.equals(common)) {
			// Case 3: Word is a descendant of the existing edge.
			insertRec(parent.child, wordSubstring);
		} else if (word.equals(common)) {
			// Case 4: Existing edge is a descendant of the word being inserted.
			parent.label = common;
			insertRec(parent.child, parentSubstring);
			//parent.child.isEndOfWord = true;
		} else {
			// Case 5: Mismatch (split required).
			parent.label = common;

			CompressedTrieNode old = parent.child;
            parent.child = new CompressedTrieNode();

			insertRec(parent.child, parentSubstring);

			parent.child.list.getEdge(parentSubstring.charAt(0)).child = old;

			insertRec(parent.child, wordSubstring);
		}

	}

	/**
	 * Finds the length of the longest common prefix between two strings.
	 *
	 * @param s1 The first string.
	 * @param s2 The second string.
	 * @return The length of the common prefix.
	 */
	private static int findCommon(String s1, String s2) {
		int i = 0;
		int minLen = Math.min(s1.length(), s2.length());
		while (i < minLen && s1.charAt(i) == s2.charAt(i)) {
			i++;
		}

		return i;
	}

	/**
	 * Helper method to print all words in the trie.
	 *
	 * @param e The CompressedTrie instance.
	 */
	private static void print(CompressedTrie e) {
		printRec(e.root, "");
		System.out.println();
	}

	/**
	 * Recursive helper method to traverse the trie and print words.
	 *
	 * @param e The current node.
	 * @param word The word prefix accumulated so far.
	 */
	private static void printRec(CompressedTrieNode e, String word) {
		SinglyLinkedList.Node cur = e.list.getHead();
		if (cur == null) {
			System.out.println(word);
		} else if (e.isEndOfWord) {
			System.out.println(word);
		}

		while (cur != null) {
			printRec(cur.edge.child, word + cur.edge.label);
			cur = cur.next;
		}
	}

	/**
	 * Searches for a word in the Compressed Trie.
	 *
	 * @param a The word to search for.
	 * @return {@code true} if the word is found and marked as a word end, {@code false} otherwise.
	 */
	public boolean search(String a) {
		return searchRec(this.root, a);
	}

	/**
	 * Recursive helper method for searching a word in the trie.
	 *
	 * @param node The current node being examined.
	 * @param word The remaining part of the word to search for.
	 * @return {@code true} if the word is found and marked as a word end, {@code false} otherwise.
	 */
	public boolean searchRec(CompressedTrieNode node, String word) {
		SinglyLinkedList.Edge parent = node.list.getEdge(word.charAt(0));

		if (parent == null) {
			// No edge starting with the required character. Word not found.
			return false;
		} else if (parent.label.equals(word)) {
			// The edge label exactly matches the remaining word.
			return parent.child.isEndOfWord;
		} else {
			String common = word.substring(findCommon(parent.label, word));
			return searchRec(parent.child, common);
		}
	}

	/**
	 * Main method for testing the Compressed Trie implementation.
	 *
	 * @param args Command line arguments (unused).
	 */
	public static void main(String[] args) {
		var a = new CompressedTrie();

		a.insert("bear");
		print(a);

		a.insert("bell");
		print(a);

		a.insert("bid");
		print(a);

		a.insert("be");
		print(a);

		a.insert("bull");
		print(a);

		a.insert("stock");
		print(a);

		a.insert("stop");
		print(a);

		System.out.println(a.search("patata"));
		System.out.println(a.search("be"));

		System.out.println();
	}

}