package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Store;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Hits;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Invalidations;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Lookups;
import com.github.fabriciofx.cactoos.jdbc.cache.statistic.Misses;
import com.github.fabriciofx.cactoos.jdbc.cache.statistics.StatisticsOf;

public final class Instrumented<K, V> implements Cache<K, V> {
    private final Cache<K, V> origin;
    private final Statistics stats;

    public Instrumented(final Cache<K, V> cache) {
        this(
            cache,
            new StatisticsOf(
                new Hits(),
                new Misses(),
                new Lookups(),
                new Invalidations()
            )
        );
    }

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
