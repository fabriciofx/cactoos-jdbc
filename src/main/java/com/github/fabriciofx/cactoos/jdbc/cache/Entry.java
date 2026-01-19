/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

/**
 * Entry.
 * @param <D> The domain key type
 * @param <V> The value type stored
 * @since 0.9.0
 */
public interface Entry<D, V> {
    /**
     * Retrieve the key associated with this entry.
     * @return The key
     */
    Key<D> key();

    /**
     * Retrieve the value associated with this entry.
     * @return The value
     */
    V value();

    /**
     * Retrieve the metadata associated with this entry.
     * @param entity Entity that has metadata
     * @return The metadata
     */
    Iterable<String> metadata(String entity);
}
