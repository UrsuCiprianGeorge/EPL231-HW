package omadiki;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import omadiki.robin.CompressedTrie;

/**
 * Provides the command-line interface (CLI) for the Compressed Trie application.
 * It presents a menu of operations to the user, primarily utilizing the trie's
 * prefix-based frequency and prediction capabilities.
 * <p>
 * NOTE: The original code uses a new {@code Scanner} object inside the loop
 * which can cause issues with input buffering.
 */
public final class Menu {

    /**
     * Starts the main interactive menu loop for the Compressed Trie application.
     * Continuously prompts the user for an action until the exit command (0) is given.
     *
     * @param trie The initialized {@code CompressedTrie} instance containing words and frequencies.
     */
    public static void startMenu(CompressedTrie trie) {
        Scanner sc = new Scanner(System.in);
        int func;

        do {
            System.out.println("Welcome. Enter one of the numbers below to continue: ");
            System.out.println("0 - Exit");
            System.out.println("1 - Top K frequent words with prefix");
            System.out.println("2 - Average frequency of prefix");
            System.out.println("3 - Predict next letter");

            func = sc.nextInt();
            if (func == 0)
                break;
            System.out.println("Enter a prefix: ");
            String prefix = sc.next();

            switch (func) {
                case 1:
                    topKFrequentWordsWithPrefix(trie, prefix, sc);
                    break;
                case 2:
                    getAverageFrequencyOfPrefix(trie, prefix);
                    break;
                case 3:
                    predictNextLetter(trie, prefix);
                    break;
            }
        } while (true);

    }

    /**
     * Executes the "Top K frequent words with prefix" operation.
     * Prompts the user for the value of K, retrieves the words, and prints them.
     *
     * @param trie The {@code CompressedTrie} instance.
     * @param prefix The prefix string provided by the user.
     * @param sc The shared {@code Scanner} instance for input.
     */
    private static void topKFrequentWordsWithPrefix(CompressedTrie trie, String prefix, Scanner sc) {
        int k;
        System.out.println("Give k:");
        k = sc.nextInt();
        MinHeap heap=trie.getWordsWithPrefix(prefix, k);

        DictionaryWord[] a = heap.getSorted();
        for (DictionaryWord b : a)
            System.out.println(b.getWord());
    }

    /**
     * Executes the "Average frequency of prefix" operation.
     * Calculates the average importance of all words found under the given prefix.
     *
     * @param trie The {@code CompressedTrie} instance.
     * @param prefix The prefix string provided by the user.
     */
    private static void getAverageFrequencyOfPrefix(CompressedTrie trie, String prefix) {
        MinHeap heap = trie.getWordsWithPrefix(prefix, -1);
        System.out.println("Average frequency of prefix: " + heap.getAvgFrequency());
    }

    /**
     * Executes the "Predict next letter" operation.
     * Finds the most likely next character based on the average frequency of all
     * words that could follow each possible next edge.
     *
     * @param trie The {@code CompressedTrie} instance.
     * @param prefix The prefix string provided by the user.
     */
    private static void predictNextLetter(CompressedTrie trie, String prefix) {
        char l = trie.predictNextLetter(prefix);
        if (l == 0)
            System.out.println("No larger words exist");
        else
            System.out.println("Predict next letter: " + l);
    }

}
