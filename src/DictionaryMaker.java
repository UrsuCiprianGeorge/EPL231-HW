import omadiki.robin.CompressedTrie;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Struct;
import java.util.Random;
import java.util.Scanner;

import static omadiki.robin.CompressedTrie.print;

public class DictionaryMaker {

    public static void main(String[] args) {
        File wordsFile = new File(args[0]);
        if (!wordsFile.exists()) {
            System.out.println("File " + wordsFile.getAbsolutePath() + " does not exist.");
            System.exit(1);
        }


        int[] wordlengths = new int[31];
        int[][] letter = new int[31][26];

        int lines = 0;

        try (BufferedReader reader = Files.newBufferedReader(Path.of(wordsFile.toURI()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines++;
                int size = line.length();
                wordlengths[size - 1]++;
                for (int i = 0; i < size; i++) {
                    letter[i][line.charAt(i) - 'a']++;
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("1- Generate random Dictionary with fixed size length word");
        System.out.println("2- Generate random Dictionary with random word length");
        System.out.print("Enter Option:");
        int tog = sc.nextInt(); // type of generation
        CompressedTrie compressedTrie = new CompressedTrie();

        switch (tog) {
            case 1:
                System.out.println("Enter Word length: ");
                int len = sc.nextInt();
                System.out.println("Enter how many words to be generated: ");
                int num = sc.nextInt();
                random_dictionary_generator_fixed(wordlengths,letter,len,num,compressedTrie);
                System.out.println("Bytes Used:"+compressedTrie.getTotalMemory(compressedTrie.root));
                break;
            case 2:
                System.out.println("Enter how many words to be generated: ");
                int num2 = sc.nextInt();
                random_dictionary_generator(wordlengths,letter,num2,compressedTrie);
                System.out.println("Bytes Used:"+compressedTrie.getTotalMemory(compressedTrie.root));
                break;
        }


    }

    public static void random_dictionary_generator_fixed(int[] wordlength, int[][] letter, int length,int dictSize,CompressedTrie compressedTrie) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();

        for (int i = 0; i < dictSize; i++) {

            for (int k = 0; k < length; k++) {
                int j;
                int weightSum = 0;
                for (j = 0; j < 26; j++) {
                    weightSum += letter[k][j];
                }

                int rnd = rand.nextInt(weightSum);
                for (j = 0; j < 26; j++) {
                    if (rnd < letter[k][j]) {
                        break;
                    }
                    rnd -= letter[k][j];
                }
                sb.append((char) (j + 'a'));

            }
            compressedTrie.insert(sb.toString());
            System.out.println(sb.toString());
            sb = new StringBuilder();
        }
    }

    public static void random_dictionary_generator(int[] wordlength, int[][] letter,int dictSize,CompressedTrie compressedTrie) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();

        for (int i = 0; i < dictSize; i++) {
            int length;

            {
                int j;
                int weightSum = 0;
                for (j = 0; j < 31; j++) {
                    weightSum += wordlength[j];
                }

                int rnd = rand.nextInt(weightSum);
                for (j = 0; j < 31; j++) {
                    if (rnd < wordlength[j]) {
                        break;
                    }
                    rnd -= wordlength[j];
                }

                length = j+1;
            }

            for (int k = 0; k < length; k++) {
                int j;
                int weightSum = 0;
                for (j = 0; j < 26; j++) {
                    weightSum += letter[k][j];
                }

                int rnd = rand.nextInt(weightSum);
                for (j = 0; j < 26; j++) {
                    if (rnd < letter[k][j]) {
                        break;
                    }
                    rnd -= letter[k][j];
                }

                sb.append((char) (j + 'a'));
            }
            compressedTrie.insert(sb.toString());
            System.out.println(sb.toString());
            sb = new StringBuilder();

        }
    }


}
