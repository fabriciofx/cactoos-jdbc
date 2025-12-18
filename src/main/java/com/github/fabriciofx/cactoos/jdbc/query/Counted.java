/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

/**
 * Check if this query is a count query.
 *
 * @since 0.8.0
 */
public final class Counted implements Query {
    /**
     * The query to be checked.
     */
    private final Unchecked<Query> origin;

    /**
     * Ctor.
     * @param query The SQL query
     */
    public Counted(final Query query) {
        this.origin = new Unchecked<>(
            new Sticky<>(
                () -> {
                    if (
                        query.asString().matches(
                            "(?i)SELECT\\s+COUNT\\(.*\\).*"
                        )
                    ) {
                        return query;
                    } else {
                        throw new IllegalArgumentException(
                            "This is not a count query"
                        );
                    }
                }
            )
        );
    }

    @Override
    public PreparedStatement prepared(final Connection connection)
        throws Exception {
        return this.origin.value().prepared(connection);
    }

    @Override
    public Params params() {
        return this.origin.value().params();
    }

    @Override
    public String named() {
        return this.origin.value().named();
    }

    @Override
    public String asString() throws Exception {
        return this.origin.value().asString();
    }
}
