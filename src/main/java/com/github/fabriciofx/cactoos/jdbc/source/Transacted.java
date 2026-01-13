/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.source;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import org.cactoos.Scalar;

/**
 * Transacted source.
 * A decorator for {@link Source}. Produces a unique not auto commited
 * {@link Session} that must be used in a
 * {@link com.github.fabriciofx.cactoos.jdbc.statement.Transaction}. This
 * connection only can be closed after a commit() or rollback() provided by
 * {@link com.github.fabriciofx.cactoos.jdbc.statement.Transaction}.
 * @since 0.9.0
 */
public final class Transacted implements Source {
    /**
     * Source.
     */
    private final Source origin;

    /**
     * Unique not auto commited connection.
     */
    private final Scalar<Session> cnnx;

    /**
     * Ctor.
     * @param source The original source to be decorated
     */
    public Transacted(final Source source) {
        this.origin = source;
        this.cnnx = new org.cactoos.scalar.Sticky<>(
            () -> {
                final Session session = source.session();
                session.autocommit(false);
                return new com.github.fabriciofx.cactoos.jdbc.session.Transacted(
                    session
                );
            }
        );
    }

    @Override
    public Session session() throws Exception {
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
