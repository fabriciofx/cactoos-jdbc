/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.query.paginated.GroupedOrder;
import com.github.fabriciofx.cactoos.jdbc.query.paginated.GroupedSelect;
import com.github.fabriciofx.cactoos.jdbc.query.paginated.UngroupedSelect;
import com.github.fabriciofx.cactoos.jdbc.text.Pretty;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOrderBy;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParser;
import org.cactoos.Text;
import org.cactoos.text.Sticky;
import org.cactoos.text.TextOf;

/**
 * Paginated.
 * <p>
 * A decorator for {@link Query} that encapsulate a query to
 * paginated form.
 * @since 0.8.0
 */
public final class Paginated implements Query {
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
    public Paginated(final Query query, final int page, final int size) {
        this.origin = query;
        this.code = new Sticky(
            () -> {
                final SqlParser.Config config = SqlParser.config()
                    .withCaseSensitive(false)
                    .withQuoting(Quoting.BACK_TICK);
                final SqlNode stmt = SqlParser
                    .create(query.sql(), config)
                    .parseQuery();
                final SqlSelect select;
                if (stmt instanceof SqlOrderBy rdr) {
                    select = (SqlSelect) rdr.query;
                } else if (stmt instanceof SqlSelect) {
                    select = (SqlSelect) stmt;
                } else {
                    throw new IllegalArgumentException(
                        "The query MUST be a SELECT"
                    );
                }
                final Text paginated;
                if (select.getFetch() != null || select.getOffset() != null) {
                    paginated = new TextOf(query.sql());
                } else if (select.getGroup() != null && !select.getGroup()
                    .isEmpty()) {
                    if (stmt instanceof SqlOrderBy order) {
                        paginated = new Pretty(
                            new GroupedOrder(order, page, size)
                        );
                    } else {
                        paginated = new Pretty(
                            new GroupedSelect(select, page, size)
                        );
                    }
                } else {
                    paginated = new Pretty(
                        new UngroupedSelect(select, page, size)
                    );
                }
                return paginated.asString();
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
