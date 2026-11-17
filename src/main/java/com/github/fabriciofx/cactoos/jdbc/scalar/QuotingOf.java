/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.scalar;

import org.apache.calcite.avatica.util.Quoting;
import org.cactoos.Scalar;

/**
 * QuotingOf.
 *
 * <p>The identifier quotes a SQL query is written with: back ticks when it
 * uses them, as MySQL does, and double quotes otherwise, as the SQL standard,
 * PostgreSQL, H2, Oracle and SQLite do. Parsing and printing the query with
 * them keeps the SQL valid for the database it was written for.</p>
 *
 * @since 0.9.0
 */
public final class QuotingOf implements Scalar<Quoting> {

    /**
     * SQL query.
     */
    private final String sql;

    /**
     * Ctor.
     *
     * @param sql A SQL query
     */
    public QuotingOf(final String sql) {
        this.sql = sql;
    }

    @Override
    public Quoting value() {
        final Quoting quoting;
        if (this.sql.indexOf('`') >= 0) {
            quoting = Quoting.BACK_TICK;
        } else {
            quoting = Quoting.DOUBLE_QUOTE;
        }
        return quoting;
    }
}
