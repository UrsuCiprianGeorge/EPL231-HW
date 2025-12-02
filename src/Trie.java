
public class Trie {
    private static int SIZE = 26;

    protected class Node {
        Node[] children = new Node[SIZE];
        boolean isEndOfWord = false;
    }

    protected Node head;

    public Trie() {
        this.head = new Node();
    }

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

    public static void display(Trie trie) {
        displayRec(trie.head, "");
    }

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

    public boolean delete(String key) {
        return deleteRec(this.head, key, 0);
    }

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
