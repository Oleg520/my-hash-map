package ru.oleg520;

import java.util.*;

public class MyHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private final float loadFactor;
    private int threshold;

    public static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }

    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

        this.loadFactor = loadFactor;
        this.threshold = (int) (initialCapacity * loadFactor);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }


    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public V get(K key) {
        Node<K, V> node;
        return ((node = getNode(key)) == null) ? null : node.value;
    }

    private Node<K, V> getNode(K key) {
        int hash = hash(key);
        int index = (table.length - 1) & hash;
        Node<K, V> node = table[index];

        while (node != null) {
            if (node.hash == hash &&
                    (node.key == key || (key != null && key.equals(node.key)))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    public V put(K key, V value) {
        return putVal(hash(key), key, value);
    }

    private V putVal(int hash, K key, V value) {
        Node<K, V>[] tab; Node<K, V> first; int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((first = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);

        for (Node<K, V> node = first; node != null; node = node.next) {
            if (node.hash == hash &&
                    (node.key == key || (key != null && key.equals(node.key)))) {
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }

        table[i] = newNode(hash, key, value, first);
        if (++size > threshold) {
            resize();
        }
        return null;
    }

    final Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        int oldCap = (oldTable == null) ? 0 : oldTable.length;
        int newCap;

        if (oldCap == 0) {
            newCap = DEFAULT_CAPACITY;
        } else {
            newCap = oldCap << 1; // Удваиваем capacity
        }

        threshold = (int) (newCap * loadFactor);
        @SuppressWarnings("unchecked")
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCap];
        table = newTable;

        if (oldTable != null) {
            for (int i = 0; i < oldCap; i++) {
                Node<K, V> node = oldTable[i];
                while (node != null) {
                    Node<K, V> next = node.next;
                    int index = (newCap - 1) & node.hash;
                    node.next = newTable[index];
                    newTable[index] = node;
                    node = next;
                }
            }
        }
        return newTable;
    }

    public V remove(K key) {
        Node<K, V> node = removeNode(hash(key), key);
        return (node == null) ? null : node.value;
    }

    private Node<K, V> removeNode(int hash, K key) {
        if (table == null || table.length == 0) {
            return null;
        }

        int index = (table.length - 1) & hash;
        Node<K, V> node = table[index];
        Node<K, V> prev = null;

        while (node != null) {
            if (node.hash == hash &&
                    (node.key == key || (key != null && key.equals(node.key)))) {

                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return node;
            }
            prev = node;
            node = node.next;
        }
        return null;
    }

    public void clear() {
        Node<K,V>[] tab;
        if ((tab = table) != null && size > 0) {
            size = 0;
            Arrays.fill(tab, null);
        }
    }

    Node<K, V> newNode(int hash, K key, V value, Node<K, V> next) {
        return new Node<>(hash, key, value, next);
    }

    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        map.put("abc", 1);
        map.put("cba", 2);

        System.out.println(map.get("abc"));
        System.out.println(map.get("cba"));

        map.remove("abc");
        System.out.println(map.get("abc"));

        map.put(null, 3);
        System.out.println(map.get(null));
    }
}