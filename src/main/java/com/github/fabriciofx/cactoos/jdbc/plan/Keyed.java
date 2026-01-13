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
 * Keyed.
 * @since 0.9.0
 */
public final class Keyed implements Plan {
    /**
     * Query.
     */
    private final Query qry;

    /**
     * Ctor.
     * @param query A {@link Query}
     */
    public Keyed(final Query query) {
        this.qry = query;
    }

    @Override
    public PreparedStatement prepare(
        final Connection connection
    ) throws Exception {
        final PreparedStatement stmt = connection.prepareStatement(
            this.qry.sql(),
            java.sql.Statement.RETURN_GENERATED_KEYS
        );
        for (final Params params : this.qry.params()) {
            params.prepare(stmt);
        }
        return stmt;
    }

    @Override
    public Query query() {
        return this.qry;
    }
}
