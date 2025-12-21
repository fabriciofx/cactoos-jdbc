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
 * StatementUpdate.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class Update implements Statement<Integer> {
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
    public Update(final Session sssn, final Query qry) {
        this.session = sssn;
        this.query = qry;
    }

    @Override
    public Integer execute() throws Exception {
        try (
            PreparedStatement stmt = this.query.prepared(this.session.connection())
        ) {
            return stmt.executeUpdate();
        }
    }
}
