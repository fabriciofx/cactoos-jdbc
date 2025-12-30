/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import java.sql.ResultSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ResultSetCache.
 * Store a {@link java.sql.ResultSet} associated with a SQL query.
 * @since 0.9.0
 */
public final class ResultSetCache implements Cache<String, ResultSet> {
    /**
     * The ResultSets.
     */
    private final Map<String, ResultSet> results;

    /**
     * Ctor.
     */
    public ResultSetCache() {
        this(new ConcurrentHashMap<>());
    }

    /**
     * Ctor.
     * @param results Data to initialize the cache
     */
    public ResultSetCache(final Map<String, ResultSet> results) {
        this.results = results;
    }

    @Override
    public ResultSet retrieve(final String key) {
        return this.results.get(key);
    }

    @Override
    public void store(final String key, final ResultSet value) {
        this.results.put(key, value);
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
