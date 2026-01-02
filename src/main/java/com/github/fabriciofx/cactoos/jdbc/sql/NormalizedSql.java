/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql;

import com.github.fabriciofx.cactoos.jdbc.Sql;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.SqlWith;
import org.apache.calcite.sql.SqlWriterConfig;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.cactoos.Text;
import org.cactoos.text.Sticky;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

/**
 * NormalizedSelect. Normalize (transform to the canonical query) a select
 * query.
 *
 * @since 0.9.0
 */
public final class NormalizedSql implements Sql {
    /**
     * Source SQL.
     */
    private final Text origin;

    /**
     * Normalized SQL.
     */
    private final Text normalized;

    /**
     * Ctor.
     *
     * @param sql The query
     */
    public NormalizedSql(final String sql) {
        this(new TextOf(sql));
    }

    /**
     * Ctor.
     *
     * @param sql The query
     */
    public NormalizedSql(final Text sql) {
        this.origin = sql;
        this.normalized = new Sticky(
            () -> {
                final String result;
                if (sql.asString().startsWith("SELECT")
                    || sql.asString().startsWith("WITH")
                ) {
                    final SqlParser.Config config = SqlParser.config()
                        .withConformance(SqlConformanceEnum.DEFAULT)
                        .withQuoting(Quoting.BACK_TICK);
                    final SqlParser parser = SqlParser.create(
                        sql.asString(),
                        config
                    );
                    final SqlNode stmt = parser.parseStmt();
                    final SqlSelect select;
                    if (stmt.getKind() == SqlKind.SELECT) {
                        select = (SqlSelect) stmt;
                    } else if (stmt.getKind() == SqlKind.WITH) {
                        select = (SqlSelect) ((SqlWith) stmt).body;
                    } else {
                        throw new IllegalArgumentException(
                            "Not a SELECT statement"
                        );
                    }
                    final SqlSelect normal = new SqlSelect(
                        select.getParserPosition(),
                        new SqlNodeList(select.getParserPosition()),
                        SqlNodeList.SINGLETON_STAR,
                        select.getFrom(),
                        select.getWhere(),
                        select.getGroup(),
                        select.getHaving(),
                        select.getWindowList(),
                        select.getQualify(),
                        select.getOrderList(),
                        select.getOffset(),
                        select.getFetch(),
                        select.getHints()
                    );
                    normal.setOrderBy(null);
                    normal.setOffset(null);
                    normal.setFetch(null);
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
                        .format(normal)
                        .replaceAll("\\s+", " ")
                        .trim();
                } else {
                    result = sql.asString();
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
        return this.normalized.asString();
    }
}
