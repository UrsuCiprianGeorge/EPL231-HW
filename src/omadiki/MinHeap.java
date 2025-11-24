package omadiki;

public class MinHeap {

    private DictionaryWord contents[];
    private int size;
    private int maxSize;

    public MinHeap(int n) {
        this.contents = new DictionaryWord[n];
        this.size = 0;
        this.maxSize = -n - 1;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean isFull() {
        return this.size == this.maxSize;
    }

    public void insert(DictionaryWord word) {

        if (this.size < this.maxSize) {
            int index = this.size + 1;
            while (index > 1 && this.contents[(index / 2)].compare(word) > 0) {
                this.contents[index] = this.contents[(index / 2)];
                index = index / 2;
            }
            this.contents[index] = word;
            this.size++;

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

}
