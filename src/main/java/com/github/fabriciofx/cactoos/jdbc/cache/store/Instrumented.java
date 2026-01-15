/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.store;

import com.github.fabriciofx.cactoos.jdbc.cache.Statistics;
import com.github.fabriciofx.cactoos.jdbc.cache.Store;
import java.util.List;

/**
 * Instrumented Store.
 * @param <K> The key type
 * @param <V> The value type
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
        this.stats.statistic("lookups").increment(1);
        return this.origin.retrieve(key);
    }

    @Override
    public List<V> save(final K key, final V value) throws Exception {
        final List<V> removed = this.origin.save(key, value);
        this.stats.statistic("evictions").increment(removed.size());
        return removed;
    }

    @Override
    public V delete(final K key) {
        this.stats.statistic("invalidations").increment(1);
        return this.origin.delete(key);
    }

    @Override
    public boolean contains(final K key) {
        final boolean exists = this.origin.contains(key);
        if (exists) {
            this.stats.statistic("hits").increment(1);
        } else {
            this.stats.statistic("misses").increment(1);
        }
        this.stats.statistic("lookups").increment(1);
        return exists;
    }

    @Override
    public void clear() {
        this.origin.clear();
    }
}
