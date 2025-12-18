/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Timeout query.
 *
 * @since 0.1
 */
public final class Timeout implements Query {
    /**
     * The query to be decorated.
     */
    private final Query origin;

    /**
     * Query timeout time in seconds.
     */
    private final int time;

    /**
     * Ctor.
     * @param query The SQL query
     * @param seconds The timeout time
     */
    public Timeout(final Query query, final int seconds) {
        this.origin = query;
        this.time = seconds;
    }

    @Override
    public PreparedStatement prepared(
        final Connection connection
    ) throws Exception {
        final PreparedStatement stmt = this.origin.prepared(connection);
        stmt.setQueryTimeout(this.time);
        return stmt;
    }

    @Override
    public Params params() {
        return this.origin.params();
    }

    @Override
    public String named() {
        return this.origin.named();
    }

    @Override
    public String asString() throws Exception {
        return this.origin.asString();
    }
}
