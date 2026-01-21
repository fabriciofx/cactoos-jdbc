/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.prepared;

import com.github.fabriciofx.cactoos.cache.Cache;
import com.github.fabriciofx.cactoos.cache.Entry;
import com.github.fabriciofx.cactoos.cache.Key;
import com.github.fabriciofx.cactoos.cache.Store;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.cache.CacheEntry;
import com.github.fabriciofx.cactoos.jdbc.cache.CacheKey;
import com.github.fabriciofx.cactoos.jdbc.query.Normalized;
import com.github.fabriciofx.cactoos.jdbc.rset.CachedResultSet;
import com.github.fabriciofx.cactoos.jdbc.sql.TableNames;
import com.github.fabriciofx.cactoos.jdbc.table.LinkedTable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Cached.
 * A {@link java.sql.PreparedStatement} decorator for cache query data.
 * @since 0.9.0
 * @checkstyle ParameterNumberCheck (500 lines)
 * @checkstyle IllegalCatchCheck (500 lines)
 */
@SuppressWarnings({"PMD.AvoidCatchingGenericException", "PMD.UnnecessaryLocalRule"})
public final class Cached extends PreparedEnvelope {
    /**
     * A normalized PreparedStatement to be stored into cache.
     */
    private final PreparedStatement stored;

    /**
     * The normalized SQL select.
     */
    private final Normalized normalized;

    /**
     * The cache.
     */
    private final Cache<Query, Table> cache;

    /**
     * Ctor.
     *
     * @param origin Decorated PreparedStatement
     * @param stored PreparedStatement to normalized SQL select
     * @param normalized The normalized select SQL
     * @param cache The cache
     */
    public Cached(
        final PreparedStatement origin,
        final PreparedStatement stored,
        final Normalized normalized,
        final Cache<Query, Table> cache
    ) {
        super(origin);
        this.stored = stored;
        this.normalized = normalized;
        this.cache = cache;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        try {
            final ResultSet result;
            final Key<Query> key = new CacheKey(this.normalized);
            final Store<Query, Table> store = this.cache.store();
            if (store.contains(key)) {
                final Entry<Query, Table> entry = store.retrieve(key);
                result = new CachedResultSet(
                    entry.value().rows(),
                    entry.value().columns()
                );
            } else {
                try (ResultSet rset = this.stored.executeQuery()) {
                    final Table table = new LinkedTable(rset);
                    result = new CachedResultSet(table.rows(), table.columns());
                    store.save(
                        key,
                        new CacheEntry(
                            key,
                            table,
                            new TableNames(this.normalized)
                        )
                    );
                }
            }
            return result;
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }
}
