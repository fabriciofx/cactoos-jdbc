/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.query.normalized.Shuttle;
import com.github.fabriciofx.cactoos.jdbc.text.Pretty;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.parser.SqlParser;
import org.cactoos.Text;
import org.cactoos.text.Sticky;

/**
 * Normalized.
 * <p>Normalize a {@link Query}, i.e. converts the query to canonical form.
 *
 * @since 0.9.0
 */
public final class Normalized implements Query {
    /**
     * The query.
     */
    private final Query origin;

    /**
     * Normalized SQL code.
     */
    private final Text code;

    /**
     * Ctor.
     *
     * @param query A {@link Query}
     */
    public Normalized(final Query query) {
        this.origin = query;
        this.code = new Sticky(
            () -> {
                final SqlParser.Config config = SqlParser.config()
                    .withCaseSensitive(false)
                    .withQuoting(Quoting.BACK_TICK);
                return new Pretty(
                    SqlParser
                        .create(query.sql(), config)
                        .parseQuery()
                        .accept(new Shuttle())
                ).asString();
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
