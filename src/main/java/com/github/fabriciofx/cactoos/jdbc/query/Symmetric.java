/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.sql.Pretty;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlDelete;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.cactoos.Text;
import org.cactoos.text.Sticky;

/**
 * Symmetric.
 * A decorator for {@link Query} that produces a symmetric (select) from
 * delete.
 * @since 0.9.0
 */
public final class Symmetric implements Query {
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
     * @param query A {@link Query}
     */
    public Symmetric(final Query query) {
        this.origin = query;
        this.code = new Sticky(
            () -> {
                final SqlParser.Config config = SqlParser.config()
                    .withConformance(SqlConformanceEnum.DEFAULT)
                    .withQuoting(Quoting.BACK_TICK);
                final SqlParser parser = SqlParser.create(
                    query.sql(),
                    config
                );
                final SqlDelete delete = (SqlDelete) parser.parseStmt();
                final SqlNode from = delete.getTargetTable();
                final SqlNode where = delete.getCondition();
                final SqlSelect select = new SqlSelect(
                    SqlParserPos.ZERO,
                    new SqlNodeList(SqlParserPos.ZERO),
                    SqlNodeList.SINGLETON_STAR,
                    from,
                    where,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                );
                return new Pretty(select).asString();
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
