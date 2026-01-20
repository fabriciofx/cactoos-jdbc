/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import org.cactoos.Text;
import org.cactoos.text.Replaced;
import org.cactoos.text.Sticky;
import org.cactoos.text.TextOf;
import org.cactoos.text.Trimmed;

/**
 * Pretty.
 * @since 0.9.0
 */
public final class Pretty implements Query {
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
     * @param query A query
     */
    public Pretty(final Query query) {
        this.origin = query;
        this.code = new Sticky(
            () -> new Trimmed(
                new Replaced(
                    new TextOf(query.sql()),
                    "\\s+",
                    " "
                )
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
}
