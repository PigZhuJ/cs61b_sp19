import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Stack;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;

    private class Node {
        private K key;
        private V value;
        private Node left, right;
        private int size;

        public Node(K key, V val, int size) {
            this.key = key;
            this.value = val;
            this.size = size;
        }
    }

    public BSTMap() {
    }

    /**
     * Removes all of the mappings from this map.
     */
    @Override
    public void clear() {
        root = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        return get(key) != null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node x, K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return get(x.left, key);
        } else if (cmp > 0) {
            return get(x.right, key);
        } else {
            return x.value;
        }
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        } else {
            return x.size;
        }
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        root = put(root, key, value);
    }

    private Node put(Node x, K key, V value) {
        if (x == null) {
            return new Node(key, value, 1);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, value);
        } else if (cmp > 0) {
            x.right = put(x.right, key, value);
        } else {
            x.value = value;
        }
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    /* Print the BSTMap (key, vale) pairs in increasing of order of key. */
    public void printInOrder() {
        for (int i = 0; i < size(); i += 1) {
            System.out.println(select(i).key + " " + select(i).value);
        }
    }

    /* printInOrder() helper.
     * Return the Node with (k + 1)st smallest key.
     */
    private Node select(int k) {
        if (k < 0 || k >= size()) {
            throw new IllegalArgumentException();
        }
        return select(root, k);
    }

    /* printInOrder() helper.
     * Return key of rank k.
     */
    private Node select(Node x, int k) {
        if (x == null) {
            return null;
        }
        int t = size(x.left);
        if (t > k) {
            return select(x.left, k);
        } else if (t < k) {
            return select(x.right, k - t - 1);
        } else {
            return x;
        }
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> BSTSet = new HashSet<>();
        for (int i = 0; i < size(); i += 1) {
            BSTSet.add(select(i).key);
        }
        return BSTSet;
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        V toRemove = get(key);
        root = remove(root, key);
        return toRemove;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        if (!containsKey(key)) {
            return null;
        }
        if (!get(key).equals(value)) {
            return null;
        }
        root = remove(root, key);
        return value;
    }

    /* Return the tree which has the node with specific key been removed. */
    private Node remove(Node x, K key) {
        if (x == null) {
            return null;
        }

        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = remove(x.left, key);
        } else if (cmp > 0) {
            x.right = remove(x.right, key);
        } else {
            if (x.right == null) {
                return x.left;
            }
            if (x.left == null) {
                return x.right;
            }
            // If both left and right nodes are not null, then replace this node
            // with the smallest node in the right node (or with the largest
            // node in the left node, same, but does not implement here).
            Node temp = x;
            x = min(temp.right);
            x.right = deleteMin(temp.right);
            x.left = temp.left;
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    private Node min(Node x) {
        if (x.left == null) {
            return x;
        }
        return min(x.left);
    }

    /* Return the tree which has the node with min key been removed. */
    private Node deleteMin(Node x) {
        if (x.left == null) {
            // If x.left is null, meaning that x is the min, so replace it with
            // x.right, whether x.right is null or not does not matter.
            return x.right;
        }
        x.left = deleteMin(x.left);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    /* Iterator of the BSTMap. */
    public Iterator<K> iterator() {
        return new BSTIterator(root);
    }

    private class BSTIterator implements Iterator<K> {
        private Stack<Node> stack = new Stack<>();

        public BSTIterator(Node src) {
            while (src != null) {
                // Push root node and all left nodes to the stack.
                stack.push(src);
                src = src.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public K next() {
            Node curr = stack.pop();

            if (curr.right != null) {
                Node temp = curr.right;
                while (temp != null) {
                    stack.push(temp);
                    temp = temp.left;
                }
            }
            return curr.key;
        }
    }

    public static void main(String[] args) {
        BSTMap<String, Integer> bstMap = new BSTMap<>();
        for (int i = 0; i < 10; i++) {
            bstMap.put("hi" + i, 1 + i);
        }
//        bstMap.printInOrder();
        Iterator<String> itr = bstMap.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }
}
