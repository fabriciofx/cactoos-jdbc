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
 * Simple.
 * @since 0.9.0
 */
public final class Simple implements Plan {
    /**
     * Query.
     */
    private final Query qry;

    /**
     * Ctor.
     * @param query A {@link Query}
     */
    public Simple(final Query query) {
        this.qry = query;
    }

    @Override
    public PreparedStatement prepare(
        final Connection connection
    ) throws Exception {
        final PreparedStatement stmt = connection.prepareStatement(
            this.qry.sql()
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
