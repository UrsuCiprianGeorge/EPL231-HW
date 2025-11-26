import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

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
                wordlengths[size-1]++;
                for (int i = 0; i < size; i++) {
                    letter[size-1][line.charAt(i)-'a']++;
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
