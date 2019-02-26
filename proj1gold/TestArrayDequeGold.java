import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class TestArrayDequeGold {

    @Test
    public void test() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<Integer>();
        ArrayDequeSolution<Integer> happy = new ArrayDequeSolution<>();
        List<String> operations = new ArrayList<>();

        for (int i = 0; i < 100; i += 1) {
            // turn all operations that have been executed to string.
            String operation = "";
            for (String op : operations) {
                operation = operation + "\n" + op;
            }
            // 0 for addFirst, 1 for addLast, 2 for removeFirst, 3 for removeLast.
            int seed = StdRandom.uniform(3);
            if (seed == 0) {
                sad.addFirst(i);
                happy.addFirst(i);
                Integer sadFirst = sad.get(0);
                Integer happyFirst = happy.get(0);
                operations.add("addFirst(" + i + ")");
                assertEquals(operation + "\n" + "addFirst(" + i + ")", happyFirst, sadFirst);
            } else if(seed == 1) {
                // addLast has problem.
                // make this seed equals to a int > 4 can check whether
                // other methods have problems
                sad.addLast(i);
                happy.addLast(i);
                Integer sadLast = sad.get(sad.size() - 1);
                Integer happyLast = happy.get(happy.size() - 1);
                operations.add("addLast(" + i + ")");
                assertEquals(operation + "\n" + "addLast(" + i + ")", happyLast, sadLast);
            } else if(seed == 2 && !sad.isEmpty()) {
                Integer sadRemoveFirst = sad.removeFirst();
                Integer happyRemoveFirst = happy.removeFirst();
                operations.add("removeFirst()");
                assertEquals(operation + "\n" + "removeFirst()", happyRemoveFirst, sadRemoveFirst);
            } else if(seed == 3 && !sad.isEmpty()) {
                Integer sadRemoveLast = sad.removeLast();
                Integer happyRemoveLast = happy.removeLast();
                operations.add("removeLast()");
                assertEquals(operation + "\n" + "removeLast()", happyRemoveLast, sadRemoveLast);
            }
        }

    }

    /** This main method is optional. */
    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestArrayDequeGold.class);
    }
}
