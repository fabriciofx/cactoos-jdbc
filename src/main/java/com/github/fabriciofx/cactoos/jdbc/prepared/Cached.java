/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.prepared;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.query.Normalized;
import com.github.fabriciofx.cactoos.jdbc.rset.CachedResultSet;
import com.github.fabriciofx.cactoos.jdbc.table.LinkedTable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.cactoos.Func;

/**
 * Cached.
 * A {@link java.sql.PreparedStatement} decorator for cache query data.
 * @since 0.9.0
 * @checkstyle ParameterNumberCheck (500 lines)
 * @checkstyle IllegalCatchCheck (500 lines)
 */
@SuppressWarnings({"PMD.AvoidCatchingGenericException", "PMD.CloseResource"})
public final class Cached extends PreparedStatementEnvelope {
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
    private final Cache<String, Table> cache;

    /**
     * Hash function.
     */
    private final Func<Query, String> hash;

    /**
     * Ctor.
     *
     * @param origin Decorated PreparedStatement
     * @param stored PreparedStatement to normalized SQL select
     * @param normalized The normalized select SQL
     * @param cache The cache
     * @param hash The hash function
     */
    public Cached(
        final PreparedStatement origin,
        final PreparedStatement stored,
        final Normalized normalized,
        final Cache<String, Table> cache,
        final Func<Query, String> hash
    ) {
        super(origin);
        this.stored = stored;
        this.normalized = normalized;
        this.cache = cache;
        this.hash = hash;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        try {
            final ResultSet result;
            final String key = this.hash.apply(this.normalized);
            if (this.cache.contains(key)) {
                final Table table = this.cache.retrieve(key);
                result = new CachedResultSet(table.rows(), table.columns());
            } else {
                try (ResultSet rset = this.stored.executeQuery()) {
                    final Table table = new LinkedTable(rset);
                    result = new CachedResultSet(
                        table.rows(),
                        table.columns()
                    );
                    this.cache.store(key, table);
                }
            }
            return result;
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }
}
