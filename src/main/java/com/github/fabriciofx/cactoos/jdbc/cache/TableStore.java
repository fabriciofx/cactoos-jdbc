/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.cache.Entries;
import com.github.fabriciofx.cactoos.cache.Entry;
import com.github.fabriciofx.cactoos.cache.Key;
import com.github.fabriciofx.cactoos.cache.Keys;
import com.github.fabriciofx.cactoos.cache.Store;
import com.github.fabriciofx.cactoos.cache.metadata.TypeOf;
import com.github.fabriciofx.cactoos.cache.store.StoreOf;
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
 * @checkstyle NestedIfDepthCheck (200 lines)
 */
@SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts")
public final class TableStore implements Store<Query, Table> {
    /**
     * Store.
     */
    private final Store<Query, Table> store;

    /**
     * Table name (String) -> Queries (Keys) association.
     */
    private final Map<String, Set<Key<Query>>> tables;

    /**
     * Ctor.
     */
    public TableStore() {
        this(new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
    }

    /**
     * Ctor.
     *
     * @param entries Data to initialize the cache
     * @param keys Keys associated to a table
     */
    public TableStore(
        final Map<Key<Query>, Entry<Query, Table>> entries,
        final Map<String, Set<Key<Query>>> keys
    ) {
        this(new StoreOf<>(entries), keys);
    }

    /**
     * Ctor.
     * @param store A store
     * @param keys Keys associated to a table
     */
    public TableStore(
        final Store<Query, Table> store,
        final Map<String, Set<Key<Query>>> keys
    ) {
        this.store = store;
        this.tables = keys;
    }

    @Override
    public Entry<Query, Table> retrieve(final Key<Query> key) {
        return this.store.retrieve(key);
    }

    @Override
    public List<Entry<Query, Table>> save(
        final Key<Query> key,
        final Entry<Query, Table> entry
    ) throws Exception {
        final List<Set<String>> tbls = entry.metadata().value(
            "tables",
            new TypeOf<>() { }
        );
        if (!tbls.isEmpty()) {
            for (final String table : tbls.get(0)) {
                final Set<Key<Query>> keys = this.tables.get(table);
                if (keys == null) {
                    final Set<Key<Query>> kys = new HashSet<>();
                    kys.add(key);
                    this.tables.put(table, kys);
                } else {
                    keys.add(key);
                }
            }
        }
        return this.store.save(key, entry);
    }

    @Override
    public Entry<Query, Table> delete(final Key<Query> key) {
        final Entry<Query, Table> entry = this.store.delete(key);
        if (entry.valid()) {
            final List<Set<String>> tbls = entry.metadata().value(
                "tables",
                new TypeOf<>() { }
            );
            if (!tbls.isEmpty()) {
                for (final String table : tbls.get(0)) {
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
        }
        return entry;
    }

    @Override
    public boolean contains(final Key<Query> key) {
        return this.store.contains(key);
    }

    @Override
    public Keys<Query> keys() {
        return this.store.keys();
    }

    @Override
    public Entries<Query, Table> entries() {
        return this.store.entries();
    }
}
