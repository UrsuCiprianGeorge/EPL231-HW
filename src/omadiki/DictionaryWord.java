package omadiki;

/**
 * Represents a word in the dictionary along with its importance or frequency.
 * This class is used to store and compare words based on their usage metrics.
 */
public class DictionaryWord {

    /** The string representation of the word. */
    private String word;

    /** The importance or frequency count of the word. */
    private int importance;

    /**
     * Constructs a new DictionaryWord with the specified word and importance.
     *
     * @param word       The word string.
     * @param importance The importance or frequency of the word.
     */
    public DictionaryWord(String word, int importance) {
        this.word = word;
        this.importance = importance;
    }

    /**
     * Retrieves the word string.
     *
     * @return The word.
     */
    public String getWord() {
        return word;
    }

    /**
     * Retrieves the importance of the word.
     *
     * @return The importance value.
     */
    public int getImportance() {
        return importance;
    }

    /**
     * Compares this DictionaryWord with another based on importance.
     * If importance is equal, compares lexicographically by the word string.
     *
     * @param other The other DictionaryWord to compare against.
     * @return A negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object.
     */
    public int compare(DictionaryWord other) {
        if (this.importance - other.importance == 0) {
            return this.word.compareTo(other.word);
        }
        return this.importance - other.importance;
    }

    /**
     * Returns a string representation of the DictionaryWord.
     * Format: "word:importance"
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return this.word + ":" + this.importance;
    }
}