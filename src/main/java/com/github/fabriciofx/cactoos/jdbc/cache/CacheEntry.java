/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Table;
import java.util.Set;
import org.cactoos.Scalar;
import org.cactoos.scalar.Unchecked;

/**
 * CacheEntry.
 * @since 0.9.0
 */
public final class CacheEntry implements Entry {
    /**
     * Key.
     */
    private final Key id;

    /**
     * Table to be stored in the cache.
     */
    private final Table tbl;

    /**
     * Table names associated with the table.
     */
    private final Unchecked<Set<String>> tbls;

    /**
     * Ctor.
     * @param key The key
     * @param table The Table
     * @param tables Table names
     */
    public CacheEntry(
        final Key key,
        final Table table,
        final Scalar<Set<String>> tables
    ) {
        this(key, table, new Unchecked<>(tables));
    }

    /**
     * Ctor.
     * @param key The key
     * @param table The table to be stored
     * @param tables The table names associated with the table
     */
    public CacheEntry(
        final Key key,
        final Table table,
        final Unchecked<Set<String>> tables
    ) {
        this.id = key;
        this.tbl = table;
        this.tbls = tables;
    }

    @Override
    public Key key() {
        return this.id;
    }

    @Override
    public Table value() {
        return this.tbl;
    }

    @Override
    public Set<String> tables() {
        return this.tbls.value();
    }
}
