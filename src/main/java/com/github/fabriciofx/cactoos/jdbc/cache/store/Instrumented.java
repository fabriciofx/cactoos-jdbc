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
import java.util.Set;

/**
 * Instrumented Store.
 * @since 0.9.0
 */
public final class Instrumented implements Store {
    /**
     * Store.
     */
    private final Store origin;

    /**
     * Statistics.
     */
    private final Statistics stats;

    /**
     * Ctor.
     * @param cache The cache
     * @param statistics The statistics
     */
    public Instrumented(final Store cache, final Statistics statistics) {
        this.origin = cache;
        this.stats = statistics;
    }

    @Override
    public Entry retrieve(final Key key) {
        this.stats.statistic("lookups").increment(1);
        return this.origin.retrieve(key);
    }

    @Override
    public List<Entry> save(final Key key, final Entry entry) throws Exception {
        final List<Entry> removed = this.origin.save(key, entry);
        this.stats.statistic("evictions").increment(removed.size());
        return removed;
    }

    @Override
    public Entry delete(final Key key) {
        this.stats.statistic("invalidations").increment(1);
        return this.origin.delete(key);
    }

    @Override
    public boolean contains(final Key key) {
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
    public List<Entry> invalidate(final Set<String> tables) {
        final List<Entry> removed = this.origin.invalidate(tables);
        this.stats.statistic("invalidations").increment(removed.size());
        return removed;
    }

    @Override
    public void clear() {
        this.origin.clear();
    }
}
