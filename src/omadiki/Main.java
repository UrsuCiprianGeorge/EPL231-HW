package omadiki;

import omadiki.robin.CompressedTrie;

import java.io.*;
import java.util.Scanner;

public final class Main {

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