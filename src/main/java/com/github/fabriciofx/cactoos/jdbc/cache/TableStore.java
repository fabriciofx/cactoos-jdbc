/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.cache.Entries;
import com.github.fabriciofx.cactoos.cache.Entry;
import com.github.fabriciofx.cactoos.cache.Key;
import com.github.fabriciofx.cactoos.cache.Keys;
import com.github.fabriciofx.cactoos.cache.Policy;
import com.github.fabriciofx.cactoos.cache.Store;
import com.github.fabriciofx.cactoos.cache.entries.MapEntries;
import com.github.fabriciofx.cactoos.cache.entry.InvalidEntry;
import com.github.fabriciofx.cactoos.cache.keys.SetKeys;
import com.github.fabriciofx.cactoos.cache.policy.MaxSizePolicy;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Table;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TableStore.
 *
 * @since 0.9.0
 */
public final class TableStore implements Store<Query, Table> {
    /**
     * Query (Key) -> Table (Entry) association.
     */
    private final Map<Key<Query>, Entry<Query, Table>> records;

    /**
     * Table name (String) -> Queries (Keys) association.
     */
    private final Map<String, Set<Key<Query>>> tables;

    /**
     * Policy.
     */
    private final Policy<Query, Table> policy;

    /**
     * Ctor.
     */
    public TableStore() {
        this(
            new HashMap<>(),
            new HashMap<>(),
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
        this.records = entries;
        this.tables = keys;
        this.policy = policy;
    }

    @Override
    public Entry<Query, Table> retrieve(final Key<Query> key) {
        return this.records.getOrDefault(key, new InvalidEntry<>());
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
        final List<Entry<Query, Table>> evicted = this.policy.apply(this);
        final Entry<Query, Table> removed = this.records.put(key, entry);
        if (removed != null) {
            evicted.add(removed);
        }
        return evicted;
    }

    @Override
    public Entry<Query, Table> delete(final Key<Query> key) {
        Entry<Query, Table> entry = this.records.remove(key);
        if (entry != null) {
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
        } else {
            entry = new InvalidEntry<>();
        }
        return entry;
    }

    @Override
    public boolean contains(final Key<Query> key) {
        return this.records.containsKey(key);
    }

    @Override
    public Keys<Query> keys() {
        return new SetKeys<>(this.records.keySet());
    }

    @Override
    public Entries<Query, Table> entries() {
        return new MapEntries<>(this.records);
    }
}
