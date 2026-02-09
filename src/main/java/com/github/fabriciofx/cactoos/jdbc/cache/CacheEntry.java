/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.cache.Entry;
import com.github.fabriciofx.cactoos.cache.Key;
import com.github.fabriciofx.cactoos.cache.Metadata;
import com.github.fabriciofx.cactoos.cache.entry.EntryOf;
import com.github.fabriciofx.cactoos.cache.metadata.MetadataOf;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Table;
import java.util.Set;
import org.cactoos.Scalar;
import org.cactoos.map.MapEntry;
import org.cactoos.scalar.Unchecked;

/**
 * CacheEntry.
 * @since 0.9.0
 */
public final class CacheEntry implements Entry<Query, Table> {
    /**
     * Entry.
     */
    private final Entry<Query, Table> entry;

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
            new MetadataOf(
                new MapEntry<>("tables", new Unchecked<>(tables).value())
            )
        );
    }

    /**
     * Ctor.
     * @param key The key
     * @param table The Table
     * @param metadata Metadata (table names)
     */
    public CacheEntry(
        final Key<Query> key,
        final Table table,
        final Metadata metadata
    ) {
        this(new EntryOf<>(key, table, metadata));
    }

    /**
     * Ctor.
     * @param entry An entry
     */
    public CacheEntry(final Entry<Query, Table> entry) {
        this.entry = entry;
    }

    @Override
    public Key<Query> key() {
        return this.entry.key();
    }

    @Override
    public Table value() {
        return this.entry.value();
    }

    @Override
    public Metadata metadata() {
        return this.entry.metadata();
    }

    @Override
    public boolean valid() {
        return true;
    }

    @Override
    public int size() {
        return this.entry.size();
    }
}
