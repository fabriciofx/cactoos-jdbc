/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.store;

import com.github.fabriciofx.cactoos.jdbc.Store;
import com.github.fabriciofx.cactoos.jdbc.cache.Statistics;

/**
 * Instrumented.
 * @since 0.9.0
 */
public final class Instrumented<K, V> implements Store<K, V> {
    /**
     * Cache.
     */
    private final Store<K, V> origin;

    /**
     * Statistics.
     */
    private final Statistics stats;

    /**
     * Ctor.
     * @param cache The cache
     * @param statistics The statistics
     */
    public Instrumented(final Store<K, V> cache, final Statistics statistics) {
        this.origin = cache;
        this.stats = statistics;
    }

    @Override
    public V retrieve(final K key) {
        this.stats.statistic("lookups").increment();
        return this.origin.retrieve(key);
    }

    @Override
    public void save(final K key, final V value) {
        this.origin.save(key, value);
    }

    @Override
    public V delete(final K key) {
        this.stats.statistic("invalidations").increment();
        return this.origin.delete(key);
    }

    @Override
    public boolean contains(final K key) {
        final boolean exists = this.origin.contains(key);
        if (exists) {
            this.stats.statistic("hits").increment();
        } else {
            this.stats.statistic("misses").increment();
        }
        this.stats.statistic("lookups").increment();
        return exists;
    }

    @Override
    public void clear() {
        this.origin.clear();
    }
}
