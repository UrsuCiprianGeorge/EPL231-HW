package omadiki;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import omadiki.robin.CompressedTrie;

public final class Menu {

    public static void startMenu(CompressedTrie trie) {
        Scanner sc = new Scanner(System.in);
        int func = 0;

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
                    topKFrequentWordsWithPrefix(trie, prefix);
                    break;
                case 2:
                    System.out.println("Average frequency of prefix: " + getAverageFrequencyOfPrefix(trie, prefix));
                    break;
                case 3:
                    predictNextLetter(trie, prefix);
                    break;
            }
        } while (func != 0);

    }

    private static void topKFrequentWordsWithPrefix(CompressedTrie trie, String prefix) {
        int k = 0;
        System.out.println("Give k:");
        Scanner sc = new Scanner(System.in);
        k = sc.nextInt();
        MinHeap heap=trie.getWordsWithPrefix(prefix, k);

        System.out.print(heap);

    }

    private static float getAverageFrequencyOfPrefix(CompressedTrie trie, String prefix) {
        MinHeap heap = trie.getWordsWithPrefix(prefix, -1);
        int size = heap.getSize();
        AtomicInteger sum = new AtomicInteger(0);
        heap.traverseNodes(dictionaryWord -> sum.addAndGet(dictionaryWord.getImportance()));
        float avgFreq = size == 0 ? 0 : ((float) sum.get()) / size;
        return avgFreq;
    }

    private static void predictNextLetter(CompressedTrie trie, String prefix) {

    }

}
