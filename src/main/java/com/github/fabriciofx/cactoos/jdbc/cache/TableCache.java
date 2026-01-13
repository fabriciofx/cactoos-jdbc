/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Table;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * TableCache.
 * Store a {@link ResultSet} associated with a SQL query.
 * @since 0.9.0
 */
public final class TableCache implements Cache<String, Table> {
    /**
     * The ResultSets.
     */
    private final Map<String, Table> results;

    /**
     * Maximum cache size.
     */
    private final int size;

    /**
     * Ctor.
     */
    public TableCache() {
        this(new HashMap<>(), Integer.MAX_VALUE);
    }

    /**
     * Ctor.
     * @param results Data to initialize the cache
     * @param max Maximum cache size
     */
    public TableCache(
        final Map<String, Table> results,
        final int max
    ) {
        this.results = results;
        this.size = max;
    }

    @Override
    public Table retrieve(final String key) {
        return this.results.get(key);
    }

    @Override
    public void store(final String key, final Table value) {
        while (this.results.size() > this.size) {
            this.results.remove(this.results.keySet().iterator().next());
        }
        this.results.put(key, value);
    }

    @Override
    public Table delete(final String key) {
        return this.results.remove(key);
    }

    @Override
    public boolean contains(final String key) {
        return this.results.containsKey(key);
    }

    @Override
    public void clear() {
        this.results.clear();
    }
}
