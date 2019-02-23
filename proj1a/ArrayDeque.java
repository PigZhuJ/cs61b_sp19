/**
 * @author Ziyi Yan cxfyzy@gmail.com
 *         Created on 04/07/2017.
 */
public class ArrayDeque<Item> implements Deque<Item> {
    private Item[] items;
    private int nextFirst;
    private int nextLast;
    private int size;
    private static final int INITIAL_CAPACITY = 8;

    public ArrayDeque() {
        items = (Item[]) new Object[INITIAL_CAPACITY];
        nextFirst = 0;
        nextLast = 1;
        size = 0;
    }

    @Override
    public void addFirst(Item item) {
        if (isFull()) {
            extend();
        }
        items[nextFirst] = item;
        nextFirst = minusOne(nextFirst);
        size++;
    }

    @Override
    public void addLast(Item item) {
        if (isFull()) {
            extend();
        }
        items[nextLast] = item;
        nextLast = plusOne(nextLast);
        size++;
    }

    private void extend() {
        resize(2 * size());
    }

    @Override
    public boolean isEmpty() {
        return minusOne(nextLast) == nextFirst;
    }


    @Override
    public int size() {
        return size;
    }

    public int capacity() {
        return items.length;
    }

    @Override
    public Item removeFirst() {
        if (isSparse()) {
            shrink();
        }
        nextFirst = plusOne(nextFirst);
        Item toRemove = items[nextFirst];
        items[nextFirst] = null;
        size--;
        return toRemove;
    }

    @Override
    public Item removeLast() {
        if (isSparse()) {
            shrink();
        }
        nextLast = minusOne(nextLast);
        Item toRemove = items[nextLast];
        items[nextLast] = null;
        size--;
        return toRemove;
    }

    private void shrink() {
        resize(items.length / 2);
    }

    private void resize(int target) {
        System.out.printf("Resizing from %5d to %5d\n", items.length, target);
        // TODO: refactor resize to smaller functions.
        Item[] oldItems = items;
        int oldFirst = plusOne(nextFirst);
        int oldLast = minusOne(nextLast);

        items = (Item[]) new Object[target];
        nextFirst = 0;
        nextLast = 1;
        for (int i = oldFirst; i != oldLast; i = plusOne(i, oldItems.length)) {
            items[nextLast] = oldItems[i];
            nextLast = plusOne(nextLast);
        }
        items[nextLast] = oldItems[oldLast];
        nextLast = plusOne(nextLast);
    }

    @Override
    public Item get(int index) {
        return items[plusIndex(nextFirst + 1, index)];
    }

    @Override
    public void printDeque() {
        for (int i = plusOne(nextFirst); i != nextLast; i = plusOne(i)) {
            System.out.print(items[i] + " ");
        }
    }

    private int minusOne(int index) {
        return Math.floorMod(index - 1, items.length);
    }

    private int plusOne(int index) {
        return Math.floorMod(index + 1, items.length);
    }

    private int plusOne(int index, int size) {
        return Math.floorMod(index + 1, size);
    }

    private int plusIndex(int index, int offset) {
        return Math.floorMod(index + offset, items.length);
    }

    private boolean isFull() {
        return size() == items.length;
    }

    public boolean isSparse() {
        return items.length >= 16 && size() < items.length / 4;
    }
}
