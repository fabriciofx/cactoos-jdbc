/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import java.sql.Connection;
import org.cactoos.Scalar;

/**
 * Transacted session.
 * A decorator for {@link Session}. Produces a unique not auto commited
 * {@link java.sql.Connection} that must be used in a
 * {@link com.github.fabriciofx.cactoos.jdbc.statement.Transaction}. This
 * connection only can be closed after a commit() or rollback() provided by
 * {@link com.github.fabriciofx.cactoos.jdbc.statement.Transaction}.
 * @since 0.9.0
 */
public final class Transacted implements Session {
    /**
     * Session.
     */
    private final Session origin;

    /**
     * Unique not auto commited connection.
     */
    private final Scalar<Connection> connexio;

    /**
     * Ctor.
     * @param session The original session to be decorated
     */
    public Transacted(final Session session) {
        this.origin = session;
        this.connexio = new org.cactoos.scalar.Sticky<>(
            () -> {
                final Connection connection = session.connection();
                connection.setAutoCommit(false);
                return new com.github.fabriciofx.cactoos.jdbc.connection.Transacted(
                    connection
                );
            }
        );
    }

    @Override
    public Connection connection() throws Exception {
        return this.connexio.value();
    }

    @Override
    public String url() throws Exception {
        return this.origin.url();
    }

    @Override
    public String username() {
        return this.origin.username();
    }

    @Override
    public String password() {
        return this.origin.password();
    }
}
