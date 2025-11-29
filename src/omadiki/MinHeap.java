package omadiki;

import java.util.Arrays;

public class MinHeap {

    private DictionaryWord[] contents;
    private int size;
    private int capacity;
    private boolean resize;

    public MinHeap(int n) {
        if (n == -1) {
            n = 1;
            resize = true;
        }
        this.contents = new DictionaryWord[n + 1];
        this.capacity = n + 1;
        this.size = 0;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }


    public boolean isFull() {
        return this.size == this.capacity;
    }

    public void insert(DictionaryWord word) {

        if (this.size < this.capacity) {
            int index = this.size + 1;
            if (!this.resize && index == capacity && this.contents[1].getImportance() < word.getImportance()) {
                this.deleteMin();
                index = index - 1;
            }
            if (!this.resize && index == capacity && this.contents[1].getImportance() > word.getImportance()) {

            } else {


                while (index > 1 && this.contents[(index / 2)].compare(word) > 0) {
                    this.contents[index] = this.contents[(index / 2)];
                    index = index / 2;
                }

                this.contents[index] = word;
                this.size++;


                if (this.resize && size == capacity - 1) {
                    incrementContents();
                }
            }


        }

    }

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

    public float getAvgFrequency() {
        int sum = 0;
        for (int i = 1; i <= this.size; i++) {
            sum += this.contents[i].getImportance();
        }
        return size == 0 ? 0 : ((float) sum) / size;
    }

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

    private static void swap(DictionaryWord[] word , int i , int j){
        DictionaryWord temp = word[i];
        word[i]=word[j];
        word[j]=temp;
    }

    private void quicksort(DictionaryWord[] arr, int low, int high) {
        if (low >= high) return;

       int pivotIndex= partition(arr,low,high);

       quicksort(arr, low, pivotIndex-1);
       quicksort(arr, pivotIndex, high);
    }

    private DictionaryWord[] getContentsCopy() {
        DictionaryWord[] n = new DictionaryWord[size];
        for (int i = 0; i < this.size; i++)
            n[i] = this.contents[i+1];
        return n;
    }

    public DictionaryWord[] getSorted() {
        DictionaryWord[] a = getContentsCopy();
        quicksort(a, 0, a.length-1);
        return a;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.size).append(", ");
        for (int i = 1; i <= this.size; i++) {
            sb.append(this.contents[i]).append(", ");
        }
        return sb.toString();
    }

    private void incrementContents() {
        this.capacity = this.capacity * 2;
        this.contents = Arrays.copyOf(this.contents, this.capacity);
    }

}
