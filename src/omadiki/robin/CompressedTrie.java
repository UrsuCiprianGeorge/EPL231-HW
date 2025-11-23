package omadiki.robin;

public class CompressedTrie {
	protected static class CompressedTrieNode {
        private RobinHoodHashing hash;
		private boolean isEndOfWord;
        private int importance;

		public CompressedTrieNode() {
			hash = new RobinHoodHashing();
			isEndOfWord = false;
		}

		public void insertEdge(RobinHoodHashing.Edge edge) {
            this.isEndOfWord = false;
			hash.insert(edge);
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
        RobinHoodHashing.Edge parent = node.hash.getEdge(word);

		if (parent == null) {
			CompressedTrieNode a = new CompressedTrieNode();
			a.isEndOfWord = true;
			RobinHoodHashing.Edge e = new RobinHoodHashing.Edge(word, a);
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

            parent.child.hash.getEdge(parentSubstring).child = old;

            insertRec(parent.child, wordSubstring);
        }
		
	}

	public static void print(CompressedTrie e) {
		printRec(e.root, "");
		System.out.println();
	}

	private static void printRec(CompressedTrieNode e, String word) {
		if (e == null) {
            return;
        }

        if (e.isEndOfWord) {
			System.out.println(String.format("%s:%d", word, e.importance));
		}

		for (int i = 0; i < e.hash.capacity; i++) {
            if (e.hash.table[i] == null) continue;
			printRec(e.hash.table[i].child, word + e.hash.table[i].label);
		}
	}

	public boolean search(String a) {
        return searchRec(this.root, a);
	}

    private boolean searchRec(CompressedTrieNode node, String word) {
        RobinHoodHashing.Edge parent = node.hash.getEdge(word);

        if (parent == null) {
            return false;
        } else if (parent.label.equals(word)) {
            boolean a = parent.child.isEndOfWord;
            if (a) parent.child.importance++;
            return a;
        } else  {
            String common = word.substring(findCommon(parent.label, word));
            return searchRec(parent.child, common);
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
