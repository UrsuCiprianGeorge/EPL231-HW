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


        int[] wordlengths = new int[34];
        int[][] letter;



        try (BufferedReader reader = Files.newBufferedReader(Path.of(wordsFile.toURI()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                String col1 = parts[0];
                long col2 = Long.parseLong(parts[1]);








            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
