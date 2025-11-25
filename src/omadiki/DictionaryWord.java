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

    public int compare(DictionaryWord other) {
        if (this.importance - other.importance == 0) {
            return this.word.compareTo(other.word);
        }
        return this.importance - other.importance;
    }

    @Override
    public String toString() {
        return this.word + ":" + this.importance;
    }
}
