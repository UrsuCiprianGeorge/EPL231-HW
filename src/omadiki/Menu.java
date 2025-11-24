package omadiki;

import java.util.Scanner;
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
            System.out.println("Enter a prefix: ");
            String prefix = sc.next();

            switch (func) {
                case 1: topKFrequentWordsWithPrefix(trie, prefix); break;
                case 2: getAverageFrequencyOfPrefix(trie, prefix); break;
                case 3: predictNextLetter(trie, prefix); break;
            }
        } while (func != 0);

    }


    private static void topKFrequentWordsWithPrefix(CompressedTrie trie, String prefix,int k) {
      
        
    }

    private static void getAverageFrequencyOfPrefix(CompressedTrie trie, String prefix) {

    }

    private static void predictNextLetter(CompressedTrie trie, String prefix) {

    }



}
