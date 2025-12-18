/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
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
    private final Query query;

    /**
     * Ctor.
     * @param qury The query that retrieves all elements
     * @param max The maximum amount of elements per page
     * @param skip Skip the first nth elements
     */
    public Paginated(final Query qury, final int max, final int skip) {
        this.query = new QueryOf(
            new FormattedText(
                "%s LIMIT :limit OFFSET :offset",
                qury.named()
            ),
            new ParamsOf(
                qury.params(),
                new IntOf("limit", max),
                new IntOf("offset", skip)
            )
        );
    }

    @Override
    public PreparedStatement prepared(final Connection connection)
        throws Exception {
        return this.query.prepared(connection);
    }

    @Override
    public Params params() {
        return this.query.params();
    }

    @Override
    public String named() {
        return this.query.named();
    }

    @Override
    public String asString() throws Exception {
        return this.query.asString();
    }
}
