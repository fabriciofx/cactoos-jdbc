/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.statistic;

import com.github.fabriciofx.cactoos.jdbc.cache.Statistic;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Misses.
 * <p>Number of times the value was not found.</p>
 * @since 0.9.0
 */
public final class Misses implements Statistic {
    /**
     * Count.
     */
    private final AtomicInteger count;

    /**
     * Ctor.
     */
    public Misses() {
        this(new AtomicInteger(0));
    }

    /**
     * Ctor.
     * @param count The count
     */
    public Misses(final AtomicInteger count) {
        this.count = count;
    }

    @Override
    public String name() {
        return "misses";
    }

    @Override
    public void increment() {
        this.count.incrementAndGet();
    }

    @Override
    public void reset() {
        this.count.set(0);
    }

    @Override
    public int value() {
        return this.count.get();
    }
}
