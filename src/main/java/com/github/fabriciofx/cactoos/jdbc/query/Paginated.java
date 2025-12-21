/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Sql;
import com.github.fabriciofx.cactoos.jdbc.param.IntOf;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    private final Query origin;

    /**
     * Ctor.
     * @param query The query that retrieves all elements
     * @param max The maximum amount of elements per page
     * @param skip Skip the first nth elements
     */
    public Paginated(final Query query, final int max, final int skip) {
        this.origin = new QueryOf(
            new FormattedText(
                "%s LIMIT :limit OFFSET :offset",
                query.sql().named()
            ),
            new ParamsOf(
                query.params(),
                new IntOf("limit", max),
                new IntOf("offset", skip)
            )
        );
    }

    @Override
    public PreparedStatement prepared(final Connection connection)
        throws Exception {
        return this.origin.prepared(connection);
    }

    @Override
    public Params params() {
        return this.origin.params();
    }

    @Override
    public Sql sql() {
        return this.origin.sql();
    }

    @Override
    public String asString() throws Exception {
        return this.origin.asString();
    }
}
