/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.sql.SqlParsed;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.cactoos.Text;
import org.cactoos.list.ListOf;
import org.cactoos.text.UncheckedText;

/**
 * StatementBatch query.
 *
 * @since 0.1
 */
public final class BatchOf implements Query {
    /**
     * Named SQL query.
     */
    private final Text nmd;

    /**
     * SQL query.
     */
    private final Text sql;

    /**
     * A list of SQL query parameters.
     */
    private final Iterable<Params> parameters;

    /**
     * Ctor.
     * @param sql The SQL query
     * @param prms A list of SQL query parameters
     */
    public BatchOf(final String sql, final Params... prms) {
        this(() -> sql, prms);
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param prms A list of SQL query parameters
     */
    public BatchOf(final Text sql, final Params... prms) {
        this(sql, new ListOf<>(prms));
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param prms A list of SQL query parameters
     */
    public BatchOf(final Text sql, final Iterable<Params> prms) {
        this.nmd = sql;
        this.sql = new SqlParsed(sql, prms.iterator().next());
        this.parameters = prms;
    }

    @Override
    public PreparedStatement prepared(
        final Connection connection
    ) throws Exception {
        final PreparedStatement stmt = connection.prepareStatement(
            this.sql.asString()
        );
        for (final Params prms : this.parameters) {
            prms.prepare(stmt);
            stmt.addBatch();
        }
        return stmt;
    }

    @Override
    public Params params() {
        return null;
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
