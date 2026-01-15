/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import com.github.fabriciofx.cactoos.jdbc.cache.Statistics;

/**
 * Cache.
 * @param <K> The key type
 * @param <V> The value type
 * @since 0.9.0
 */
public interface Cache<K, V> {
    /**
     * Store.
     * @return The store.
     */
    Store<K, V> store();

    /**
     * Statistics.
     * @return The statistics
     */
    Statistics statistics();
}
