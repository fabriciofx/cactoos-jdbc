/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import java.sql.Connection;
import org.cactoos.Scalar;
import org.cactoos.scalar.Sticky;

/**
 * Transacted session.
 *
 * <p>Produces a {@link java.sql.Connection} that only closes on commit() or
 * rollback()</p>
 *
 * @since 0.1
 */
public final class Transacted implements Session {
    /**
     * Hold connection.
     */
    private final Scalar<Connection> connexio;

    /**
     * Ctor.
     * @param session Session
     */
    public Transacted(final Session session) {
        this.connexio = new Sticky<>(
            () -> {
                final Connection connection = session.connection();
                connection.setAutoCommit(false);
                return connection;
            }
        );
    }

    @Override
    public Connection connection() throws Exception {
        return this.connexio.value();
    }
}
