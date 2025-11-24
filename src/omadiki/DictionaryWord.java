package omadiki;

public class DictionaryWord {
    private String word;
    private int importance;

    public DictionaryWord(String word, int importance) {
        this.word = word;
        this.importance = importance;
    }

    public String getWord() {
        return word;
    }
    public int getImportance() {
        return importance;
    }
}
