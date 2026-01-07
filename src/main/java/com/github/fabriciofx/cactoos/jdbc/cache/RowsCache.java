/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.rowset.CachedRowSet;

/**
 * CachedRowSetCache.
 * Store a {@link CachedRowSet} associated with a SQL query.
 * @since 0.9.0
 */
public final class RowsCache implements Cache<String, List<Map<String, Object>>> {
    /**
     * The Rows.
     */
    private final Map<String, List<Map<String, Object>>> results;

    /**
     * Maximum cache size.
     */
    private final int size;

    /**
     * Ctor.
     */
    public RowsCache() {
        this(new HashMap<>(), Integer.MAX_VALUE);
    }

    /**
     * Ctor.
     * @param results Data to initialize the cache
     * @param max Maximum cache size
     */
    public RowsCache(
        final Map<String, List<Map<String, Object>>> results,
        final int max
    ) {
        this.results = results;
        this.size = max;
    }

    @Override
    public List<Map<String, Object>> retrieve(final String key) {
        return this.results.get(key);
    }

    @Override
    public void store(final String key, final List<Map<String, Object>> value) {
        while (this.results.size() > this.size) {
            this.results.remove(this.results.keySet().iterator().next());
        }
        this.results.put(key, value);
    }

    @Override
    public List<Map<String, Object>> delete(final String key) {
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
