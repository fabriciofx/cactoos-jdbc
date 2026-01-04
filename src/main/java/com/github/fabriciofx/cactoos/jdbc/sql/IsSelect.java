/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql;

import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWith;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.cactoos.Scalar;
import org.cactoos.Text;
import org.cactoos.text.TextOf;

/**
 * IsSelect. Checks if a query is a select statement.
 *
 * @since 0.9.0
 */
public final class IsSelect implements Scalar<Boolean> {
    /**
     * The query.
     */
    private final Text sql;

    /**
     * Ctor.
     *
     * @param sql The query
     */
    public IsSelect(final String sql) {
        this(new TextOf(sql));
    }

    /**
     * Ctor.
     *
     * @param sql The query
     */
    public IsSelect(final Text sql) {
        this.sql = sql;
    }

    @Override
    public Boolean value() throws Exception {
        boolean result;
        try {
            final SqlParser parser = SqlParser.create(this.sql.asString());
            final SqlNode stmt = parser.parseStmt();
            result = stmt.getKind() == SqlKind.SELECT
                || stmt.getKind() == SqlKind.WITH
                && ((SqlWith) stmt).body.getKind() == SqlKind.SELECT;
        } catch (final SqlParseException ex) {
            result = false;
        }
        return result;
    }
}
