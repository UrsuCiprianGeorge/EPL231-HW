package omadiki;

import java.util.Arrays;

public class MinHeap {

    private DictionaryWord contents[];
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

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean isFull() {
        return this.size == this.capacity;
    }

    public void insert(DictionaryWord word) {

        if (this.size < this.capacity) {
            int index = this.size + 1;
            while (index > 1 && this.contents[(index / 2)].compare(word) > 0) {
                this.contents[index] = this.contents[(index / 2)];
                index = index / 2;
            }

            this.contents[index] = word;
            this.size++;
            if (this.resize && size == capacity-1) {
                incrementContents();
            }
            if (!this.resize && size == capacity) {
                this.deleteMin();
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

    void incrementContents() {
        this.capacity = this.capacity * 2;
        this.contents = Arrays.copyOf(this.contents, this.capacity);
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
}
