/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.plan;

import com.github.fabriciofx.cactoos.jdbc.Plan;
import com.github.fabriciofx.cactoos.jdbc.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Normal.
 * @since 0.9.0
 */
public final class Normal implements Plan {
    /**
     * Query.
     */
    private final Query qry;

    /**
     * Ctor.
     * @param query A {@link Query}
     */
    public Normal(final Query query) {
        this.qry = query;
    }

    @Override
    public PreparedStatement prepare(final Connection connection)
        throws Exception {
        return new Simple(this.qry).prepare(connection);
    }

    @Override
    public Query query() {
        return this.qry;
    }
}
