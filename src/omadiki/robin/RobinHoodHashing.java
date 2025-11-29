package omadiki.robin;

/**
 * Implements a Robin Hood Hashing open addressing hash map.
 * This class is designed to store {@code Edge} objects (which represent trie
 * path segments) and uses Robin Hood insertion to minimize variance in probe lengths.
 * <p>
 * The hashing mechanism assumes labels start with lowercase English letters
 * and uses the first character for hash calculation.
 */
public class RobinHoodHashing {

    /** A sequence of prime numbers used for resizing the hash table (rehash operation). */
    private static int[] PRIMES = {3, 7, 11, 17, 23, 29};

    /**
     * Represents a compressed trie edge, which holds a string label
     * (the compressed path segment) and a pointer to the child trie node.
     * This structure is shared with the list-based trie implementation but
     * includes an {@code occupied} flag for deletion handling (although deletion
     * is not fully implemented here).
     */
    protected static class Edge {
        /** The string label of the edge, which can be one or more characters. */
        String label;
        /** The child node that this edge points to. */
        CompressedTrie.CompressedTrieNode child;
        /** Flag indicating if the slot is currently occupied by a valid entry. */
        boolean occupied = true;

        /**
         * Constructs a new Edge with the specified label and child node.
         *
         * @param label The compressed string segment for this edge.
         * @param child The destination node in the trie.
         */
        public Edge(String label, CompressedTrie.CompressedTrieNode child) {
            this.label = label;
            this.child = child;
        }
    }

    /** The array representing the hash table. */
    Edge[] table;
    /** The current capacity of the hash table. */
    int capacity;
    /** The number of elements currently stored in the hash table. */
    int size;
    /** The maximum probe length observed (distance from home slot). */
    int maxProbeLength;

    /**
     * Constructs a new RobinHoodHashing table with a small initial capacity (3).
     */
    public RobinHoodHashing() {
        this.capacity = 3;
        table = new Edge[this.capacity];
        size = 0;
        maxProbeLength = 0;
    }

    /**
     * Calculates the hash for a given string based on its first character.
     * Assumes the first character is a lowercase English letter ('a' to 'z').
     *
     * @param s The string (usually the edge label) to hash.
     * @return The initial hash table index.
     */
    private int hash(String s) {
        return (s.charAt(0) - 'a') % capacity;
    }

    /**
     * Retrieves an edge whose label starts with the same character as the input string.
     * Searches starting from the home index and continues up to {@code maxProbeLength}.
     *
     * @param s The string whose first character is used for searching.
     * @return The matching {@code Edge}, or {@code null} if not found.
     */
    Edge getEdge(String s) {
        int index = hash(s);
        Edge e = table[index];
        if (e == null) return null;
        // Check home slot for immediate match
        if (e.label.charAt(0) == s.charAt(0)) return e;

        // Linear probing
        for (int i = index; circularDiff(i, index) <= maxProbeLength; index = (index + 1) % capacity) {
            if (table[index] == null) break;
            if (!table[index].occupied) continue;
            if (table[index].label.charAt(0) == s.charAt(0)) return table[index];
        }

        return null;
    }

    /**
     * Inserts an edge into the hash table using the Robin Hood Hashing strategy.
     * This strategy ensures that if the incoming element has a longer probe length
     * than the element currently at the slot, the two are swapped.
     *
     * @param edge The edge to insert.
     */
    void insert(Edge edge) {
        if (edge == null) {
            return;
        }

        int index = hash(edge.label);
        Edge cur = table[index];
        int curProbeLength = cur == null ? 0 : circularDiff(hash(cur.label), index);
        int probeLength = 0;
        while (cur != null) {
            // Robin Hood Check
            if (probeLength > curProbeLength) {
                table[index] = edge;
                edge = cur;
                probeLength = curProbeLength;
            }

            index = (index+1) % capacity;
            probeLength++;
            cur = table[index];
            curProbeLength = cur == null ? 0 : circularDiff(hash(cur.label), index);
        }

        // Slot found (cur == null), place the current edge (which may be the original or a displaced one)
        if (probeLength > maxProbeLength) {
            maxProbeLength = probeLength;
        }

        table[index] = edge;
        size++;

        // Trigger rehash if load factor exceeds 90%
        if (size >= (int) (capacity * 0.9f)) {
            rehash();
        }
    }

    /**
     * Calculates the circular difference between two indices, representing the
     * probe length (distance from the home index) in a circular array.
     *
     * @param a The home index (starting point).
     * @param b The current index (ending point).
     * @return The circular distance from {@code a} to {@code b}.
     */
    private int circularDiff(int a, int b) {
        return a <= b ? b-a : capacity-a+b;
    }

    /**
     * Increases the capacity of the hash table to the next prime number in the sequence
     * and re-inserts all existing elements into the new, larger table.
     */
    private void rehash() {
        int i;
        // Find the current capacity in the PRIMES array
        for (i = 0; i <= PRIMES.length; i++) {
            if (PRIMES[i] == capacity) {
                break;
            }
        }
        int oldCapacity = capacity;
        capacity = PRIMES[i+1];

        Edge[] oldTable = table;
        table = new Edge[capacity];
        maxProbeLength = 0;
        size = 0;

        for (int j = 0; j < oldCapacity; j++) {
            insert(oldTable[j]);
        }
    }

    /**
     * Main method for testing the Robin Hood Hashing implementation.
     */
    public static void main(String[] args) {
        RobinHoodHashing h = new RobinHoodHashing();
        h.insert(new Edge("gay", null));
        h.insert(new Edge("huy", null));
        h.insert(new Edge("giu", null));

        for (int i = 0; i < h.capacity; i++) {
            System.out.println(i + ": " + (h.table[i] == null ? "null" : h.table[i].label));
        }
    }
}
