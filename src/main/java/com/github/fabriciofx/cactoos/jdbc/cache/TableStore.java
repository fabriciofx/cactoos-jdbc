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
import com.github.fabriciofx.cactoos.cache.store.StoreOf;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Table;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TableStore.
 *
 * @since 0.9.0
 */
public final class TableStore implements Store<Query, Table> {

    /**
     * Store.
     */
    private final Store<Query, Table> store;

    /**
     * Ctor.
     */
    public TableStore() {
        this(new ConcurrentHashMap<>());
    }

    /**
     * Ctor.
     *
     * @param entries Data to initialize the cache
     */
    public TableStore(final Map<Key<Query>, Entry<Query, Table>> entries) {
        this(new StoreOf<>(entries));
    }

    /**
     * Ctor.
     *
     * @param store A store
     */
    public TableStore(final Store<Query, Table> store) {
        this.store = store;
    }

    @Override
    public Entry<Query, Table> retrieve(final Key<Query> key) {
        return this.store.retrieve(key);
    }

    @Override
    public Entry<Query, Table> save(
        final Key<Query> key,
        final Entry<Query, Table> entry
    ) {
        return this.store.save(key, entry);
    }

    @Override
    public Entry<Query, Table> delete(final Key<Query> key) {
        return this.store.delete(key);
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
