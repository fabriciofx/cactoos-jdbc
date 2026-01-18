/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.cache.statistics.StatisticsOf;
import com.github.fabriciofx.cactoos.jdbc.cache.store.TableStore;
import org.cactoos.Scalar;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

/**
 * TableCache.
 * @since 0.9.0
 */
public final class TableCache implements Cache {
    /**
     * Keep only one Store.
     */
    private final Scalar<Store> scalar;

    /**
     * Ctor.
     */
    public TableCache() {
        this(new Sticky<>(TableStore::new));
    }

    /**
     * Ctor.
     * @param scalar The store
     */
    public TableCache(final Scalar<Store> scalar) {
        this.scalar = scalar;
    }

    @Override
    public Store store() {
        return new Unchecked<>(this.scalar).value();
    }

    @Override
    public Statistics statistics() {
        return new StatisticsOf();
    }
}
