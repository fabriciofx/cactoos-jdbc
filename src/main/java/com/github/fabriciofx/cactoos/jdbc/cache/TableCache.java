/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Evictions;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Hits;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Invalidations;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Lookups;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Misses;
import com.github.fabriciofx.cactoos.jdbc.cache.statistics.StatisticsOf;
import java.util.HashMap;
import java.util.Map;

/**
 * TableCache.
 * @since 0.9.0
 */
public final class TableCache implements Cache<String, Table> {
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
    private final Statistics stats;

    /**
     * Ctor.
     */
    public TableCache() {
        this(
            new StatisticsOf(
                new Hits(),
                new Misses(),
                new Lookups(),
                new Evictions(),
                new Invalidations()
            )
        );
    }

    /**
     * Ctor.
     * @param statistics The statistics
     */
    public TableCache(final Statistics statistics) {
        this(new HashMap<>(), Integer.MAX_VALUE, statistics);
    }

    /**
     * Ctor.
     * @param results Data to initialize the cache
     * @param max Maximum cache size
     * @param statistics The statistics
     */
    public TableCache(
        final Map<String, Table> results,
        final int max,
        final Statistics statistics
    ) {
        this.results = results;
        this.size = max;
        this.stats = statistics;
    }

    @Override
    public Table retrieve(final String key) {
        return this.results.get(key);
    }

    @Override
    public void store(final String key, final Table value) {
        final Statistic evictions = this.stats.statistic("evictions");
        while (this.results.size() > this.size) {
            this.results.remove(this.results.keySet().iterator().next());
            evictions.increment();
        }
        this.results.put(key, value);
    }

    @Override
    public Table delete(final String key) {
        final Statistic invalids = this.stats.statistic(
            "invalidations"
        );
        invalids.increment();
        return this.results.remove(key);
    }

    @Override
    public boolean contains(final String key) {
        final boolean exists = this.results.containsKey(key);
        if (exists) {
            final Statistic hits = this.stats.statistic("hits");
            hits.increment();
        } else {
            final Statistic misses = this.stats.statistic("misses");
            misses.increment();
        }
        final Statistic lookups = this.stats.statistic("lookups");
        lookups.increment();
        return exists;
    }

    @Override
    public Statistics statistics() {
        return this.stats;
    }

    @Override
    public void clear() {
        this.results.clear();
        this.stats.reset();
    }
}
