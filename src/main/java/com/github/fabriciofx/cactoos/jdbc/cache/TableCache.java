/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Eviction;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Hits;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Invalidation;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Lookups;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Misses;
import com.github.fabriciofx.cactoos.jdbc.cache.statistics.StatisticsOf;
import com.github.fabriciofx.cactoos.jdbc.cache.store.TableStore;

/**
 * TableCache.
 * @since 0.9.0
 */
public final class TableCache implements Cache<String, Table> {
    /**
     * Store.
     */
    private final Store<String, Table> str;

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
                new Eviction(),
                new Invalidation()
            )
        );
    }

    /**
     * Ctor.
     * @param statistics Statistics
     */
    public TableCache(final Statistics statistics) {
        this.str = new TableStore(statistics);
        this.stats = statistics;
    }

    @Override
    public Store<String, Table> store() {
        return this.str;
    }

    @Override
    public Statistics statistics() {
        return this.stats;
    }

    @Override
    public void clear() {
        this.str.clear();
        this.stats.reset();
    }
}
