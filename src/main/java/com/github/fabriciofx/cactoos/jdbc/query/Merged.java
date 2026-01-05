/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.sql.MergeShuttle;
import com.github.fabriciofx.cactoos.jdbc.sql.Pretty;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.cactoos.Text;
import org.cactoos.text.Sticky;

/**
 * Merged.
 * <p>
 * A decorator for {@link Query} that inlines parameter values into a query.
 *
 * @since 0.9.0
 */
public final class Merged implements Query {
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
     * @param query A {@link Query}
     */
    public Merged(final Query query) {
        this.origin = query;
        this.code = new Sticky(
            () -> {
                final SqlParser.Config config = SqlParser.config()
                    .withConformance(SqlConformanceEnum.DEFAULT)
                    .withQuoting(Quoting.BACK_TICK);
                final SqlParser parser = SqlParser.create(query.sql(), config);
                final SqlNode stmt = parser.parseStmt();
                final SqlNode replaced = stmt.accept(new MergeShuttle(query));
                return new Pretty(replaced).asString();
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
