package bearmaps;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private ArrayList<PriorityNode> itemPQ;
    private int size;

    public ArrayHeapMinPQ() {
        itemPQ =  new ArrayList<>();
        size = 0;
    }

    /* Adds an item with the given priority value. Throws an
     * IllegalArgumentException if item is already present.
     * You may assume that item is never null. */
    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        itemPQ.add(new PriorityNode(item, priority));
        size += 1;
        int currPos = size() - 1;
        swim(currPos);
    }

    /* Returns true if the PQ contains the given item. */
    @Override
    public boolean contains(T item) {
        return itemPQ.contains(new PriorityNode(item, 0));
    }

    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T getSmallest() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return itemPQ.get(0).getItem();
    }

    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T removeSmallest() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T toRemove = itemPQ.get(0).getItem();
        swap(0, size() - 1);
        size -= 1;
        sink(0);
        return toRemove;
    }

    /* Returns the number of items in the PQ. */
    @Override
    public int size() {
        return size;
    }

    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    @Override
    public void changePriority(T item, double priority) {
        if (isEmpty() || !contains(item)) {
            throw new NoSuchElementException();
        }
        int index = getIndex(item);
        double oldPriority = itemPQ.get(index).getPriority();
        itemPQ.get(getIndex(item)).setPriority(priority);
        if (Double.compare(oldPriority, priority) <= 0) {
            sink(index);
        } else {
            swim(index);
        }
    }

    private class PriorityNode implements Comparable<PriorityNode> {

        private T item;
        private double priority;

        PriorityNode(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }

        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(PriorityNode other) {
            if (other == null) {
                return -1;
            }
            return Double.compare(this.getPriority(), other.getPriority());
        }

        // Equal items do not need to have same priority.
        @Override
        public boolean equals(Object o) {
            if (o == null || o.getClass() != this.getClass()) {
                return false;
            } else {
                return ((PriorityNode) o).getItem().equals(this.getItem());
            }
        }
        @Override
        public int hashCode() {
            return item.hashCode();
        }
    }

    private boolean isEmpty() {
        return size == 0;
    }

    // Return the index of the PriorityNode that contains the item.
    private int getIndex(T item) {
        return itemPQ.indexOf(new PriorityNode(item, 0));
    }

    // Return the index of parent of current node.
    private int parent(int i) {
        if (i == 0) {
            return 0;
        } else {
            return (i - 1) / 2;
        }
    }

    // Return the index of left child of current node.
    private int leftChild(int i) {
        return 2 * i + 1;
    }

    // Return the index of right child of current node.
    private int rightChild(int i) {
        return 2 * i + 2;
    }

    // Helper of add().
    private void swim(int i) {
        if (i > 0 && smaller(i, parent(i))) {
            swap(i, parent(i));
            swim(parent(i));
        }
    }

    // Helper of removeSmallest().
    private void sink(int i) {
        int smallest = i;
        if (leftChild(i) <= size() - 1 && smaller(leftChild(i), i)) {
            smallest = leftChild(i);
        }
        if (rightChild(i) <= size() - 1 && smaller(rightChild(i), smallest)) {
            smallest = rightChild(i);
        }
        if (smallest != i) {
            swap(i, smallest);
            sink(smallest);
        }
    }

    // Swap two nodes.
    private void swap(int i, int j) {
        PriorityNode temp = itemPQ.get(i);
        itemPQ.set(i, itemPQ.get(j));
        itemPQ.set(j, temp);
    }

    // Return true if ith node has smaller priority than jth node.
    private boolean smaller(int i, int j) {
        return itemPQ.get(i).compareTo(itemPQ.get(j)) < 0;
    }
}
