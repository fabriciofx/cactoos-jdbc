/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

/**
 * Cache.
 * @param <K> The key type
 * @param <V> The value type
 * @since 0.9.0
 */
public interface Cache<K, V> {
    /**
     * Retrieve a data from cache.
     * @param key The key
     * @return The value associated with the key
     */
    V retrieve(K key);

    /**
     * Store a data into cache.
     * @param key The key associated to the value
     * @param value The value
     */
    void store(K key, V value);

    /**
     * Checks if the cache has a value associated with the key.
     * @param key The key
     * @return True if there is, false otherwise
     */
    boolean contains(K key);

    /**
     * Cleanup the cache.
     */
    void clear();
}
