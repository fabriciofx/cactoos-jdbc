/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Plan;
import com.github.fabriciofx.cactoos.jdbc.Session;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Transacted.
 * A decorator for Session which allows transactions. This decorator only
 * close the JDBC connection after a commit or rollback.
 * @since 0.9.0
 */
public final class Transacted implements Session {
    /**
     * Session.
     */
    private final Session origin;

    /**
     * Close or not the connection.
     */
    private final AtomicBoolean closeable;

    /**
     * Ctor.
     * @param session A Session
     */
    public Transacted(final Session session) {
        this.origin = session;
        this.closeable = new AtomicBoolean(false);
    }

    @Override
    public PreparedStatement prepared(final Plan plan) throws Exception {
        return this.origin.prepared(plan);
    }

    @Override
    public void autoCommit(final boolean enabled) throws Exception {
        this.origin.autoCommit(false);
    }

    @Override
    public void commit() throws Exception {
        this.origin.commit();
        this.closeable.set(true);
    }

    @Override
    public void rollback() throws Exception {
        this.origin.rollback();
        this.closeable.set(true);
    }

    @Override
    public void close() throws IOException {
        if (this.closeable.get()) {
            this.origin.close();
        }
    }
}
