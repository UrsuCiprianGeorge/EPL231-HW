package omadiki;

/**
 * A generic class representing a simple immutable pair of two objects,
 * referred to as 'left' and 'right'.
 *
 * @param <L> The type of the left element.
 * @param <R> The type of the right element.
 */
public class Pair<L, R> {
    /** The left element of the pair. */
    private final L left;
    /** The right element of the pair. */
    private final R right;

    /**
     * Constructs a new Pair with the specified left and right elements.
     *
     * @param left The element to be stored as the left component.
     * @param right The element to be stored as the right component.
     */
    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Retrieves the left element of this pair.
     *
     * @return The left element of type L.
     */
    public L getLeft() {
        return left;
    }

    /**
     * Retrieves the right element of this pair.
     *
     * @return The right element of type R.
     */
    public R getRight() {
        return right;
    }
}
