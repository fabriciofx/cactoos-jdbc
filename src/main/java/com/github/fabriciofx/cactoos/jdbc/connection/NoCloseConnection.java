/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;
import org.cactoos.Scalar;

/**
 * NoCloseConnection.
 *
 * @since 0.8.1
 */
public final class NoCloseConnection extends ConnectionEnvelope implements
    Scalar<Connection> {
    /**
     * The Connection.
     */
    private final Connection origin;

    /**
     * Ctor.
     *
     * @param connection Original connection to be decorated
     */
    public NoCloseConnection(final Connection connection) {
        super(connection);
        this.origin = connection;
    }

    @Override
    public void close() throws SQLException {
        // No close connection. This method is empty on purpose
    }

    @Override
    public Connection value() throws Exception {
        return this.origin;
    }
}
