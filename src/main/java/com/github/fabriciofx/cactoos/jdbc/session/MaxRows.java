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
 * MaxRows.
 * A decorator for Session that sets the max of rows.
 * @since 0.9.0
 */
public final class MaxRows implements Session {
    /**
     * Session.
     */
    private final Session origin;

    /**
     * Max of rows.
     */
    private final int max;

    /**
     * Ctor.
     * @param origin A session
     * @param max The max number of rows
     */
    public MaxRows(final Session origin, final int max) {
        this.origin = origin;
        this.max = max;
    }

    @Override
    public PreparedStatement prepared(final Plan plan) throws Exception {
        final PreparedStatement stmt = this.origin.prepared(plan);
        stmt.setMaxRows(this.max);
        return stmt;
    }

    @Override
    public void autocommit(final boolean enabled) throws Exception {
        this.origin.autocommit(enabled);
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
