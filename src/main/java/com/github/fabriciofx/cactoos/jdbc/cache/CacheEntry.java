/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.cache.Key;
import com.github.fabriciofx.cactoos.cache.entry.EntryEnvelope;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Table;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.cactoos.Scalar;
import org.cactoos.list.ListOf;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.cactoos.scalar.Unchecked;

/**
 * CacheEntry.
 * @since 0.9.0
 */
public final class CacheEntry extends EntryEnvelope<Query, Table> {
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
                    new ListOf<>(new Unchecked<>(tables).value())
                )
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
        final Map<String, List<String>> metadata
    ) {
        super(key, table, metadata);
    }
}
