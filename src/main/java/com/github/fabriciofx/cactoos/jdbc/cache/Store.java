/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import java.util.List;
import java.util.Set;

/**
 * Cache.
 * @since 0.9.0
 */
public interface Store {
    /**
     * Retrieve a data from cache.
     * @param key The key
     * @return The value associated with the key
     */
    Entry retrieve(Key key);

    /**
     * Save a data into cache.
     * @param key The key associated to the value
     * @param entry A Entry
     * @return Elements removed automatically
     * @throws Exception If something goes wrong
     */
    List<Entry> save(Key key, Entry entry) throws Exception;

    /**
     * Delete a value into cache.
     * @param key The key associated to the value
     * @return The value associated with the key
     */
    Entry delete(Key key);

    /**
     * Checks if the cache has a value associated with the key.
     * @param key The key
     * @return True if there is, false otherwise
     */
    boolean contains(Key key);

    /**
     * Invalidate cache entries according a set of tables.
     * @param tables A set of tables
     * @return The keys associated to table
     */
    List<Entry> invalidate(Set<String> tables);

    /**
     * Clear cache data and statistic.
     */
    void clear();
}
