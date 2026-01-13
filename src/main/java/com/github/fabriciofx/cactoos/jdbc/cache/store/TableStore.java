/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.store;

import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.cache.Statistic;
import com.github.fabriciofx.cactoos.jdbc.cache.Statistics;
import com.github.fabriciofx.cactoos.jdbc.cache.Store;
import java.util.HashMap;
import java.util.Map;

/**
 * TableStore.
 * <p>Store a {@link Table} associated with a key.</p>
 * @since 0.9.0
 */
public final class TableStore implements Store<String, Table> {
    /**
     * The Results.
     */
    private final Map<String, Table> results;

    /**
     * Maximum cache size.
     */
    private final int size;

    /**
     * Statistics.
     */
    private final Statistics statistics;

    /**
     * Ctor.
     * @param statistics The statistics
     */
    public TableStore(final Statistics statistics) {
        this(new HashMap<>(), Integer.MAX_VALUE, statistics);
    }

    /**
     * Ctor.
     * @param results Data to initialize the cache
     * @param max Maximum cache size
     * @param statistics The statistics
     */
    public TableStore(
        final Map<String, Table> results,
        final int max,
        final Statistics statistics
    ) {
        this.results = results;
        this.size = max;
        this.statistics = statistics;
    }

    @Override
    public Table retrieve(final String key) {
        return this.results.get(key);
    }

    @Override
    public void save(final String key, final Table value) {
        final Statistic eviction = this.statistics.statistic("eviction");
        while (this.results.size() > this.size) {
            this.results.remove(this.results.keySet().iterator().next());
            eviction.increment();
        }
        this.results.put(key, value);
    }

    @Override
    public Table delete(final String key) {
        final Statistic invalidation = this.statistics.statistic(
            "invalidation"
        );
        invalidation.increment();
        return this.results.remove(key);
    }

    @Override
    public boolean contains(final String key) {
        final boolean exists = this.results.containsKey(key);
        if (exists) {
            final Statistic hits = this.statistics.statistic("hits");
            hits.increment();
        } else {
            final Statistic misses = this.statistics.statistic("misses");
            misses.increment();
        }
        final Statistic lookups = this.statistics.statistic("lookups");
        lookups.increment();
        return exists;
    }

    @Override
    public void clear() {
        this.results.clear();
    }
}
