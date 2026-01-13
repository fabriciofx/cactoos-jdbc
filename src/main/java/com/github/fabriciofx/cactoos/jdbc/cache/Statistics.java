/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

/**
 * Statistics.
 * @since 0.9.0
 */
public interface Statistics extends Iterable<Statistic> {
    /**
     * Retrieve a statistic.
     * @param name The name of statistic
     * @return The statistic
     */
    Statistic statistic(String name);

    /**
     * Reset all statistics.
     */
    void reset();
}
