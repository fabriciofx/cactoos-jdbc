/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.store;

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

/**
 * TableStore.
 *
 * @since 0.9.0
 */
public final class TableStore implements Store {
    /**
     * Entries.
     */
    private final Map<Key, Entry> entries;

    /**
     * Keys.
     */
    private final Map<String, Set<Key>> keys;

    /**
     * Policy.
     */
    private final Policy policy;

    /**
     * Ctor.
     */
    public TableStore() {
        this(new HashMap<>(), new HashMap<>(), new MaxSizePolicy());
    }

    /**
     * Ctor.
     *
     * @param entries Data to initialize the cache
     * @param keys Keys associated to a table
     * @param policy Policy that will remove automatically old cache entries
     */
    public TableStore(
        final Map<Key, Entry> entries,
        final Map<String, Set<Key>> keys,
        final Policy policy
    ) {
        this.entries = entries;
        this.keys = keys;
        this.policy = policy;
    }

    @Override
    public Entry retrieve(final Key key) {
        return this.entries.get(key);
    }

    @Override
    public List<Entry> save(final Key key, final Entry entry)
        throws Exception {
        final List<Entry> removed = this.policy.apply(this.entries);
        removed.forEach(element -> element.tables().forEach(this.keys::remove));
        entry.tables().forEach(
            table -> this.keys
                .computeIfAbsent(table, tbl -> new HashSet<>())
                .add(key)
        );
        this.entries.put(key, entry);
        return removed;
    }

    @Override
    public Entry delete(final Key key) {
        this.keys.values().forEach(element -> element.remove(key));
        return this.entries.remove(key);
    }

    @Override
    public boolean contains(final Key key) {
        return this.entries.containsKey(key);
    }

    @Override
    public List<Entry> invalidate(final Set<String> tables) {
        return tables.stream()
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
