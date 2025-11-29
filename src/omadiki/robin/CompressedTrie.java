package omadiki.robin;

import omadiki.DictionaryWord;
import omadiki.MinHeap;
import omadiki.Pair;

public class CompressedTrie {
    // Node class representing each compressed trie node
    protected static class CompressedTrieNode {
        private final RobinHoodHashing hash; // Hash table storing compressed edges
        private boolean isEndOfWord; // Marks if this node terminates a word
        private int importance; // Usage counter for frequency tracking

        public CompressedTrieNode() {
            hash = new RobinHoodHashing();
            isEndOfWord = false;
        }

        // Inserts a compressed edge into the hash table
        public void insertEdge(RobinHoodHashing.Edge edge) {
            hash.insert(edge);
        }
    }

    CompressedTrieNode root;

    public CompressedTrie() {
        root = new CompressedTrieNode(); // Initialize root node
    }

    // Public search method
    public boolean search(String a) {
        return searchRec(this.root, a);
    }

    // Recursive search through compressed edges
    private boolean searchRec(CompressedTrieNode node, String word) {
        RobinHoodHashing.Edge parent = node.hash.getEdge(word);

        if (parent == null) { // No matching compressed edge
            return false;
        } else if (parent.label.equals(word)) { // Full match
            boolean a = parent.child.isEndOfWord;
            if (a) parent.child.importance++; // Increase usage counter
            return a;
        } else { // Partial match → traverse deeper
            String common = word.substring(findCommon(parent.label, word));
            return searchRec(parent.child, common);
        }
    }

    // Public insertion method
    public void insert(String word) {
        insertRec(this.root, word);
    }

    // Recursive insertion with edge splitting
    private void insertRec(CompressedTrieNode node, String word) {
        RobinHoodHashing.Edge parent = node.hash.getEdge(word);

        if (parent == null) { // No edge: create new
            CompressedTrieNode a = new CompressedTrieNode();
            a.isEndOfWord = true;
            RobinHoodHashing.Edge e = new RobinHoodHashing.Edge(word, a);
            node.insertEdge(e);
            return;
        }

        if (parent.label.equals(word)) { // Exact match
            parent.child.isEndOfWord = true;
            return;
        }

        // Find common prefix
        String common = word.substring(0, findCommon(parent.label, word));

        String wordSubstring = word.substring(common.length());
        String parentSubstring = parent.label.substring(common.length());

        if (parent.label.equals(common)) { // Parent edge fully matches prefix
            insertRec(parent.child, wordSubstring);
        } else if (word.equals(common)) { // Word fully matches prefix
            parent.label = common;
            insertRec(parent.child, parentSubstring);
        } else { // Split edge
            parent.label = common;

            CompressedTrieNode old = parent.child;
            parent.child = new CompressedTrieNode();

            insertRec(parent.child, parentSubstring); // Reinsert old edge remainder
            parent.child.hash.getEdge(parentSubstring).child = old;

            insertRec(parent.child, wordSubstring); // Insert new word remainder
        }
    }

    // Public delete call
    public boolean delete(String word) {
        return deleteRec(this.root, word);
    }

    // Recursive delete
    private boolean deleteRec(CompressedTrieNode node, String word) {
        RobinHoodHashing.Edge parent = node.hash.getEdge(word);

        if (parent == null) {
            return false; // Word does not exist
        } else if (parent.label.equals(word)) {
            if (!parent.child.isEndOfWord)
                return false; // Exists as prefix but not full word
            parent.child.isEndOfWord = false;
            parent.occupied = false; // Remove edge from hash
            return true;
        } else {
            String common = word.substring(findCommon(parent.label, word));
            return deleteRec(parent.child, common);
        }
    }

    // Prints the full trie
    public static void print(CompressedTrie e) {
        printRec(e.root, "");
        System.out.println();
    }

    // Recursive print
    private static void printRec(CompressedTrieNode e, String word) {
        if (e == null) {
            return;
        }

        if (e.isEndOfWord) { // Output complete word and importance
            System.out.println(String.format("%s:%d", word, e.importance));
        }

        for (int i = 0; i < e.hash.capacity; i++) {
            if (e.hash.table[i] == null) continue;
            printRec(e.hash.table[i].child, word + e.hash.table[i].label);
        }
    }

    // Finds common prefix length between two strings
    private static int findCommon(String s1, String s2) {
        int i = 0;
        int minLen = Math.min(s1.length(), s2.length());
        while (i < minLen && s1.charAt(i) == s2.charAt(i)) {
            i++;
        }

        return i;
    }

    // Finds node for given prefix and returns (node, remainder)
    private Pair<CompressedTrieNode, String> getPrefix(String prefix) {
        String search = prefix;
        String remainder = "";
        CompressedTrieNode cur = this.root;
        RobinHoodHashing.Edge edge = null;
        while (cur != null) {
            edge = cur.hash.getEdge(search);
            if (edge == null) {
                cur = null;
                break;
            } else {
                int common = findCommon(search, edge.label);
                remainder = edge.label.substring(common, edge.label.length());
                search = search.substring(common, search.length());
                cur = edge.child;
                if (search.isEmpty()) {
                    break; // Prefix fully matched
                }
            }
        }

        return new Pair<>(cur, remainder);
    }

    // Returns k most frequent words starting with prefix
    public MinHeap getWordsWithPrefix(String prefix, int k) {
        Pair<CompressedTrieNode, String> cur = getPrefix(prefix);

        MinHeap heap = new MinHeap(k);
        getWordsRec(cur.getLeft(), prefix + cur.getRight(), heap);

        return heap;
    }

    // Recursively collect words from subtree
    private void getWordsRec(CompressedTrieNode node, String word, MinHeap heap) {
        if (node == null) {
            return;
        }
        if (node.isEndOfWord) {
            heap.insert(new DictionaryWord(word, node.importance));
        }

        for (int i = 0; i < node.hash.capacity; i++) {
            if (node.hash.table[i] == null) continue;
            getWordsRec(node.hash.table[i].child, word + node.hash.table[i].label, heap);
        }
    }

    // Predicts next letter based on highest avg frequency
    public char predictNextLetter(String prefix) {
        Pair<CompressedTrieNode, String> pair = getPrefix(prefix);
        if (!pair.getRight().isEmpty()) {
            return pair.getRight().charAt(0);
        }

        CompressedTrieNode cur = pair.getLeft();
        int maxIndex = -1;
        float max = 0;

        for (int i = 0; i < cur.hash.capacity; i++) {
            if (cur.hash.table[i] == null) continue;
            MinHeap heap = getWordsWithPrefix(prefix + cur.hash.table[i].label, -1);
            float freq = heap.getAvgFrequency();
            if (freq > max) {
                max = freq;
                maxIndex = i;
            }
        }

        return maxIndex == -1 ? '\0' : cur.hash.table[maxIndex].label.charAt(0);
    }

    // Testing
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
        System.out.println(a.search("b"));
        System.out.println(a.search("stock"));

        System.out.println(a.search("bid"));
        System.out.println(a.search("bear"));
        System.out.println(a.search("bid"));
        System.out.println(a.search("bell"));

        a.delete("bell");
        System.out.println("After del:");
        print(a);
        System.out.println(a.search("bell"));

        System.out.println(a.getWordsWithPrefix("b", -1));

        System.out.println();
    }
}