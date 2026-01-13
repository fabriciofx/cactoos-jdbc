/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import com.github.fabriciofx.cactoos.jdbc.plan.Batched;
import java.sql.PreparedStatement;

/**
 * Batch statement.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class Batch implements Statement<int[]> {
    /**
     * Session.
     */
    private final Session session;

    /**
     * The SQL query.
     */
    private final Query qry;

    /**
     * Ctor.
     * @param session A {@link Session}
     * @param query A SQL {@link Query}
     */
    public Batch(final Session session, final Query query) {
        this.session = session;
        this.qry = query;
    }

    @Override
    public int[] execute() throws Exception {
        try (
            PreparedStatement stmt = this.session.prepared(
                new Batched(this.qry)
            )
        ) {
            return stmt.executeBatch();
        }
    }
}
