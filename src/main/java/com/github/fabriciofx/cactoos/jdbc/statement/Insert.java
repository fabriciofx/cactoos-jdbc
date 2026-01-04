/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import com.github.fabriciofx.cactoos.jdbc.plan.Simple;
import java.sql.PreparedStatement;

/**
 * Insert statement.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class Insert implements Statement<Boolean> {
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
    public Insert(final Session session, final Query query) {
        this.session = session;
        this.qry = query;
    }

    @Override
    public Boolean execute() throws Exception {
        try (PreparedStatement stmt = this.session.prepared(new Simple(this.qry))) {
            return stmt.execute();
        }
    }

    @Override
    public Query query() {
        return this.qry;
    }
}
