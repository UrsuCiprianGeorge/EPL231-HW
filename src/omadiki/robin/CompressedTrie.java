package omadiki.robin;

import omadiki.DictionaryWord;
import omadiki.MinHeap;
import omadiki.Pair;

/**
 * Implements a Compressed Trie data structure.
 * <p>
 * This structure optimizes a standard Trie by compressing sequences of nodes
 * that have only a single child into a single edge labeled with the full
 * substring. Edge storage and lookups are managed using a
 * {@link RobinHoodHashing} hash table.
 * </p>
 */
public class CompressedTrie {
    /**
     * Node class representing each compressed trie node.
     * <p>
     * Each node stores its outgoing edges in a hash table ({@link RobinHoodHashing})
     * and tracks word completion and usage frequency.
     * </p>
     */
    protected static class CompressedTrieNode {
        /**
         * Hash table storing compressed edges.
         */
        private final RobinHoodHashing hash;
        /**
         * Marks if this node terminates a word.
         */
        private boolean isEndOfWord;
        /**
         * Usage counter for frequency tracking and prediction.
         */
        private int importance;

        /**
         * Constructs a new, empty {@code CompressedTrieNode}.
         */
        public CompressedTrieNode() {
            hash = new RobinHoodHashing();
            isEndOfWord = false;
        }

        /**
         * Inserts a compressed edge into the node's hash table.
         *
         * @param edge The compressed edge to insert.
         */
        public void insertEdge(RobinHoodHashing.Edge edge) {
            hash.insert(edge);
        }
    }

    /**
     * The root node of the compressed trie.
     */
    public CompressedTrieNode root;

    /**
     * Constructs an empty {@code CompressedTrie} with an initialized root node.
     */
    public CompressedTrie() {
        root = new CompressedTrieNode(); // Initialize root node
    }

    /**
     * Searches for a word in the compressed trie.
     * <p>
     * If the word is found, its importance (usage counter) is incremented.
     * </p>
     *
     * @param a The string (word) to search for.
     * @return {@code true} if the word exists and is marked as an end-of-word,
     * {@code false} otherwise.
     */
    public boolean search(String a) {
        return searchRec(this.root, a);
    }

    /**
     * Recursive helper for the search operation.
     *
     * @param node The current node to search from.
     * @param word The remaining portion of the word to match.
     * @return {@code true} if the word is found, {@code false} otherwise.
     */
    private boolean searchRec(CompressedTrieNode node, String word) {
        RobinHoodHashing.Edge parent = node.hash.search(word);

        if (parent == null) { // No matching compressed edge
            return false;
        } else if (parent.label.equals(word)) { // Full match
            boolean a = parent.child.isEndOfWord;
            if (a) parent.child.importance++; // Increase usage counter
            return a;
        } else { // Partial match
            String common = word.substring(findCommon(parent.label, word));
            return searchRec(parent.child, common);
        }
    }

    public long getTotalMemory(CompressedTrieNode node) {

        if (node == null) return 0;

        long sum = 0;

        // Size of node fields (approximate)
        sum += 4; // boolean isEndOfWord (JVM pads)
        sum += 4; // importance int
        sum += 8; // reference to hash object

        // Size of RobinHoodHashing table
        if (node.hash.table != null) {
            for (int i = 0; i < node.hash.capacity; i++) {
                var edge = node.hash.table[i];

                if (edge != null) {
                    // Edge object overhead
                    sum += 12; // object header (approx)

                    // Label string memory
                    sum += 8; // reference to String
                    sum += 12; // String header
                    sum += edge.label.length() * 2; // 2 bytes per char

                    // Recursive child memory
                    sum += getTotalMemory(edge.child);
                }
            }
        }

        return sum;
    }


    /**
     * Inserts a word into the compressed trie.
     *
     * @param word The word to insert.
     */
    public void insert(String word) {
        insertRec(this.root, word);
    }

    /**
     * Recursive helper for the insertion operation, handling edge splitting.
     *
     * @param node The current node to insert from.
     * @param word The remaining portion of the word to insert.
     */
    private void insertRec(CompressedTrieNode node, String word) {
        RobinHoodHashing.Edge parent = node.hash.search(word);

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
            parent.child.hash.search(parentSubstring).child = old;

            insertRec(parent.child, wordSubstring); // Insert new word remainder
        }
    }

    /**
     * Deletes a word from the compressed trie by marking its end-of-word flag
     * as false.
     *
     * @param word The word to delete.
     * @return {@code true} if the word was successfully found and marked for deletion,
     * {@code false} otherwise.
     */
    public boolean delete(String word) {
        return deleteRec(this.root, word);
    }

    /**
     * Recursive helper for the delete operation.
     *
     * @param node The current node to search from.
     * @param word The remaining portion of the word to match.
     * @return {@code true} if the word was deleted, {@code false} otherwise.
     */
    private boolean deleteRec(CompressedTrieNode node, String word) {
        RobinHoodHashing.Edge parent = node.hash.search(word);

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

    /**
     * Prints the full content of the compressed trie, including all stored words
     * and their associated importance (frequency).
     *
     * @param e The {@code CompressedTrie} to print.
     */
    public static void print(CompressedTrie e) {
        printRec(e.root, "");
        System.out.println();
    }

    /**
     * Recursive helper to traverse the trie and print words.
     *
     * @param e    The current node.
     * @param word The word built up so far along the path to this node.
     */
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

    /**
     * Finds the length of the common prefix between two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The length of the longest common prefix.
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
     * Given a prefix, it parses the trie until it finds the node that corresponds to that prefix.
     * Sometimes an edge may contain characters that are not in the prefix prompted (e.g. prefix = "app", edge = "appl").
     * So we need a way to get the remaining characters ("l" in this example), hence,
     * it returns a Pair of the children of that node, along with a string that contains all remaining characters from that edge.
     *
     * @param prefix the prefix to search for.
     * @return Pair of the child node and the remaining string.
     */
    private Pair<CompressedTrieNode, String> getPrefix(String prefix) {
        String search = prefix;
        String remainder = "";
        CompressedTrieNode cur = this.root;
        RobinHoodHashing.Edge edge = null;
        while (cur != null) {
            edge = cur.hash.search(search);
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

    /**
     * Retrieves the {@code k} most frequent words starting with the given prefix.
     *
     * @param prefix The starting prefix.
     * @param k      The maximum number of words to return. If k is non-positive (-1 in the implementation),
     *               all words with the prefix are returned.
     * @return A {@link MinHeap} containing {@link DictionaryWord} objects (word and frequency).
     */
    public MinHeap getWordsWithPrefix(String prefix, int k) {
        Pair<CompressedTrieNode, String> cur = getPrefix(prefix);

        MinHeap heap = new MinHeap(k);
        getWordsRec(cur.getLeft(), prefix + cur.getRight(), heap);

        return heap;
    }

    /**
     * Recursive helper to traverse the subtree and collect all words into the min-heap.
     *
     * @param node The current node to traverse from.
     * @param word The word built up so far (including the prefix).
     * @param heap The {@link MinHeap} to store the most frequent words.
     */
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

    /**
     * Predicts the next letter based on the highest average frequency of words
     * that follow the prefix.
     *
     * @param prefix The current input prefix.
     * @return The predicted next character, or '\0' if no prediction can be made.
     */
    public char predictNextLetter(String prefix) {
        Pair<CompressedTrieNode, String> pair = getPrefix(prefix);
        if (pair.getLeft() == null)
            return '\0';
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

    /**
     * A simple testing method demonstrating the functionality of the {@code CompressedTrie}.
     *
     * @param args Command line arguments (not used).
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