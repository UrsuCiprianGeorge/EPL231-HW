package omadiki;

import omadiki.robin.CompressedTrie;

import java.io.*;
import java.util.Scanner;

/**
 * The main entry point for the application.
 * This class handles command line argument validation, file parsing for
 * dictionary words (lexicon) and text usage, initialization of the
 * {@code CompressedTrie}, and starting the user menu.
 */
public final class Main {

    /**
     * Main method that executes the application.
     * <p>
     * Requires exactly two command line arguments: the dictionary file path
     * and the text file path.
     *
     * @param args Command line arguments: args[0] is the dictionary file, args[1] is the text file.
     */
    public static void main(String[] args) {
        if  (args.length != 2) {
            System.err.println("Must have dictionary file and text file");
            System.exit(1);
        }

        File dic = new File(args[0]);
        if (!dic.exists()) {
            System.out.println("File " + dic.getAbsolutePath() + " does not exist.");
            System.exit(1);
        }

        File txt = new File(args[1]);
        if (!txt.exists()) {
            System.out.println("File " + args[1] + " does not exist.");
            System.exit(1);
        }

        CompressedTrie trie = new CompressedTrie();
        parseLexicon(trie, dic);
        parseTxt(trie, txt);

        CompressedTrie.print(trie);
        Menu.startMenu(trie);

    }

    /**
     * Reads a lexicon (dictionary) file, inserting each word into the Compressed Trie.
     * Words are trimmed and converted to lowercase before insertion.
     *
     * @param trie The {@code CompressedTrie} to populate.
     * @param f The {@code File} object representing the dictionary/lexicon file.
     */
    private static void parseLexicon(CompressedTrie trie, File f) {
        try (BufferedReader reader = new BufferedReader(new FileReader(f)))  {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                line = line.toLowerCase();
                trie.insert(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + f.getAbsolutePath());
            System.exit(1);
        }
    }

    /**
     * Reads a text file to simulate word usage frequency.
     * Each word in the text is sanitized (non-alphabetic characters removed)
     * and searched in the trie. Successful searches increment the word's importance counter.
     *
     * @param trie The {@code CompressedTrie} used to track word frequency.
     * @param f The {@code File} object representing the text file.
     */
    private static void parseTxt(CompressedTrie trie, File f) {
        try (Scanner sc = new Scanner(f))  {
            String word;
            while (sc.hasNext()) {
                word = sc.next();
                String[] words = word.split("[^A-Za-z]+");
                for (String w : words) {
                    w = w.toLowerCase();
                    trie.search(w);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading file " + f.getAbsolutePath());
            System.exit(1);
        }
    }
}