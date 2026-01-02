/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql;

import com.github.fabriciofx.cactoos.jdbc.Sql;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlDynamicParam;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriterConfig;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;
import org.apache.calcite.sql.util.SqlShuttle;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.cactoos.Text;
import org.cactoos.text.Sticky;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

/**
 * Parametrized SQL.
 * Parse a SQL changing literals for question mark (dynamic params).
 * @since 0.9.0
 */
public final class ParametrizedSql implements Sql {
    /**
     * Source SQL.
     */
    private final Text origin;

    /**
     * Parametrized SQL.
     */
    private final Text parametrized;

    /**
     * Ctor.
     * @param sql SQL query
     */
    public ParametrizedSql(final String sql) {
        this(new TextOf(sql));
    }

    /**
     * Ctor.
     * @param sql SQL query
     */
    public ParametrizedSql(final Text sql) {
        this.origin = sql;
        this.parametrized = new Sticky(
            () -> {
                final String result;
                if (sql.asString().startsWith("CREATE")) {
                    result = sql.asString();
                } else {
                    final SqlParser.Config config = SqlParser.config()
                        .withConformance(SqlConformanceEnum.DEFAULT)
                        .withQuoting(Quoting.BACK_TICK);
                    final SqlParser parser = SqlParser.create(
                        sql.asString(),
                        config
                    );
                    final SqlNode stmt = parser.parseStmt();
                    final AtomicInteger index = new AtomicInteger(0);
                    final SqlNode replaced = stmt.accept(
                        new SqlShuttle() {
                            @Override
                            public SqlNode visit(final SqlLiteral literal) {
                                return new SqlDynamicParam(
                                    index.getAndIncrement(),
                                    SqlParserPos.ZERO
                                );
                            }
                        }
                    );
                    final SqlDialect dialect = SqlDialect
                        .DatabaseProduct
                        .UNKNOWN
                        .getDialect();
                    final SqlWriterConfig conf = SqlPrettyWriter.config()
                        .withDialect(dialect)
                        .withIndentation(0)
                        .withClauseStartsLine(false)
                        .withSelectListItemsOnSeparateLines(false);
                    final SqlPrettyWriter writer = new SqlPrettyWriter(conf);
                    result = writer
                        .format(replaced)
                        .replaceAll("\\s+", " ")
                        .trim();
                }
                return result;
            }
        );
    }

    @Override
    public String source() {
        return new UncheckedText(this.origin).asString();
    }

    @Override
    public String parsed() throws Exception {
        return this.parametrized.asString();
    }
}
