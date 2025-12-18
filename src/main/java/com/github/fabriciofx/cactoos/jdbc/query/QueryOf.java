/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Param;
import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import com.github.fabriciofx.cactoos.jdbc.sql.SqlParsed;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.cactoos.Text;
import org.cactoos.text.UncheckedText;

/**
 * Simple parameters query.
 *
 * @since 0.1
 */
public final class QueryOf implements Query {
    /**
     * Named SQL query.
     */
    private final Text nmd;

    /**
     * SQL query.
     */
    private final Text sql;

    /**
     * SQL query parameters.
     */
    private final Params parameters;

    /**
     * Ctor.
     * @param sql The SQL query
     * @param prms SQL query parameters
     */
    public QueryOf(final String sql, final Param... prms) {
        this(() -> sql, prms);
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param prms An array of SQL query parameters
     */
    public QueryOf(final Text sql, final Param... prms) {
        this(sql, new ParamsOf(prms));
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param prms SQL query parameters
     */
    public QueryOf(final Text sql, final Params prms) {
        this.nmd = sql;
        this.sql = new SqlParsed(sql, prms);
        this.parameters = prms;
    }

    @Override
    public PreparedStatement prepared(
        final Connection connection
    ) throws Exception {
        final PreparedStatement stmt = connection.prepareStatement(
            this.sql.asString()
        );
        this.parameters.prepare(stmt);
        return stmt;
    }

    @Override
    public Params params() {
        return this.parameters;
    }

    @Override
    public String named() {
        return new UncheckedText(this.nmd).asString();
    }

    @Override
    public String asString() throws Exception {
        return this.sql.asString();
    }
}
