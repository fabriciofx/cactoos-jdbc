/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.cache.Entry;
import com.github.fabriciofx.cactoos.cache.Key;
import com.github.fabriciofx.cactoos.cache.Policy;
import com.github.fabriciofx.cactoos.cache.policy.MaxSizePolicy;
import com.github.fabriciofx.cactoos.cache.store.StoreEnvelope;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TableStore.
 *
 * @since 0.9.0
 */
public final class TableStore extends StoreEnvelope<Query, Table> {
    /**
     * Table name (String) -> Queries (Keys) association.
     */
    private final Map<String, Set<Key<Query>>> tables;

    /**
     * Ctor.
     */
    public TableStore() {
        this(
            new ConcurrentHashMap<>(),
            new ConcurrentHashMap<>(),
            new MaxSizePolicy<>()
        );
    }

    /**
     * Ctor.
     *
     * @param entries Data to initialize the cache
     * @param keys Keys associated to a table
     * @param policy Policy that will remove automatically old cache entries
     */
    public TableStore(
        final Map<Key<Query>, Entry<Query, Table>> entries,
        final Map<String, Set<Key<Query>>> keys,
        final Policy<Query, Table> policy
    ) {
        super(entries, policy);
        this.tables = keys;
    }

    @Override
    public List<Entry<Query, Table>> save(
        final Key<Query> key,
        final Entry<Query, Table> entry
    ) throws Exception {
        final List<String> tbls = entry.metadata().get("tables");
        for (final String table : tbls) {
            final Set<Key<Query>> keys = this.tables.get(table);
            if (keys == null) {
                final Set<Key<Query>> kys = new HashSet<>();
                kys.add(key);
                this.tables.put(table, kys);
            } else {
                keys.add(key);
            }
        }
        return super.save(key, entry);
    }

    @Override
    public Entry<Query, Table> delete(final Key<Query> key) {
        final Entry<Query, Table> entry = super.delete(key);
        if (entry.valid()) {
            final List<String> tbls = entry.metadata().get("tables");
            for (final String table : tbls) {
                final Set<Key<Query>> keys = this.tables.getOrDefault(
                    table,
                    new HashSet<>()
                );
                keys.remove(key);
                if (keys.isEmpty()) {
                    this.tables.remove(table);
                }
            }
        }
        return entry;
    }
}
