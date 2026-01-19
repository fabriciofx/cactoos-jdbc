/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.store;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.cache.Entry;
import com.github.fabriciofx.cactoos.jdbc.cache.Key;
import com.github.fabriciofx.cactoos.jdbc.cache.Policy;
import com.github.fabriciofx.cactoos.jdbc.cache.Store;
import com.github.fabriciofx.cactoos.jdbc.cache.policy.MaxSizePolicy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.StreamSupport;

/**
 * TableStore.
 *
 * @since 0.9.0
 */
public final class TableStore implements Store<Query, Table> {
    /**
     * Entries.
     */
    private final Map<Key<Query>, Entry<Query, Table>> entries;

    /**
     * Keys.
     */
    private final Map<String, Set<Key<Query>>> keys;

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
        this.entries = entries;
        this.keys = keys;
        this.policy = policy;
    }

    @Override
    public Entry<Query, Table> retrieve(final Key<Query> key) {
        return this.entries.get(key);
    }

    @Override
    public List<Entry<Query, Table>> save(
        final Key<Query> key,
        final Entry<Query, Table> entry
    ) throws Exception {
        final List<Entry<Query, Table>> removed = this.policy.apply(
            this.entries
        );
        removed.forEach(
            element -> element.metadata("tables").forEach(this.keys::remove)
        );
        entry.metadata("tables").forEach(
            table -> this.keys
                .computeIfAbsent(table, tbl -> new HashSet<>())
                .add(key)
        );
        this.entries.put(key, entry);
        return removed;
    }

    @Override
    public Entry<Query, Table> delete(final Key<Query> key) {
        this.keys.values().forEach(element -> element.remove(key));
        return this.entries.remove(key);
    }

    @Override
    public boolean contains(final Key<Query> key) {
        return this.entries.containsKey(key);
    }

    @Override
    public List<Entry<Query, Table>> invalidate(final Iterable<String> entity) {
        return StreamSupport
            .stream(entity.spliterator(), false)
            .map(this.keys::remove)
            .filter(Objects::nonNull)
            .flatMap(Set::stream)
            .distinct()
            .map(this.entries::remove)
            .filter(Objects::nonNull)
            .toList();
    }

    @Override
    public void clear() {
        this.entries.clear();
        this.keys.clear();
    }
}
