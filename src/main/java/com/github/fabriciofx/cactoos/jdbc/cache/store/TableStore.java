/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.store;

import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.cache.Policy;
import com.github.fabriciofx.cactoos.jdbc.cache.Store;
import com.github.fabriciofx.cactoos.jdbc.cache.policy.MaxSizePolicy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TableStore.
 *
 * @since 0.9.0
 */
public final class TableStore implements Store<String, Table> {
    /**
     * The Results.
     */
    private final Map<String, Table> results;

    /**
     * Policy.
     */
    private final Policy<Map<String, Table>, Table> policy;

    /**
     * Ctor.
     */
    public TableStore() {
        this(new HashMap<>(), new MaxSizePolicy());
    }

    /**
     * Ctor.
     *
     * @param results Data to initialize the cache
     * @param policy Policy that will remove automatically old cache entries
     */
    public TableStore(
        final Map<String, Table> results,
        final Policy<Map<String, Table>, Table> policy
    ) {
        this.results = results;
        this.policy = policy;
    }

    @Override
    public Table retrieve(final String key) {
        return this.results.get(key);
    }

    @Override
    public List<Table> save(final String key, final Table value)
        throws Exception {
        final List<Table> removed = this.policy.apply(this.results);
        this.results.put(key, value);
        return removed;
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
