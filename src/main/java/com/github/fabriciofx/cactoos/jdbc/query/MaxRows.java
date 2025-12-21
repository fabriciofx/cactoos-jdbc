/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Sql;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Max rows per query.
 *
 * @since 0.1
 */
public final class MaxRows implements Query {
    /**
     * The query to be decorated.
     */
    private final Query origin;

    /**
     * Number of rows per query.
     */
    private final int rows;

    /**
     * Ctor.
     * @param query The SQL query
     * @param max The max number of rows
     */
    public MaxRows(final Query query, final int max) {
        this.origin = query;
        this.rows = max;
    }

    @Override
    public PreparedStatement prepared(
        final Connection connection
    ) throws Exception {
        final PreparedStatement stmt = this.origin.prepared(connection);
        stmt.setMaxRows(this.rows);
        return stmt;
    }

    @Override
    public Params params() {
        return this.origin.params();
    }

    @Override
    public Sql sql() {
        return this.origin.sql();
    }

    @Override
    public String asString() throws Exception {
        return this.origin.asString();
    }
}
