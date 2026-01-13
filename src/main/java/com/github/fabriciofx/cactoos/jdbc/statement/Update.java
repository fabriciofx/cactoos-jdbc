/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import com.github.fabriciofx.cactoos.jdbc.plan.Simple;
import java.sql.PreparedStatement;

/**
 * Update statement.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class Update implements Statement<Integer> {
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
    public Update(final Session session, final Query query) {
        this.session = session;
        this.qry = query;
    }

    @Override
    public Integer execute() throws Exception {
        try (
            PreparedStatement stmt = this.session.prepared(new Simple(this.qry))
        ) {
            return stmt.executeUpdate();
        }
    }
}
