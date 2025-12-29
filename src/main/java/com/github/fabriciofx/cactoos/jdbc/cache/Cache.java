package com.github.fabriciofx.cactoos.jdbc.cache;

public interface Cache<K, V> {
    V retrieve(K key);
    void store(K key, V value);
    boolean contains(K key);
    void clear();
}
