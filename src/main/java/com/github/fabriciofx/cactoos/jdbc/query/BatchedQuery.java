/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Sql;
import com.github.fabriciofx.cactoos.jdbc.sql.PositionedSql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.cactoos.Text;
import org.cactoos.list.ListOf;

/**
 * BatchedQuery.
 *
 * @since 0.9.0
 */
@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
public final class BatchedQuery implements Query {
    /**
     * SQL query.
     */
    private final Sql sql;

    /**
     * A list of SQL query parameters.
     */
    private final Iterable<Params> parameters;

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public BatchedQuery(final String sql, final Params... params) {
        this(() -> sql, params);
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public BatchedQuery(final Text sql, final Params... params) {
        this(sql, new ListOf<>(params));
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public BatchedQuery(final Text sql, final Iterable<Params> params) {
        this(new PositionedSql(sql, params.iterator().next()), params);
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public BatchedQuery(final Sql sql, final Iterable<Params> params) {
        this.sql = sql;
        this.parameters = params;
    }

    @Override
    public PreparedStatement prepared(
        final Connection connection
    ) throws Exception {
        final PreparedStatement stmt = connection.prepareStatement(
            this.sql.parsed()
        );
        for (final Params params : this.parameters) {
            params.prepare(stmt);
            stmt.addBatch();
        }
        return stmt;
    }

    @Override
    public Params params() {
        return null;
    }

    @Override
    public Sql sql() {
        return this.sql;
    }

    @Override
    public String asString() throws Exception {
        return this.sql.parsed();
    }
}
