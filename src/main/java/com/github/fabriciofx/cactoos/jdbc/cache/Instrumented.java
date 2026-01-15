/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Evictions;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Hits;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Invalidations;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Lookups;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Misses;
import com.github.fabriciofx.cactoos.jdbc.cache.statistics.StatisticsOf;

/**
 * Instrumented Cache.
 * @param <K> The key type
 * @param <V> The value type
 * @since 0.9.0
 */
@SuppressWarnings("PMD.UnnecessaryFullyQualifiedName")
public final class Instrumented<K, V> implements Cache<K, V> {
    /**
     * Cache.
     */
    private final Cache<K, V> origin;

    /**
     * Statistics.
     */
    private final Statistics stats;

    /**
     * Ctor.
     * @param cache The cache
     */
    public Instrumented(final Cache<K, V> cache) {
        this(
            cache,
            new StatisticsOf(
                new Hits(),
                new Misses(),
                new Lookups(),
                new Invalidations(),
                new Evictions()
            )
        );
    }

    /**
     * Ctor.
     * @param cache The cache
     * @param statistics The statistics
     */
    public Instrumented(final Cache<K, V> cache, final Statistics statistics) {
        this.origin = cache;
        this.stats = statistics;
    }

    @Override
    public Store<K, V> store() {
        return new com.github.fabriciofx.cactoos.jdbc.cache.store.Instrumented<>(
            this.origin.store(),
            this.stats
        );
    }

    @Override
    public Statistics statistics() {
        return this.stats;
    }
}
