/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Plan;
import com.github.fabriciofx.cactoos.jdbc.Session;
import java.io.IOException;
import java.sql.PreparedStatement;

/**
 * Timeout.
 * Decorator for Session that configures a timeout.
 * @since 0.9.0
 */
public final class Timeout implements Session {
    /**
     * Session.
     */
    private final Session origin;

    /**
     * The time in seconds.
     */
    private final int seconds;

    /**
     * Ctor.
     * @param session The session
     * @param seconds The time in seconds
     */
    public Timeout(final Session session, final int seconds) {
        this.origin = session;
        this.seconds = seconds;
    }

    @Override
    public PreparedStatement prepared(final Plan plan) throws Exception {
        final PreparedStatement stmt = this.origin.prepared(plan);
        stmt.setQueryTimeout(this.seconds);
        return stmt;
    }

    @Override
    public void autoCommit(final boolean enabled) throws Exception {
        this.origin.autoCommit(enabled);
    }

    @Override
    public void commit() throws Exception {
        this.origin.commit();
    }

    @Override
    public void rollback() throws Exception {
        this.origin.rollback();
    }

    @Override
    public void close() throws IOException {
        this.origin.close();
    }
}
