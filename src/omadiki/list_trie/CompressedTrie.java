package omadiki.list_trie;

public class CompressedTrie {
	protected static class CompressedTrieNode {
		private SinglyLinkedList list;
		public boolean isEndOfWord;

		public CompressedTrieNode() {
			list = new SinglyLinkedList();
			isEndOfWord = false;
		}

		public void insertEdge(SinglyLinkedList.Edge edge) {
			//this.isEndOfWord = false;
			list.insert(edge);
		}

	}

	CompressedTrieNode root;

	public CompressedTrie() {
		root = new CompressedTrieNode();
	}

	public void insert(String word) {
		insertRec(this.root, word);
	}

	private void insertRec(CompressedTrieNode node, String word) {
		SinglyLinkedList.Edge parent = node.list.getEdge(word.charAt(0));

		if (parent == null) {
			CompressedTrieNode a = new CompressedTrieNode();
			a.isEndOfWord = true;
			SinglyLinkedList.Edge e = new SinglyLinkedList.Edge(word, a);
			node.insertEdge(e);
			return;
		}

		if (parent.label.equals(word)) {
			parent.child.isEndOfWord = true;
			return;
		}

		String common = word.substring(0, findCommon(parent.label, word));

		String wordSubstring = word.substring(common.length());
		String parentSubstring = parent.label.substring(common.length());

		if (parent.label.equals(common)) {
			insertRec(parent.child, wordSubstring);
		} else if (word.equals(common)) {
			parent.label = common;
			insertRec(parent.child, parentSubstring);
		} else {
			parent.label = common;

			CompressedTrieNode old = parent.child;
			CompressedTrieNode neww = new CompressedTrieNode();
			parent.child = neww;

			insertRec(parent.child, parentSubstring);

			parent.child.list.getEdge(parentSubstring.charAt(0)).child = old;

			insertRec(parent.child, wordSubstring);
		}

	}

	private static int findCommon(String s1, String s2) {
		int i = 0;
		int minLen = Math.min(s1.length(), s2.length());
		while (i < minLen && s1.charAt(i) == s2.charAt(i)) {
			i++;
		}

		return i;
	}

	private static void print(CompressedTrie e) {
		printRec(e.root, "");
		System.out.println();
	}

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

	boolean search(String a) {
		return searchRec(this.root, a);
	}

	public boolean searchRec(CompressedTrieNode node, String word) {
		SinglyLinkedList.Edge parent = node.list.getEdge(word.charAt(0));

		if (parent == null) {
			return false;
		} else if (parent.label.equals(word)) {
			return parent.child.isEndOfWord;
		} else {
			String common = word.substring(findCommon(parent.label, word));
			return searchRec(parent.child, common);
		}
	}

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
		System.out.println(a.search("stock"));

		System.out.println();
	}

}