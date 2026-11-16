/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.plan;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Plan;
import com.github.fabriciofx.cactoos.jdbc.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Batched.
 *
 * @since 0.9.0
 */
public final class Batched implements Plan {

    /**
     * Query.
     */
    private final Query query;

    /**
     * Ctor.
     *
     * @param query A {@link Query}
     */
    public Batched(final Query query) {
        this.query = query;
    }

    @Override
    public PreparedStatement build(
        final Connection connection
    ) throws Exception {
        final PreparedStatement stmt = connection.prepareStatement(
            this.query.sql()
        );
        for (final Params params : this.query.params()) {
            params.initialize(stmt);
            stmt.addBatch();
        }
        return stmt;
    }

    @Override
    public Query query() {
        return this.query;
    }
}
