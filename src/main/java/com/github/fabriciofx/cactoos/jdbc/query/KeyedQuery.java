/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Param;
import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Sql;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import com.github.fabriciofx.cactoos.jdbc.sql.PositionedSql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.cactoos.Text;

/**
 * KeyedQuery.
 *
 * @since 0.9.0
 */
@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
public final class KeyedQuery implements Query {
    /**
     * SQL query.
     */
    private final Sql sql;

    /**
     * SQL query parameters.
     */
    private final Params parameters;

    /**
     * Ctor.
     * @param sql The SQL query code
     * @param params SQL query parameters
     */
    public KeyedQuery(final String sql, final Param... params) {
        this(() -> sql, params);
    }

    /**
     * Ctor.
     * @param sql The SQL query code
     * @param params An array of SQL query parameters
     */
    public KeyedQuery(final Text sql, final Param... params) {
        this(sql, new ParamsOf(params));
    }

    /**
     * Ctor.
     * @param sql The SQL query code
     * @param params SQL query parameters
     */
    public KeyedQuery(final Text sql, final Params params) {
        this(new PositionedSql(sql, params), params);
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params SQL query parameters
     */
    public KeyedQuery(final Sql sql, final Params params) {
        this.sql = sql;
        this.parameters = params;
    }

    @Override
    public PreparedStatement prepared(
        final Connection connection
    ) throws Exception {
        final PreparedStatement stmt = connection.prepareStatement(
            this.sql.parse(),
            java.sql.Statement.RETURN_GENERATED_KEYS
        );
        this.parameters.prepare(stmt);
        return stmt;
    }

    @Override
    public Params params() {
        return this.parameters;
    }

    @Override
    public Sql sql() {
        return this.sql;
    }

    @Override
    public String asString() throws Exception {
        return this.sql.parse();
    }
}
