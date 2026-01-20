/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql;

import com.github.fabriciofx.cactoos.jdbc.Query;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWith;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.cactoos.Scalar;
import org.cactoos.text.TextOf;
import org.cactoos.text.Trimmed;
import org.cactoos.text.Upper;

/**
 * QueryKind.
 *
 * @since 0.9.0
 * @checkstyle CyclomaticComplexityCheck (200 lines)
 * @checkstyle JavaNCSSCheck (200 lines)
 */
@SuppressWarnings({"PMD.CognitiveComplexity", "PMD.NcssCount"})
public final class QueryKind implements Scalar<SqlKind> {
    /**
     * Query.
     */
    private final Query query;

    /**
     * Ctor.
     *
     * @param query A {@link Query}
     */
    public QueryKind(final Query query) {
        this.query = query;
    }

    @Override
    public SqlKind value() throws Exception {
        final String normalized = new Upper(
            new Trimmed(
                new TextOf(this.query.sql())
            )
        ).asString();
        SqlKind kind;
        if (normalized.startsWith("CREATE TABLE")) {
            kind = SqlKind.CREATE_TABLE;
        } else if (normalized.startsWith("CREATE VIEW")) {
            kind = SqlKind.CREATE_VIEW;
        } else if (normalized.startsWith("CREATE INDEX")) {
            kind = SqlKind.CREATE_INDEX;
        } else if (normalized.startsWith("CREATE SCHEMA")) {
            kind = SqlKind.CREATE_SCHEMA;
        } else if (normalized.startsWith("CREATE DOMAIN")) {
            kind = SqlKind.CREATE_SCHEMA;
        } else if (normalized.startsWith("CREATE SEQUENCE")) {
            kind = SqlKind.CREATE_SEQUENCE;
        } else if (normalized.startsWith("CREATE TYPE")) {
            kind = SqlKind.CREATE_TYPE;
        } else if (normalized.startsWith("CREATE FUNCTION")) {
            kind = SqlKind.CREATE_FUNCTION;
        } else if (normalized.startsWith("CREATE PROCEDURE")) {
            kind = SqlKind.CREATE_FUNCTION;
        } else if (normalized.startsWith("CREATE TRIGGER")) {
            kind = SqlKind.CREATE_FUNCTION;
        } else if (normalized.startsWith("ALTER TABLE")) {
            kind = SqlKind.ALTER_TABLE;
        } else if (normalized.startsWith("ALTER VIEW")) {
            kind = SqlKind.ALTER_VIEW;
        } else if (normalized.startsWith("ALTER INDEX")) {
            kind = SqlKind.ALTER_INDEX;
        } else if (normalized.startsWith("ALTER SCHEMA")) {
            kind = SqlKind.ALTER_TABLE;
        } else if (normalized.startsWith("ALTER DOMAIN")) {
            kind = SqlKind.ALTER_TABLE;
        } else if (normalized.startsWith("ALTER SEQUENCE")) {
            kind = SqlKind.ALTER_SEQUENCE;
        } else if (normalized.startsWith("ALTER TYPE")) {
            kind = SqlKind.ALTER_TABLE;
        } else if (normalized.startsWith("DROP TABLE")) {
            kind = SqlKind.DROP_TABLE;
        } else if (normalized.startsWith("DROP VIEW")) {
            kind = SqlKind.DROP_VIEW;
        } else if (normalized.startsWith("DROP INDEX")) {
            kind = SqlKind.DROP_INDEX;
        } else if (normalized.startsWith("DROP SCHEMA")) {
            kind = SqlKind.DROP_SCHEMA;
        } else if (normalized.startsWith("DROP DOMAIN")) {
            kind = SqlKind.DROP_SCHEMA;
        } else if (normalized.startsWith("DROP SEQUENCE")) {
            kind = SqlKind.DROP_SEQUENCE;
        } else if (normalized.startsWith("DROP TYPE")) {
            kind = SqlKind.DROP_TYPE;
        } else if (normalized.startsWith("DROP FUNCTION")) {
            kind = SqlKind.DROP_FUNCTION;
        } else if (normalized.startsWith("DROP PROCEDURE")) {
            kind = SqlKind.DROP_FUNCTION;
        } else if (normalized.startsWith("CREATE TRIGGER")) {
            kind = SqlKind.DROP_FUNCTION;
        } else if (normalized.startsWith("TRUNCATE")) {
            kind = SqlKind.TRUNCATE_TABLE;
        } else {
            final SqlParser.Config config = SqlParser.config()
                .withConformance(SqlConformanceEnum.DEFAULT)
                .withQuoting(Quoting.BACK_TICK);
            final SqlParser parser = SqlParser.create(normalized, config);
            final SqlNode stmt = parser.parseStmt();
            kind = stmt.getKind();
            if (kind == SqlKind.WITH) {
                kind = SqlWith.class.cast(stmt).body.getKind();
            }
        }
        return kind;
    }
}
