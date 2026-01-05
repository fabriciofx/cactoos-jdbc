/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.sql.Pretty;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.SqlWith;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.cactoos.Text;
import org.cactoos.text.Sticky;

/**
 * Normalized.
 * <p>
 * A decorator for {@link Query} that transform a select query into its
 * canonical form.
 *
 * @since 0.9.0
 */
public final class Normalized implements Query {
    /**
     * Query.
     */
    private final Query origin;

    /**
     * SQL code.
     */
    private final Text code;

    /**
     * Ctor.
     *
     * @param query A query
     */
    public Normalized(final Query query) {
        this.origin = query;
        this.code = new Sticky(
            () -> {
                final SqlParser.Config config = SqlParser.config()
                    .withConformance(SqlConformanceEnum.DEFAULT)
                    .withQuoting(Quoting.BACK_TICK);
                final SqlParser parser = SqlParser.create(query.sql(), config);
                final SqlNode stmt = parser.parseStmt();
                final SqlSelect select;
                if (stmt.getKind() == SqlKind.SELECT) {
                    select = (SqlSelect) stmt;
                } else if (stmt.getKind() == SqlKind.WITH) {
                    select = (SqlSelect) ((SqlWith) stmt).body;
                } else {
                    throw new IllegalArgumentException(
                        "This query is not a SELECT statement"
                    );
                }
                final SqlSelect canonical = new SqlSelect(
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
                canonical.setOrderBy(null);
                canonical.setOffset(null);
                canonical.setFetch(null);
                return new Pretty(canonical).asString();
            }
        );
    }

    @Override
    public Iterable<Params> params() {
        return this.origin.params();
    }

    @Override
    public String sql() throws Exception {
        return this.code.asString();
    }
}
