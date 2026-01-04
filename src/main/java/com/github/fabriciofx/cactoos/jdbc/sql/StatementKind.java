/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql;

import java.util.Locale;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWith;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.cactoos.Scalar;
import org.cactoos.Text;
import org.cactoos.text.TextOf;
import org.cactoos.text.Trimmed;
import org.cactoos.text.Upper;

/**
 * StatementKind.
 * @since 0.9.0
 */
public final class StatementKind implements Scalar<SqlKind> {
    /**
     * The query.
     */
    private final Text sql;

    /**
     * Ctor.
     *
     * @param sql The query
     */
    public StatementKind(final String sql) {
        this(new TextOf(sql));
    }

    /**
     * Ctor.
     *
     * @param sql The query
     */
    public StatementKind(final Text sql) {
        this.sql = sql;
    }

    @Override
    public SqlKind value() throws Exception {
        final String normalized = new Upper(new Trimmed(this.sql), Locale.ROOT)
            .asString();
        SqlKind kind;
        if (normalized.startsWith("CREATE TABLE")) {
            kind = SqlKind.CREATE_TABLE;
        } else {
            final SqlParser.Config config = SqlParser.config()
                .withConformance(SqlConformanceEnum.DEFAULT)
                .withQuoting(Quoting.BACK_TICK);
            final SqlParser parser = SqlParser.create(
                this.sql.asString(),
                config
            );
            final SqlNode stmt = parser.parseStmt();
            kind = stmt.getKind();
            if (kind == SqlKind.WITH) {
                kind = ((SqlWith) stmt).body.getKind();
            }
        }
        return kind;
    }
}
