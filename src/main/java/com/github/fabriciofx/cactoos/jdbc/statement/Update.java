/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import java.sql.PreparedStatement;

/**
 * Update statement.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class Update implements Statement<Integer> {
    /**
     * The session.
     */
    private final Session sssn;

    /**
     * The SQL query.
     */
    private final Query qry;

    /**
     * Ctor.
     * @param session A Session
     * @param query A SQL query
     */
    public Update(final Session session, final Query query) {
        this.sssn = session;
        this.qry = query;
    }

    @Override
    public Integer execute() throws Exception {
        try (
            PreparedStatement stmt = this.qry.prepared(this.sssn.connection())
        ) {
            return stmt.executeUpdate();
        }
    }

    @Override
    public Session session() {
        return this.sssn;
    }

    @Override
    public Query query() {
        return this.qry;
    }
}
