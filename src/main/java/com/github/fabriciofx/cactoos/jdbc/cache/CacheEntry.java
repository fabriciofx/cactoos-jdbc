/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Table;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.cactoos.Scalar;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.cactoos.scalar.Unchecked;

/**
 * CacheEntry.
 * @since 0.9.0
 */
public final class CacheEntry implements Entry<Query, Table> {
    /**
     * Key.
     */
    private final Key<Query> id;

    /**
     * Table to be stored in the cache.
     */
    private final Table table;

    /**
     * Metadata (tables names).
     */
    private final Map<String, Set<String>> meta;

    /**
     * Ctor.
     * @param key The key
     * @param table The Table
     * @param tables Table names
     */
    public CacheEntry(
        final Key<Query> key,
        final Table table,
        final Scalar<Set<String>> tables
    ) {
        this(
            key,
            table,
            new MapOf<>(
                new MapEntry<>(
                    "tables",
                    new Unchecked<>(tables).value()
                )
            )
        );
    }

    /**
     * Ctor.
     * @param key The key
     * @param table The table to be stored
     * @param meta Metadata associated with this entry.
     */
    public CacheEntry(
        final Key<Query> key,
        final Table table,
        final Map<String, Set<String>> meta
    ) {
        this.id = key;
        this.table = table;
        this.meta = meta;
    }

    @Override
    public Key<Query> key() {
        return this.id;
    }

    @Override
    public Table value() {
        return this.table;
    }

    @Override
    public Iterable<String> metadata(final String entity) {
        return this.meta.getOrDefault(entity, new HashSet<>());
    }
}
