/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Plan;
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
    public PreparedStatement prepared(final Plan plan) throws Exception {
        return plan.prepare(this.connection);
    }

    @Override
    public void autocommit(final boolean enabled) throws Exception {
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
