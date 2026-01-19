/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import java.util.List;

/**
 * Store.
 * @param <D> The domain key type
 * @param <V> The value type stored
 * @since 0.9.0
 */
public interface Store<D, V> {
    /**
     * Retrieve an entry from store.
     * @param key The key
     * @return The entry associated with the key
     */
    Entry<D, V> retrieve(Key<D> key);

    /**
     * Save an entry into store.
     * @param key The key associated to the entry
     * @param entry An entry
     * @return Elements removed automatically
     * @throws Exception If something goes wrong
     */
    List<Entry<D, V>> save(Key<D> key, Entry<D, V> entry) throws Exception;

    /**
     * Delete an entry into store.
     * @param key The key associated to the entry
     * @return The entry associated with the key
     */
    Entry<D, V> delete(Key<D> key);

    /**
     * Checks if the store has an entry associated with the key.
     * @param key The key
     * @return True if there is, false otherwise
     */
    boolean contains(Key<D> key);

    /**
     * Invalidate store entries according metadata.
     * @param metadata The metadata
     * @return The entries associated with this metadata
     */
    List<Entry<D, V>> invalidate(Iterable<String> metadata);

    /**
     * Clear store.
     */
    void clear();
}
