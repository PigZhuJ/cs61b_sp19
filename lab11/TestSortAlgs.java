import edu.princeton.cs.algs4.Queue;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestSortAlgs {

    @Test
    public void testQuickSort() {
        Queue<Integer> tas = new Queue<>();
        tas.enqueue(8);
        tas.enqueue(3);
        tas.enqueue(1);
        tas.enqueue(5);
        tas.enqueue(0);
        tas.enqueue(2);
        tas.enqueue(6);
        tas.enqueue(4);
        for (int i = 10; i < 10000; i += 1) {
            tas.enqueue(i);
        }
        assertTrue(isSorted(QuickSort.quickSort(tas)));

        Queue<String> pas = new Queue<>();
        pas.enqueue("Joe");
        pas.enqueue("Nancy");
        pas.enqueue("Caroline");
        pas.enqueue("Jeffery");
        pas.enqueue("Tom");
        pas.enqueue("Ava");
        pas.enqueue("Peter");
        pas.enqueue("Josh");
        pas.enqueue("Hug");
        assertTrue(isSorted(QuickSort.quickSort(pas)));
    }

    @Test
    public void testMergeSort() {
        Queue<Integer> tas = new Queue<>();
        tas.enqueue(8);
        tas.enqueue(3);
        tas.enqueue(1);
        tas.enqueue(5);
        tas.enqueue(0);
        tas.enqueue(2);
        tas.enqueue(6);
        tas.enqueue(4);
        for (int i = 10; i < 10000; i += 1) {
            tas.enqueue(i);
        }
        assertTrue(isSorted(MergeSort.mergeSort(tas)));

        Queue<String> pas = new Queue<>();
        pas.enqueue("Joe");
        pas.enqueue("Nancy");
        pas.enqueue("Caroline");
        pas.enqueue("Jeffery");
        pas.enqueue("Tom");
        pas.enqueue("Ava");
        pas.enqueue("Peter");
        pas.enqueue("Josh");
        pas.enqueue("Hug");
        assertTrue(isSorted(MergeSort.mergeSort(pas)));
    }

    /**
     * Returns whether a Queue is sorted or not.
     *
     * @param items  A Queue of items
     * @return       true/false - whether "items" is sorted
     */
    private <Item extends Comparable> boolean isSorted(Queue<Item> items) {
        if (items.size() <= 1) {
            return true;
        }
        Item curr = items.dequeue();
        Item prev = curr;
        while (!items.isEmpty()) {
            prev = curr;
            curr = items.dequeue();
            if (curr.compareTo(prev) < 0) {
                return false;
            }
        }
        return true;
    }
}
