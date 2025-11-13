package omadiki;

import omadiki.CompressedTrie.CompressedTrieNode;

public class SinglyLinkedList {
	protected static class Node {
		Edge edge;
		Node next;
		
		public Node(Edge e) {
			this.edge = e;
		}
	}
	
	protected static class Edge {
		String label;
		CompressedTrie.CompressedTrieNode child;
		
		
		public Edge(String label, CompressedTrie.CompressedTrieNode child) {
			this.label = label;
			this.child = child;
		}
	}
	
	private Node head;
	
	public SinglyLinkedList() {
		this.head = null;
	}
	
	public Node getHead() {
		return head;
	}
	
	public boolean isEmpty() {
		return head == null;
	}
	
	public void insert(Edge edge) {
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
	
	public Edge getEdge(char c) {
        Node cur = this.head;
        while (cur != null) {
            if (cur.edge.label.charAt(0) == c)
                return cur.edge;
            cur = cur.next;
        }

        return null;
	}
}
