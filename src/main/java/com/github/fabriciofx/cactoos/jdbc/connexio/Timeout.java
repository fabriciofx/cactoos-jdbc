/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.connexio;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Query;
import java.io.IOException;
import java.sql.PreparedStatement;

/**
 * Timeout.
 * Decorator for Connexio that configures a timeout.
 * @since 0.9.0
 */
public final class Timeout implements Connexio {
    /**
     * Connexio.
     */
    private final Connexio origin;

    /**
     * The time in seconds.
     */
    private final int seconds;

    /**
     * Ctor.
     * @param connexio The connexio
     * @param seconds The time in seconds
     */
    public Timeout(final Connexio connexio, final int seconds) {
        this.origin = connexio;
        this.seconds = seconds;
    }

    @Override
    public PreparedStatement prepared(final Query query) throws Exception {
        final PreparedStatement stmt = this.origin.prepared(query);
        stmt.setQueryTimeout(this.seconds);
        return stmt;
    }

    @Override
    public PreparedStatement batched(final Query query) throws Exception {
        final PreparedStatement stmt = this.origin.batched(query);
        stmt.setQueryTimeout(this.seconds);
        return stmt;
    }

    @Override
    public PreparedStatement keyed(final Query query)
        throws Exception {
        final PreparedStatement stmt = this.origin.keyed(query);
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
