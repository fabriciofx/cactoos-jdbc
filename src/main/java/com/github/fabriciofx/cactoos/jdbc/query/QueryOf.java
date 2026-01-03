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
import com.github.fabriciofx.cactoos.jdbc.sql.NamedSql;
import org.cactoos.Text;
import org.cactoos.list.ListOf;
import org.cactoos.text.TextOf;

/**
 * Simple parametrized query.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
public final class QueryOf implements Query {
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
     */
    public QueryOf(final String sql) {
        this(new TextOf(sql));
    }

    /**
     * Ctor.
     * @param sql The SQL query
     */
    public QueryOf(final Text sql) {
        this(sql, new ParamsOf());
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public QueryOf(final String sql, final Param... params) {
        this(sql, new ParamsOf(params));
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public QueryOf(final Text sql, final Param... params) {
        this(sql, new ParamsOf(params));
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public QueryOf(final String sql, final Params... params) {
        this(new TextOf(sql), params);
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public QueryOf(final Text sql, final Params... params) {
        this(sql, new ListOf<>(params));
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public QueryOf(final Text sql, final Iterable<Params> params) {
        this(new NamedSql(sql, params.iterator().next()), params);
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public QueryOf(final Sql sql, final Iterable<Params> params) {
        this.sql = sql;
        this.parameters = params;
    }

    @Override
    public Iterable<Params> params() {
        return this.parameters;
    }

    @Override
    public Sql sql() {
        return this.sql;
    }
}
