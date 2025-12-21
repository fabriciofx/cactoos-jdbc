/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Param;
import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import com.github.fabriciofx.cactoos.jdbc.sql.ParsedSql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.cactoos.Text;
import org.cactoos.text.UncheckedText;

/**
 * Keyed query.
 *
 * @since 0.1
 */
public final class WithKey implements Query {
    /**
     * Named SQL query.
     */
    private final Text nmd;

    /**
     * SQL query.
     */
    private final Text sql;

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
     * @param prms SQL query parameters
     */
    public WithKey(final String sql, final Param... prms) {
        this(() -> sql, prms);
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param prms SQL query parameters
     */
    public WithKey(final Text sql, final Param... prms) {
        this(sql, "id", prms);
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param pknm The primary key name
     * @param prms SQL query parameters
     */
    public WithKey(
        final Text sql,
        final String pknm,
        final Param... prms
    ) {
        this.nmd = sql;
        this.sql = new ParsedSql(sql, prms);
        this.key = pknm;
        this.parameters = new ParamsOf(prms);
    }

    @Override
    public PreparedStatement prepared(
        final Connection connection
    ) throws Exception {
        final PreparedStatement stmt = connection.prepareStatement(
            this.sql.asString(),
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
    public String named() {
        return new UncheckedText(this.nmd).asString();
    }

    @Override
    public String asString() throws Exception {
        return this.sql.asString();
    }
}
