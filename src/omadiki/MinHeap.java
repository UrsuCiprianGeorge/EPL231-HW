package omadiki;

import java.util.Arrays;

/**
 * Implements a Min-Heap data structure optimized for storing and retrieving
 * the top N elements (words with the highest 'importance' frequency).
 * <p>
 * The heap property is based on the 'importance' of the {@code DictionaryWord}
 * objects, where the root holds the word with the minimum importance.
 * This class supports both a fixed-capacity top-K mode and a dynamic-capacity mode.
 */
public class MinHeap {

    /** The array representing the heap, using 1-based indexing. */
    private DictionaryWord[] contents;
    /** The current number of elements in the heap. */
    private int size;
    /** The maximum number of elements the current array can hold (array length - 1). */
    private int capacity;
    /** Flag indicating whether the heap should dynamically resize (true) or operate in fixed-capacity/top-K mode (false). */
    private boolean resize;

    /**
     * Constructs a MinHeap.
     *
     * @param n The maximum desired size (K). If {@code n} is -1, the heap operates
     * in dynamic-capacity (resizable) mode.
     */
    public MinHeap(int n) {
        if (n == -1) {
            n = 1;
            resize = true;
        }
        this.contents = new DictionaryWord[n + 1];
        this.capacity = n + 1;
        this.size = 0;
    }

    /**
     * Checks if the heap is empty.
     *
     * @return {@code true} if the heap contains no elements, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Inserts a word into the heap, maintaining the min-heap property.
     * <p>
     * In fixed-capacity mode (top-K), if the heap is full, the new word is
     * inserted only if its importance is greater than the current minimum (root).
     *
     * @param word The {@code DictionaryWord} to insert.
     */
    public void insert(DictionaryWord word) {

        if (this.size < this.capacity) {
            int index = this.size + 1;

            if (!this.resize && index == capacity) {
                // Fixed-capacity (Top-K) mode: Heap is full.
                if (this.contents[1].getImportance() < word.getImportance()) {
                    // Case 1: New word is more important than the minimum. Replace the minimum.
                    this.deleteMin();
                    index--; // Slot previously occupied by the minimum is now available
                } else {
                    // Case 2: New word is less important than or equal to the minimum. Do not insert.
                    return;
                }
            }

            while (index > 1 && this.contents[(index / 2)].compare(word) > 0) {
                this.contents[index] = this.contents[(index / 2)];
                index = index / 2;
            }

            this.contents[index] = word;
            this.size++;


            // If in dynamic mode, resize when the array is full
            if (this.resize && size == capacity - 1) {
                incrementContents();
            }
        }
    }

    /**
     * Increases the array capacity (doubles the size) and copies the existing elements
     * to the new, larger array. Used only when {@code resize} is true.
     */
    private void incrementContents() {
        this.capacity = this.capacity * 2;
        this.contents = Arrays.copyOf(this.contents, this.capacity);
    }

    /**
     * Removes and returns the element with the minimum importance (the root of the Min-Heap).
     *
     * @return The {@code DictionaryWord} with the minimum importance, or {@code null} if the heap is empty.
     */
    public DictionaryWord deleteMin() {
        DictionaryWord min = null, last;
        int x = 1, child = 0;
        if (!isEmpty()) {
            min = this.contents[1];
            last = this.contents[this.size];
            this.size--;
            while ((x * 2) <= this.size) {
                child = x * 2;
                if (child != this.size
                        && this.contents[child + 1].compare(this.contents[child]) < 0) {
                    child++;
                }
                if (last.compare(this.contents[child]) > 0) {
                    this.contents[x] = this.contents[child];
                    x = child;
                } else {
                    break;
                }
            }
            this.contents[x] = last;
        }
        return min;
    }

    /**
     * Calculates the average importance (frequency) of all words currently in the heap.
     *
     * @return The average importance as a float, or 0 if the heap is empty.
     */
    public float getAvgFrequency() {
        int sum = 0;
        for (int i = 1; i <= this.size; i++) {
            sum += this.contents[i].getImportance();
        }
        return size == 0 ? 0 : ((float) sum) / size;
    }

    /**
     * Helper method to partition the array for the Quicksort algorithm.
     * Uses the importance value for comparison.
     *
     * @param word The array of words.
     * @param low The starting index.
     * @param high The ending index (pivot index).
     * @return The final index of the pivot element.
     */
    private int partition(DictionaryWord[] word, int low, int high) {
        DictionaryWord pivot = word[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if(word[j].compare(pivot)>0){
                i++;
                swap(word,i,j);
            }
        }

        swap(word,i+1,high);
        return i+1;
    }

    /**
     * Helper method to swap two elements in an array.
     *
     * @param word The array of words.
     * @param i The index of the first element.
     * @param j The index of the second element.
     */
    private static void swap(DictionaryWord[] word , int i , int j){
        DictionaryWord temp = word[i];
        word[i]=word[j];
        word[j]=temp;
    }

    /**
     * Sorts the given array of words in place using the Quicksort algorithm.
     *
     * @param arr The array to be sorted.
     * @param low The starting index.
     * @param high The ending index.
     */
    private void quicksort(DictionaryWord[] arr, int low, int high) {
        if (low >= high) return;

       int pivotIndex = partition(arr,low,high);

       quicksort(arr, low, pivotIndex-1);
       quicksort(arr, pivotIndex, high);
    }

    /**
     * Creates a copy of the heap's contents (excluding the 0-index) into a new array.
     *
     * @return A new array containing the heap elements in their current unsorted order.
     */
    private DictionaryWord[] getContentsCopy() {
        DictionaryWord[] n = new DictionaryWord[size];
        for (int i = 0; i < this.size; i++)
            n[i] = this.contents[i+1];
        return n;
    }

    /**
     * Retrieves all words from the heap, sorted by importance in descending order.
     * The sorting is performed on a copy of the contents and does not modify the heap structure.
     *
     * @return An array of {@code DictionaryWord} objects sorted by importance.
     */
    public DictionaryWord[] getSorted() {
        DictionaryWord[] a = getContentsCopy();
        quicksort(a, 0, a.length-1);
        return a;
    }

    /**
     * Provides a string representation of the heap, including its size and the contents
     * in their current heap order.
     *
     * @return A string representation of the MinHeap.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.size).append(", ");
        for (int i = 1; i <= this.size; i++) {
            sb.append(this.contents[i]).append(", ");
        }
        return sb.toString();
    }
}
