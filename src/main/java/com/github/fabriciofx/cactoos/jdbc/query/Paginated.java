/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Sql;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import org.cactoos.text.FormattedText;

/**
 * Paginated query.
 *
 * @since 0.8.0
 */
public final class Paginated implements Query {
    /**
     * The paginated query.
     */
    private final Query qry;

    /**
     * Ctor.
     * @param query A select query that retrieves all elements
     * @param page The page number
     * @param size The amount of elements in this page
     */
    public Paginated(final Query query, final int page, final int size) {
        this.qry = new QueryOf(
            new FormattedText(
                "SELECT q.*, COUNT(*) OVER () AS __total__ FROM (%s) q LIMIT :limit OFFSET :offset",
                query.sql().source()
            ),
            new ParamsOf(
                query.params().iterator().next(),
                new IntParam("limit", size),
                new IntParam("offset", Math.max(page - 1, 0) * size)
            )
        );
    }

    @Override
    public Iterable<Params> params() {
        return this.qry.params();
    }

    @Override
    public Sql sql() {
        return this.qry.sql();
    }
}
