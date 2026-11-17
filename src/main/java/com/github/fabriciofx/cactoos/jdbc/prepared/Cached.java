/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.prepared;

import com.github.fabriciofx.cactoos.cache.Cache;
import com.github.fabriciofx.cactoos.cache.Entry;
import com.github.fabriciofx.cactoos.cache.Key;
import com.github.fabriciofx.cactoos.cache.Store;
import com.github.fabriciofx.cactoos.cache.key.KeyOf;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.cache.CacheEntry;
import com.github.fabriciofx.cactoos.jdbc.query.Normalized;
import com.github.fabriciofx.cactoos.jdbc.rset.CachedResultSet;
import com.github.fabriciofx.cactoos.jdbc.scalar.HasViews;
import com.github.fabriciofx.cactoos.jdbc.scalar.TableNames;
import com.github.fabriciofx.cactoos.jdbc.table.ArrayedTable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Cached.
 * A {@link java.sql.PreparedStatement} decorator for cache query data.
 *
 * @since 0.9.0
 * @checkstyle IllegalCatchCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidCatchingGenericException")
public final class Cached extends PreparedEnvelope {

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
     * @param normalized The normalized select SQL, used as the cache key
     * @param cache The cache
     */
    public Cached(
        final PreparedStatement origin,
        final Normalized normalized,
        final Cache<Query, Table> cache
    ) {
        super(origin);
        this.normalized = normalized;
        this.cache = cache;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        try {
            final ResultSet result;
            final Key<Query> key = new KeyOf<>(this.normalized);
            final Store<Query, Table> store = this.cache.store();
            final boolean transacted = !this.getConnection().getAutoCommit();
            if (transacted) {
                result = super.executeQuery();
            } else if (store.contains(key)) {
                final Entry<Query, Table> entry = store.retrieve(key);
                result = new CachedResultSet(
                    entry.value().rows(),
                    entry.value().columns()
                );
            } else if (
                new HasViews(
                    this.getConnection(),
                    new TableNames(this.normalized)
                ).value()
            ) {
                result = super.executeQuery();
            } else {
                try (ResultSet rset = super.executeQuery()) {
                    final Table table = new ArrayedTable(rset);
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
