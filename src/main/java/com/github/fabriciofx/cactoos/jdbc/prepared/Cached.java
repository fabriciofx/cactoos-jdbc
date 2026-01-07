/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.prepared;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.query.Normalized;
import com.github.fabriciofx.cactoos.jdbc.rset.SnapshotResultSet;
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
        final Cache<String, Table> cache
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
            if (this.cache.contains(this.normalized.sql())) {
                final Table table = this.cache.retrieve(this.normalized.sql());
                result = new SnapshotResultSet(table.rows(), table.columns());
            } else {
                try (ResultSet rset = this.stored.executeQuery()) {
                    final Table table = new LinkedTable(rset);
                    result = new SnapshotResultSet(
                        table.rows(),
                        table.columns()
                    );
                    this.cache.store(this.normalized.sql(), table);
                }
            }
            return result;
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }
}
