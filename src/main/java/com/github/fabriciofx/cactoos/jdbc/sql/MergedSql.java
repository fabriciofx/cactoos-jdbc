/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql;

import com.github.fabriciofx.cactoos.jdbc.Param;
import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Sql;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlDynamicParam;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriterConfig;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;
import org.apache.calcite.sql.util.SqlShuttle;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.cactoos.Text;
import org.cactoos.text.Sticky;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

/**
 * Merge SQL. Parse SQL changing named SQL for the values of parameters.
 *
 * @since 0.9.0
 */
public final class MergedSql implements Sql {
    /**
     * Source SQL.
     */
    private final Text origin;

    /**
     * Merged SQL.
     */
    private final Text merged;

    /**
     * Ctor.
     *
     * @param sql SQL query
     * @param params SQL query parameters
     */
    public MergedSql(final String sql, final Param... params) {
        this(() -> sql, new ParamsOf(params));
    }

    /**
     * Ctor.
     *
     * @param sql SQL query
     * @param params SQL query parameters
     */
    public MergedSql(final String sql, final Params params) {
        this(new TextOf(sql), params);
    }

    /**
     * Ctor.
     *
     * @param sql SQL query
     * @param params SQL query parameters
     */
    public MergedSql(final Text sql, final Param... params) {
        this(sql, new ParamsOf(params));
    }

    /**
     * Ctor.
     *
     * @param sql SQL query
     * @param params SQL query parameters
     */
    public MergedSql(final Text sql, final Params params) {
        this.origin = sql;
        this.merged = new Sticky(
            () -> {
                final String result;
                if (sql.asString().startsWith("CREATE")) {
                    result = sql.asString();
                } else {
                    final String named = new NamedSql(sql, params).parsed();
                    final SqlParser.Config config = SqlParser.config()
                        .withConformance(SqlConformanceEnum.DEFAULT)
                        .withQuoting(Quoting.BACK_TICK);
                    final SqlParser parser = SqlParser.create(named, config);
                    final SqlNode stmt = parser.parseStmt();
                    final SqlNode replaced = stmt.accept(
                        new SqlShuttle() {
                            @Override
                            public SqlNode visit(final SqlDynamicParam mark) {
                                final Param param = params.param(mark.getIndex());
                                return param.value(mark.getParserPosition());
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
        return this.merged.asString();
    }
}
