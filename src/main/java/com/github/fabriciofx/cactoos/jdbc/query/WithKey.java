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
 * Keyed query.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
public final class WithKey implements Query {
    /**
     * SQL query.
     */
    private final Sql sql;

    /**
     * Primary key's name.
     */
    private final String key;

    /**
     * SQL query parameters.
     */
    private final Params parameters;

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params SQL query parameters
     */
    public WithKey(final String sql, final Param... params) {
        this(() -> sql, params);
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params SQL query parameters
     */
    public WithKey(final Text sql, final Param... params) {
        this(sql, "id", params);
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param pkname The primary key name
     * @param params SQL query parameters
     */
    public WithKey(
        final Text sql,
        final String pkname,
        final Param... params
    ) {
        this(new PositionedSql(sql, params), pkname, params);
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param pkname The primary key name
     * @param params SQL query parameters
     */
    public WithKey(
        final Sql sql,
        final String pkname,
        final Param... params
    ) {
        this.sql = sql;
        this.key = pkname;
        this.parameters = new ParamsOf(params);
    }

    @Override
    public PreparedStatement prepared(
        final Connection connection
    ) throws Exception {
        final PreparedStatement stmt = connection.prepareStatement(
            this.sql.parse(),
            new String[]{this.key}
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
