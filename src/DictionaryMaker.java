import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

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

        random_dictionary_generator(wordlengths, letter);

    }

    public static void random_dictionary_generator_fixed(int[] wordlength, int[][] letter, int length) {

        Random rand = new Random();

        for (int i = 0; i < 100; i++) {

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

                System.out.print((char) (j + 'a'));
            }
            System.out.println();
        }
    }

    public static void random_dictionary_generator(int[] wordlength, int[][] letter) {
        Random rand = new Random();

        for (int i = 0; i < 10000; i++) {
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

                length = j;
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

                //System.out.print((char) (j + 'a'));
            }
            //System.out.println();
        }
    }
}
