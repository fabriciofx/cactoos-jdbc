/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * JdbcSession.
 * Encapsulate the {@link java.sql.Connection}.
 * @since 0.9.0
 */
public final class JdbcSession implements Session {
    /**
     * JDBC Connection.
     */
    private final Connection connection;

    /**
     * Ctor.
     * @param connection A JDBC connection
     */
    public JdbcSession(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public PreparedStatement prepared(final Query query) throws Exception {
        final PreparedStatement stmt = this.connection.prepareStatement(
            query.sql()
        );
        for (final Params params : query.params()) {
            params.prepare(stmt);
        }
        return stmt;
    }

    @Override
    public PreparedStatement batched(final Query query) throws Exception {
        final PreparedStatement stmt = this.connection.prepareStatement(
            query.sql()
        );
        for (final Params params : query.params()) {
            params.prepare(stmt);
            stmt.addBatch();
        }
        return stmt;
    }

    @Override
    public PreparedStatement keyed(final Query query) throws Exception {
        final PreparedStatement stmt = this.connection.prepareStatement(
            query.sql(),
            java.sql.Statement.RETURN_GENERATED_KEYS
        );
        for (final Params params : query.params()) {
            params.prepare(stmt);
        }
        return stmt;
    }

    @Override
    public void autoCommit(final boolean enabled) throws Exception {
        this.connection.setAutoCommit(enabled);
    }

    @Override
    public void commit() throws Exception {
        this.connection.commit();
    }

    @Override
    public void rollback() throws Exception {
        this.connection.rollback();
    }

    @Override
    public void close() throws IOException {
        try {
            this.connection.close();
        } catch (final SQLException ex) {
            throw new IOException(ex);
        }
    }
}
