/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.text.Pretty;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.dialect.MysqlSqlDialect;
import org.apache.calcite.sql.parser.SqlParser;
import org.cactoos.Text;
import org.cactoos.text.Sticky;

/**
 * MysqlPaginated.
 *
 * <p>A decorator for {@link Query} that encapsulate a query to
 * paginated form for MySQL, which uses LIMIT and OFFSET instead of
 * OFFSET and FETCH.</p>
 *
 * @since 0.9.0
 */
public final class MysqlPaginated implements Query {

    /**
     * The query.
     */
    private final Query origin;

    /**
     * Paginated SQL code.
     */
    private final Text code;

    /**
     * Ctor.
     *
     * @param query A select query that retrieves all elements
     * @param page The page number
     * @param size The amount of elements in this page
     */
    public MysqlPaginated(final Query query, final int page, final int size) {
        this.origin = query;
        this.code = new Sticky(
            () -> new Pretty(
                () -> SqlParser.create(
                    new Paginated(query, page, size).sql(),
                    SqlParser.config()
                        .withCaseSensitive(false)
                        .withQuoting(Quoting.BACK_TICK)
                ).parseQuery(),
                MysqlSqlDialect.DEFAULT
            ).asString()
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

    @Override
    public byte[] asBytes() throws Exception {
        return new QueryOf(this.sql()).asBytes();
    }
}
