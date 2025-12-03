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
    private static final Random rnd = new Random();

    public static void main(String[] args) {
        File wordsFile = new File(args[0]);
        if (!wordsFile.exists()) {
            System.out.println("File " + wordsFile.getAbsolutePath() + " does not exist.");
            System.exit(1);
        }


        int[] wordlengths = new int[45];
        int[][] letter = new int[45][26];

        int lines = 0;

        try (BufferedReader reader = Files.newBufferedReader(Path.of(wordsFile.toURI()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty() || line.matches(".*[^A-Za-z].*")) {
                    continue;
                }
                line = line.toLowerCase();

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

        int words[]={5000,10000,25000,50000,75000,100000,150000,200000,250000,500000,750000,1000000};
        for(int i=0;i<words.length;i++) {
            CompressedTrie compressedTrie = new CompressedTrie();
            Trie trie = new Trie();
        switch (tog) {
            case 1:
                //System.out.println("Enter Word length: ");
                //int len = sc.nextInt();
               // System.out.println("Enter how many words to be generated: ");
                //int num = sc.nextInt();
                random_dictionary_generator_fixed(wordlengths, letter, 9, words[i],trie);

               // System.out.println("Bytes Used:" + compressedTrie.getTotalMemory(compressedTrie.root));
                System.out.println(trie.getTotalMemory(trie.head));
                break;
            case 2:
               // System.out.println("Enter how many words to be generated: ");
               // int num2 = sc.nextInt();
                random_dictionary_generator(wordlengths, letter, words[i],trie);
                //System.out.println("Bytes Used:" + compressedTrie.getTotalMemory(compressedTrie.root));
                System.out.println(trie.getTotalMemory(trie.head));
                break;
        }

        }

    }

    public static void random_dictionary_generator_fixed(int[] wordlength, int[][] letter, int length, int dictSize, Trie compressedTrie) {
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
            //System.out.println(sb.toString());
            sb = new StringBuilder();
        }
    }

    public static int sampleLengthGaussian(double mu, double sigma, int minLen, int maxLen) {
        double u1 = rnd.nextDouble();
        double u2 = rnd.nextDouble();
        double z = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);

        int len = (int) Math.round(mu + sigma * z);

        if (len < minLen) len = minLen;
        if (len > maxLen) len = maxLen;

        return len;
    }


    public static void random_dictionary_generator(int[] wordlength, int[][] letter, int dictSize, Trie compressedTrie) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        int sum1 = 0;
        for(int i = 0; i < wordlength.length; i++) {
            sum1 += wordlength[i];
        }
        double sum = 0;
        for (int i = 0 ; i <wordlength.length;i++) sum += i*(double)wordlength[i]/sum1;
        double mean = sum ;
        double sigma=0;
        for(int i = 0; i < wordlength.length; i++) {
            sigma=Math.pow(wordlength[i]-mean,2);
        }
        sigma=sigma/wordlength.length-1;
        //System.out.println(mean);
       // System.out.println(sigma);

        for (int i = 0; i < dictSize; i++) {

            int length = sampleLengthGaussian(mean, sigma, 1, 45);

            for (int k = 0; k < length; k++) {

                int kk = Math.min(k, letter.length - 1);  // <-- SAFETY FIX

                int weightSum = 0;
                for (int j = 0; j < 26; j++) {
                    weightSum += letter[kk][j];
                }

                if (weightSum <= 0) {  // <-- secondary safety fix
                    sb.append((char) ('a' + rand.nextInt(26)));
                    continue;
                }

                int rnd = rand.nextInt(weightSum);

                int j;
                for (j = 0; j < 26; j++) {
                    if (rnd < letter[kk][j]) break;
                    rnd -= letter[kk][j];
                }

                sb.append((char) (j + 'a'));
            }

            compressedTrie.insert(sb.toString());
            //System.out.println(sb.toString());
            sb.setLength(0);
        }
    }


}
//
//entered
//        entered
//entered
//        entered
//entered
//        entered
//entered
//        entered
//entered
//        entered
//entered
//        entered
//entered
//        entered