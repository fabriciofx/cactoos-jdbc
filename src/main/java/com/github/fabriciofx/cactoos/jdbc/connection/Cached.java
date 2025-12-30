/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.connection;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.select.IsSelect;
import com.github.fabriciofx.cactoos.jdbc.select.NormalizedSelect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;

/**
 * Cached.
 * A {@link java.sql.Connection} decorator for cache query data.
 * @since 0.9.0
 * @checkstyle IllegalCatchCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidCatchingGenericException")
public final class Cached extends ConnectionEnvelope {
    /**
     * Cache.
     */
    private final Cache<String, CachedRowSet> cache;

    /**
     * Ctor.
     * @param connection The connection
     * @param cache The cache
     */
    public Cached(
        final Connection connection,
        final Cache<String, CachedRowSet> cache
    ) {
        super(connection);
        this.cache = cache;
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql
    ) throws SQLException {
        try {
            final PreparedStatement prepared;
            if (new IsSelect(sql).value()) {
                prepared = new com.github.fabriciofx.cactoos.jdbc.prepared.Cached(
                    super.prepareStatement(sql),
                    super.prepareStatement(new NormalizedSelect(sql).asString()),
                    new NormalizedSelect(sql),
                    this.cache
                );
            } else {
                prepared = super.prepareStatement(sql);
            }
            return prepared;
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }
}
