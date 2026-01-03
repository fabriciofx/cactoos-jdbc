/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Session;
import org.cactoos.Scalar;

/**
 * Transacted session.
 * A decorator for {@link Session}. Produces a unique not auto commited
 * {@link Connexio} that must be used in a
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
    private final Scalar<Connexio> cnnx;

    /**
     * Ctor.
     * @param session The original session to be decorated
     */
    public Transacted(final Session session) {
        this.origin = session;
        this.cnnx = new org.cactoos.scalar.Sticky<>(
            () -> {
                final Connexio conn = session.connexio();
                conn.autoCommit(false);
                return new com.github.fabriciofx.cactoos.jdbc.connexio.Transacted(
                    conn
                );
            }
        );
    }

    @Override
    public Connexio connexio() throws Exception {
        return this.cnnx.value();
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
