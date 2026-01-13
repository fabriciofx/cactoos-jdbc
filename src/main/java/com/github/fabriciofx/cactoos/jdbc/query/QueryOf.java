/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import org.cactoos.Text;
import org.cactoos.list.ListOf;
import org.cactoos.text.TextOf;

/**
 * QueryOf.
 * A simple {@link Query} representation.
 * @since 0.1
 */
public final class QueryOf implements Query {
    /**
     * SQL code.
     */
    private final Text code;

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
        this.code = sql;
    }

    @Override
    public Iterable<Params> params() {
        return new ListOf<>();
    }

    @Override
    public String sql() throws Exception {
        return this.code.asString();
    }
}
