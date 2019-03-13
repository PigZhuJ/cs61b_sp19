import com.sun.nio.sctp.IllegalReceiveException;

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;


public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private int size;
    private int threshold;
    private BucketEntity<K, V>[] buckets;

    private class BucketEntity<K, V> {
        private K key;
        private V value;
        private BucketEntity<K, V> next;
        private int hashCode;

        public BucketEntity(int hashCode, K key, V value, BucketEntity<K, V> next) {
            this.hashCode = hashCode;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public int getHashCode() {
            return hashCode;
        }

        public void setHashCode(int hashCode) {
            this.hashCode = hashCode;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public BucketEntity<K, V> getNext() {
            return next;
        }

        public void setNext(BucketEntity<K, V> next) {
            this.next = next;
        }
    }

    public MyHashMap() {
        buckets = new BucketEntity[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        size = 0;
    }

    public MyHashMap(int initialSize) {
        buckets = new BucketEntity[initialSize];
        threshold = (int) (initialSize * LOAD_FACTOR);
        size = 0;
    }

    public MyHashMap(int initialSize, double loadFactor) {
        buckets = new BucketEntity[initialSize];
        threshold = (int) (initialSize * loadFactor);
        size = 0;
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        buckets = new BucketEntity[buckets.length];
        size = 0;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        return get(key) != null;
    }

    /** Rewrite the hashCode for this class. */
    private int hash(K key, int length) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        // Cited from https://algs4.cs.princeton.edu/34hash/SeparateChainingHashST.java.html
        return (key.hashCode() & 0x7fffffff) % length;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        int hashCode = hash(key, buckets.length);
        BucketEntity<K, V> entity = get(hashCode, key);
        return entity == null ? null : entity.getValue();
    }

    private BucketEntity<K, V> get(int hashCode, K key) {
        BucketEntity<K, V> entity = buckets[hashCode];
        while (entity != null) {
            if (entity.getHashCode() == hashCode && entity.getKey().equals(key)) {
                return entity;
            }
            entity = entity.getNext();
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value) {
        int hashCode = hash(key, buckets.length);
        BucketEntity<K, V> entity = buckets[hashCode];
        while (entity != null) {
            if (entity.getHashCode() == hashCode && entity.getKey().equals(key)) {
                entity.setValue(value);
                return;
            }
            entity = entity.getNext();
        }
        put(hashCode, key, value);
    }

    private void put(int hashCode, K key, V value) {
        BucketEntity<K, V> entity = new BucketEntity<>(hashCode, key, value, buckets[hashCode]);
        buckets[hashCode] = entity;
        size += 1;
        if (size > threshold) {
            resize(buckets.length * 2);
        }
    }

    private void resize(int capacity) {
        BucketEntity<K, V>[] newBuckets = new BucketEntity[capacity];
        for (int i = 0; i < buckets.length; i += 1) {
            BucketEntity<K, V> entity = buckets[i];
            while (entity != null) {
                BucketEntity<K, V> oldNext = entity.getNext();
                int newHashCode = hash(entity.getKey(), newBuckets.length);
                entity.setNext(newBuckets[newHashCode]);
                entity.setHashCode(newHashCode);
                newBuckets[newHashCode] = entity;
                entity = oldNext;
            }
        }
        buckets = newBuckets;
        threshold = (int) (buckets.length * LOAD_FACTOR);
    }

    /** Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> allKeys = new HashSet<>();
        for (int i = 0; i < buckets.length; i += 1) {
            BucketEntity<K, V> entity = buckets[i];
            while (entity != null) {
                allKeys.add(entity.getKey());
                entity = entity.getNext();
            }
        }
        return allKeys;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        int hashCode = hash(key, buckets.length);
        return remove(hashCode, key);
    }

    private V remove(int hashCode, K key) {
        BucketEntity<K, V> entity = buckets[hashCode];
        BucketEntity<K, V> nextEntity = entity.getNext();
        if (entity.getKey().equals(key)) {
            V toRemove = entity.getValue();
            buckets[hashCode] = nextEntity;
            size -= 1;
            return toRemove;
        } else {
            while (!nextEntity.getKey().equals(key)) {
                entity = entity.getNext();
                nextEntity = nextEntity.getNext();
            }
            V toRemove = nextEntity.getValue();
            entity.setNext(nextEntity.getNext());
            size -= 1;
            return toRemove;
        }
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    @Override
    public V remove(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        int hashCode = hash(key, buckets.length);
        return remove(hashCode, key, value);
    }

    private V remove(int hashCode, K key, V value) {
        BucketEntity<K, V> entity = buckets[hashCode];
        BucketEntity<K, V> nextEntity = entity.getNext();
        if (entity.getKey().equals(key) && entity.getValue().equals(value)) {
            V toRemove = entity.getValue();
            buckets[hashCode] = nextEntity;
            size -= 1;
            return toRemove;
        } else {
            while (!nextEntity.getKey().equals(key)) {
                entity = entity.getNext();
                nextEntity = nextEntity.getNext();
            }
            if (nextEntity.getValue().equals(value)) {
                V toRemove = nextEntity.getValue();
                entity.setNext(nextEntity.getNext());
                size -= 1;
                return toRemove;
            }
        }
        return null;
    }

}
