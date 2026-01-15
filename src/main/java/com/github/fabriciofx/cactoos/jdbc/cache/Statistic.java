/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

/**
 * Statistic.
 * @since 0.9.0
 */
public interface Statistic {
    /**
     * Retrieve the name of statistic.
     * @return The name
     */
    String name();

    /**
     * Increments the statistic.
     * @param num Amount to increment the statistic
     */
    void increment(int num);

    /**
     * Resets the statistic.
     */
    void reset();

    /**
     * Retrieve the value.
     * @return The value
     */
    int value();
}
