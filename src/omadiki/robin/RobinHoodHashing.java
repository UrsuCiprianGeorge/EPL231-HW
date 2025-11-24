package omadiki.robin;

public class RobinHoodHashing {

    private static int[] PRIMES = {3, 7, 11, 17, 23, 29};

    protected static class Edge {
        String label;
        CompressedTrie.CompressedTrieNode child;


        public Edge(String label, CompressedTrie.CompressedTrieNode child) {
            this.label = label;
            this.child = child;
        }
    }

    Edge[] table;
    int capacity, size;
    int maxProbeLength;


    public RobinHoodHashing() {
        this.capacity = 3;
        table = new Edge[this.capacity];
        size = 0;
        maxProbeLength = 0;
    }

    private int hash(String s) {
        return (s.charAt(0) - 'a') % capacity;
    }

    public Edge getEdge(String s) {
        return table[hash(s)];
    }

    public void insert(Edge edge) { // probelngth is index - hash (calculated circularly)
        if (edge == null) {
            return;
        }

        int index = hash(edge.label);
        Edge cur = table[index];
        int curProbeLength = cur == null ? 0 : circularDiff(hash(cur.label), index);
        int probeLength = 0;
        while (cur != null) {
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

        if (probeLength > maxProbeLength) {
            maxProbeLength = probeLength;
        }

        table[index] = edge;
        size++;

        if (size >= (int) (capacity * 0.9f)) {
            rehash();
        }
    }

    private int circularDiff(int a, int b) {
        return a <= b ? b-a : capacity-a+b;
    }

    public void rehash() {
        int i;
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
