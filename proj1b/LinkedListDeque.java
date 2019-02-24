/**
 * LinkedListDeque
 */

public class LinkedListDeque<T> implements Deque<T> {

    private class TNode {
        private T item;
        private TNode prev;
        private TNode next;

        private TNode(T x, TNode p, TNode n) {
            item = x;
            prev = p;
            next = n;
        }
    }

    /**
     *  The first item (if it exists) in the deque is the sentinel.next
     */
    private TNode sentinel;
    private int size;

    /**
     * Create an empty deque
     */
    public LinkedListDeque() {
        sentinel = new TNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    /**
     * Return the number of items in the deque
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Return true if deque is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Add an item of type T to the front of the deque
     */
    @Override
    public void addFirst(T item) {
        sentinel.next = new TNode(item, sentinel, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }

    /**
     * Add an item of type T to the back of the deque
     */
    @Override
    public void addLast(T item) {
        sentinel.prev = new TNode(item, sentinel.prev, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }

    /**
     * Remove and return the item at the front of the deque
     * If no such item exists, return null
     */
    @Override
    public T removeFirst() {
        T toRemove = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        if (!isEmpty()) {
            size -= 1;
        }
        return toRemove;

    }

    /**
     * Remove and return the item at the back of the deque
     * If no such item exists, return null
     */
    @Override
    public T removeLast() {
        T toRemove = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        if (!isEmpty()) {
            size -= 1;
        }
        return toRemove;
    }

    /**
     * Print the items in the deque from first to last, separated by a space
     * Once all the items have been printed, print out a new line
     */
    @Override
    public void printDeque() {
        TNode toPrint = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(toPrint.item + " ");
            toPrint = toPrint.next;
        }
        System.out.println();
    }

    /**
     * Get the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth. I fno such item exists,
     * return null. Must not alter the deque
     */
    @Override
    public T get(int index) {
        TNode toGet = sentinel.next;
        for (int i = 0; i < index; i++) {
            toGet = toGet.next;
        }
        return toGet.item;
    }

    /**
     * Same as get, but uses recursion
     * First, need a private helper method
     */
    private T getRecursive(int index, TNode curr) {

        if (index == 0) {
            return curr.item;
        }
        return getRecursive(index - 1, curr.next);
    }

    public T getRecursive(int index) {
        return getRecursive(index, sentinel.next);
    }

    /**
     * Create a deep copy of other
     */
    public LinkedListDeque(LinkedListDeque other) {
        sentinel = new TNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;

        for (int i = 0; i < other.size(); i++) {
            addLast((T) other.get(i)); // (T) is cast, since type of other is unknown
        }
    }

}


























