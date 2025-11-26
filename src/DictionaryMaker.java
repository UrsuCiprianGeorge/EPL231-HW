import java.io.File;

public class DictionaryMaker {

    public static void main(String[] args) {
        File wordsFile = new File(args[0]);
        if (!wordsFile.exists()) {
            System.out.println("File " + wordsFile.getAbsolutePath() + " does not exist.");
            System.exit(1);
        }


    }
}
