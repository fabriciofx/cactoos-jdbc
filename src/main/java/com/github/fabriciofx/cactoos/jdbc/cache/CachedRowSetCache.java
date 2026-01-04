/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.rowset.CachedRowSet;

/**
 * CachedRowSetCache.
 * Store a {@link javax.sql.rowset.CachedRowSet} associated with a SQL query.
 * @since 0.9.0
 */
public final class CachedRowSetCache implements Cache<String, CachedRowSet> {
    /**
     * The CachedRowSets.
     */
    private final Map<String, CachedRowSet> results;

    /**
     * Maximum cache size.
     */
    private final int size;

    /**
     * Ctor.
     */
    public CachedRowSetCache() {
        this(new ConcurrentHashMap<>(), Integer.MAX_VALUE);
    }

    /**
     * Ctor.
     * @param results Data to initialize the cache
     * @param max Maximum cache size
     */
    public CachedRowSetCache(
        final Map<String, CachedRowSet> results,
        final int max
    ) {
        this.results = results;
        this.size = max;
    }

    @Override
    public CachedRowSet retrieve(final String key) {
        return this.results.get(key);
    }

    @Override
    public void store(final String key, final CachedRowSet value) {
        while (this.results.size() > this.size) {
            this.results.remove(this.results.keySet().iterator().next());
        }
        this.results.put(key, value);
    }

    @Override
    public CachedRowSet delete(final String key) {
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
