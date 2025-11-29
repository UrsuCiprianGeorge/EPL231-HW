package omadiki.list_trie;

/**
 * A basic Singly Linked List implementation used by {@code CompressedTrie}
 * to manage the outgoing edges from a single trie node.
 * <p>
 * This list stores {@code Edge} objects, where each edge label's first character
 * is used for efficient lookup.
 */
public class SinglyLinkedList {

	/**
	 * Represents a single node within the {@code SinglyLinkedList}.
	 * Each node holds a reference to an {@code Edge} and the next node in the list.
	 */
	protected static class Node {
		/** The edge data stored in this node. */
		Edge edge;
		/** Reference to the next node in the list. */
		Node next;

		/**
		 * Constructs a new Node holding the specified Edge.
		 *
		 * @param e The Edge object to store in this node.
		 */
		public Node(Edge e) {
			this.edge = e;
		}
	}

	/**
	 * Represents a compressed trie edge, which holds a string label
	 * (the compressed path segment) and a pointer to the child trie node.
	 */
	protected static class Edge {
		/** The string label of the edge, which can be one or more characters. */
		String label;
		/** The child node that this edge points to. */
		CompressedTrie.CompressedTrieNode child;

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

	/** The starting point of the list. */
	private Node head;

	/**
	 * Constructs an empty SinglyLinkedList.
	 */
	public SinglyLinkedList() {
		this.head = null;
	}

	/**
	 * Retrieves the head node of the list.
	 *
	 * @return The first {@code Node} in the list, or {@code null} if the list is empty.
	 */
	Node getHead() {
		return head;
	}

	/**
	 * Inserts a new {@code Edge} at the end of the list.
	 *
	 * @param edge The edge to be inserted.
	 */
    void insert(Edge edge) {
		Node n = new Node(edge);
		if (this.head == null) {
			this.head = n;
			return;
		}
		
		Node cur = this.head;
		while (cur.next != null) {
			cur = cur.next;
		}
		
		cur.next = n;
	}

	/**
	 * Searches the list for an edge whose label starts with the given character.
	 * <p>
	 * This is the primary lookup mechanism for moving down the trie.
	 *
	 * @param c The character to check against the first character of each edge label.
	 * @return The matching {@code Edge}, or {@code null} if no edge starts with the character {@code c}.
	 */
	Edge getEdge(char c) {
        Node cur = this.head;
        while (cur != null) {
            if (cur.edge.label.charAt(0) == c)
                return cur.edge;
            cur = cur.next;
        }

        return null;
	}
}
