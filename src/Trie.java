/**
 * A simple Trie implementation supporting insertion,
 * search, deletion, display, and memory estimation.
 */
public class Trie {
    /** Number of possible child nodes (a-z). */
    private static int SIZE = 26;

    /**
     * Represents a single Trie node storing child pointers and
     * a flag indicating whether the node terminates a valid word.
     */
    protected class Node {
        /** Array of child nodes indexed by character. */
        Node[] children = new Node[SIZE];
        /** Flag to indicate that this node represents the end of a word. */
        boolean isEndOfWord = false;
    }

    /** Root node of the Trie. */
    protected Node head;

    /**
     * Constructs an empty Trie.
     */
    public Trie() {
        this.head = new Node();
    }

    /**
     * Estimates the total memory used by the Trie starting from a given node.
     * <p>
     * Note that this is not an accurate JVM memory measurement but rather
     * a conceptual approximation based on assumed byte sizes.
     *
     * @param node the node to start memory calculation from
     * @return an estimated number of bytes used by this subtree
     */
    public long getTotalMemory(Trie.Node node){

        if(node == null) return 0;
        long sum=0;
        sum+=1; // 1 byte isEndOfWord
        sum+=4; // pointer to array
        sum+=26*4; //sizeof children
        for(int i=0; i<SIZE; i++) {
            sum += getTotalMemory(node.children[i]);
        }
        return sum;

    }

    /**
     * Inserts a string into the Trie.
     *
     * @param s the string to insert
     */
    public void insert(String s) {

        int c;
        Node n = this.head;
        for (int i = 0; i < s.length(); i++) {
            c = (int)s.charAt(i) - 'a';

            Node tmp = n.children[c];
            if (tmp == null) {
                n.children[c] = new Node();
            }
            n = n.children[c];
        }

        n.isEndOfWord = true;
    }

    /**
     * Searches the Trie for a given string.
     *
     * @param s the string to search
     * @return true if the string exists in the Trie; false otherwise
     */
    public boolean search(String s) {
        int c;
        Node n = this.head;
        for (int i = 0; i < s.length(); i++) {
            c = (int)s.charAt(i) - 'a';

            if (n.children[c] != null) {
                n = n.children[c];
            } else {
                return false;
            }
        }

        return n.isEndOfWord;
    }

    /**
     * Displays all words stored in the Trie.
     *
     * @param trie the Trie to display
     */
    public static void display(Trie trie) {
        displayRec(trie.head, "");
    }

    /**
     * Recursive helper to display words stored in a Trie.
     *
     * @param n current node
     * @param s current accumulated prefix
     * @return the prefix if this node ends a word; otherwise an empty string
     */
    private static String displayRec(Node n, String s) {
        if (n == null)
            return "";

        if (n.isEndOfWord) {
            System.out.println(s);
        }

        for (int i = 0; i < SIZE; i++) {
            if (n.children[i] == null)
                continue;

            displayRec(n.children[i], s + (char)(i + 'a'));
        }

        if (!n.isEndOfWord)
            return "";
        return s;
    }

    /**
     * Deletes a key from the Trie.
     * <p>
     * Note: This implementation appears incomplete and may not fully remove
     * all unused nodes.
     *
     * @param key the string to delete
     * @return true if deletion was successful; false otherwise
     */
    public boolean delete(String key) {
        return deleteRec(this.head, key, 0);
    }

    /**
     * Recursive helper for deleting a key from the Trie.
     *
     * @param n current node
     * @param key the string to delete
     * @param depth current depth into the key
     * @return true if successfully deleted; false otherwise
     */
    private boolean deleteRec(Node n, String key, int depth) {
        if (n == null)
            return false;

        if (!(n.isEndOfWord && depth == key.length())) {
            return false;
        }

        boolean f = deleteRec(n.children[key.charAt(depth+1)], key, depth+1);

        n.isEndOfWord = false;

        Node ch = n.children[key.charAt(depth)].children[key.charAt(depth+1)];
        boolean hasChildren = false;
        for (int i = 0; i < SIZE; i++) {
            if (ch.children[i] != null) {
                hasChildren = true;
                break;
            }
        }

        if (!hasChildren) {

        }

        return true;
    }

    /**
     * Simple test routine.
     */
    public static void main(String[] args) {
        var a = new Trie();

        a.insert("cat");
        a.insert("cata");
        a.insert("dog");
        a.insert("doga");
        System.out.println(a.search("cata"));

        display(a);

        a.delete("cat");
        display(a);
    }
}
