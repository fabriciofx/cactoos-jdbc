/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.connection.NoCloseConnection;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

/**
 * No Close Session.
 *
 * @since 0.8.1
 */
@SuppressWarnings(
    {
        "PMD.AvoidCatchingGenericException",
        "PMD.CloseResource"
    }
)
public final class NoClose implements Session, Closeable {
    /**
     * The Session.
     */
    private final Session origin;

    /**
     * List of NoCloseConnections.
     */
    private final List<NoCloseConnection> connections;

    /**
     * Ctor.
     *
     * @param session The Session to be decorated
     */
    public NoClose(final Session session) {
        this(session, new LinkedList<>());
    }

    /**
     * Ctor.
     *
     * @param session The Session to be decorated
     * @param cnncts The list of NoCloseConnection
     */
    public NoClose(
        final Session session,
        final List<NoCloseConnection> cnncts
    ) {
        this.origin = session;
        this.connections = cnncts;
    }

    @Override
    public Connection connection() throws Exception {
        final NoCloseConnection connection = new NoCloseConnection(
            this.origin.connection()
        );
        this.connections.add(connection);
        return connection;
    }

    @Override
    public void close() throws IOException {
        try {
            for (final NoCloseConnection connection : this.connections) {
                final Connection real = connection.value();
                if (!real.isClosed()) {
                    real.close();
                }
            }
            // @checkstyle IllegalCatchCheck (1 line)
        } catch (final Exception ex) {
            throw new IOException(ex);
        }
    }
}
