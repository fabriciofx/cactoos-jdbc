/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import org.cactoos.scalar.Ternary;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;

/**
 * Paginated. A decorator for {@link Query} that encapsulate a query to
 * paginated form.
 *
 * @since 0.8.0
 */
public final class Paginated implements Query {
    /**
     * The paginated query.
     */
    private final Query code;

    /**
     * Ctor.
     *
     * @param query A select query that retrieves all elements
     * @param page The page number
     * @param size The amount of elements in this page
     */
    public Paginated(final Query query, final int page, final int size) {
        this.code = new NamedQuery(
            new FormattedText(
                "SELECT q.*, COUNT(*) OVER () AS __total__ FROM (%s) q LIMIT :limit OFFSET :offset",
                new UncheckedText(query::sql)
            ),
            new ParamsOf(
                new Unchecked<>(
                    new Ternary<>(
                        query.params().iterator().hasNext(),
                        () -> query.params().iterator().next(),
                        ParamsOf::new
                    )
                ).value(),
                new IntParam("limit", size),
                new IntParam("offset", Math.max(page - 1, 0) * size)
            )
        );
    }

    @Override
    public Iterable<Params> params() {
        return this.code.params();
    }

    @Override
    public String sql() throws Exception {
        return this.code.sql();
    }
}
