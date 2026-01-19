/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.store;

import com.github.fabriciofx.cactoos.jdbc.cache.Entry;
import com.github.fabriciofx.cactoos.jdbc.cache.Key;
import com.github.fabriciofx.cactoos.jdbc.cache.Statistics;
import com.github.fabriciofx.cactoos.jdbc.cache.Store;
import java.util.List;

/**
 * Instrumented Store.
 * @param <D> The domain key type
 * @param <V> The value type stored
 * @since 0.9.0
 */
public final class Instrumented<D, V> implements Store<D, V> {
    /**
     * Store.
     */
    private final Store<D, V> origin;

    /**
     * Statistics.
     */
    private final Statistics stats;

    /**
     * Ctor.
     * @param cache The cache
     * @param statistics The statistics
     */
    public Instrumented(final Store<D, V> cache, final Statistics statistics) {
        this.origin = cache;
        this.stats = statistics;
    }

    @Override
    public Entry<D, V> retrieve(final Key<D> key) {
        this.stats.statistic("lookups").increment(1);
        return this.origin.retrieve(key);
    }

    @Override
    public List<Entry<D, V>> save(
        final Key<D> key,
        final Entry<D, V> entry
    ) throws Exception {
        final List<Entry<D, V>> removed = this.origin.save(key, entry);
        this.stats.statistic("evictions").increment(removed.size());
        return removed;
    }

    @Override
    public Entry<D, V> delete(final Key<D> key) {
        this.stats.statistic("invalidations").increment(1);
        return this.origin.delete(key);
    }

    @Override
    public boolean contains(final Key<D> key) {
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
    public List<Entry<D, V>> invalidate(final Iterable<String> metadata) {
        final List<Entry<D, V>> removed = this.origin.invalidate(metadata);
        this.stats.statistic("invalidations").increment(removed.size());
        return removed;
    }

    @Override
    public void clear() {
        this.origin.clear();
    }
}
