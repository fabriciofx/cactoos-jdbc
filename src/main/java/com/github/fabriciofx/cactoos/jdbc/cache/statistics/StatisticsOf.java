/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.statistics;

import com.github.fabriciofx.cactoos.jdbc.cache.Statistic;
import com.github.fabriciofx.cactoos.jdbc.cache.Statistics;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * StatisticsOf.
 * @since 0.9.0
 */
public final class StatisticsOf implements Statistics {
    /**
     * Statistics.
     */
    private final Map<String, Statistic> stats;

    /**
     * Ctor.
     * @param statistics The statistics
     */
    public StatisticsOf(final Statistic... statistics) {
        this(
            Arrays.stream(statistics).collect(
                Collectors.toMap(Statistic::name, Function.identity())
            )
        );
    }

    /**
     * Ctor.
     * @param statistics The statistics
     */
    public StatisticsOf(final Map<String, Statistic> statistics) {
        this.stats = statistics;
    }

    @Override
    public Statistic statistic(final String name) {
        return this.stats.get(name);
    }

    @Override
    public void reset() {
        this.stats.clear();
    }

    @Override
    public Iterator<Statistic> iterator() {
        return this.stats.values().iterator();
    }
}
