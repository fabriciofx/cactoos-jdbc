/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.prepared;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import org.cactoos.Text;

/**
 * Cached.
 * A {@link java.sql.PreparedStatement} decorator for cache query data.
 * @since 0.9.0
 * @checkstyle ParameterNumberCheck (500 lines)
 * @checkstyle IllegalCatchCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidCatchingGenericException")
public final class Cached extends PreparedStatementEnvelope {
    /**
     * A PreparedStatement to normalized SQL select.
     */
    private final PreparedStatement prepared;

    /**
     * The normalized SQL select.
     */
    private final Text normalized;

    /**
     * The cache.
     */
    private final Cache<String, ResultSet> cache;

    /**
     * Ctor.
     *
     * @param origin Decorated PreparedStatement
     * @param prepared PreparedStatement to normalized SQL select
     * @param normalized The normalized select SQL
     * @param cache The cache
     */
    public Cached(
        final PreparedStatement origin,
        final PreparedStatement prepared,
        final Text normalized,
        final Cache<String, ResultSet> cache
    ) {
        super(origin);
        this.prepared = prepared;
        this.normalized = normalized;
        this.cache = cache;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        try {
            if (!this.cache.contains(this.normalized.asString())) {
                this.cache.store(
                    this.normalized.asString(),
                    this.prepared.executeQuery()
                );
            }
            final ResultSet rset = this.cache.retrieve(this.normalized.asString());
            final RowSetFactory rsf = RowSetProvider.newFactory();
            final CachedRowSet crs = rsf.createCachedRowSet();
            crs.populate(rset);
            rset.beforeFirst();
            return crs;
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }
}
