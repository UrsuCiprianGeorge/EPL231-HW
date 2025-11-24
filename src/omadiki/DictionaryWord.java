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

    int compare(DictionaryWord word2) {
        if (this.importance - word2.importance == 0) {
            return this.word.compareTo(word2.word);
        }
        return this.importance - word2.importance;
    }


    @Override
    public String toString() {
        return this.word + ":" + this.importance;
    }
}
