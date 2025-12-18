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
 * StatementInsert.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class Insert implements Statement<Boolean> {
    /**
     * The session.
     */
    private final Session session;

    /**
     * The SQL query.
     */
    private final Query query;

    /**
     * Ctor.
     * @param sssn A Session
     * @param qry A SQL query
     */
    public Insert(final Session sssn, final Query qry) {
        this.session = sssn;
        this.query = qry;
    }

    @Override
    public Boolean result() throws Exception {
        try (
            PreparedStatement stmt = this.query.prepared(this.session.connection())
        ) {
            return stmt.execute();
        }
    }
}
