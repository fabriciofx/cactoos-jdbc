/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import com.github.fabriciofx.cactoos.jdbc.result.ResultSetAsValue;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * StatementInsert with keys.
 *
 * @param <T> Type of the key
 * @since 0.1
 */
public final class InsertWithKey<T> implements Statement<T> {
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
     *
     * @param sssn A Session
     * @param qry  A SQL query
     */
    public InsertWithKey(final Session sssn, final Query qry) {
        this.session = sssn;
        this.query = qry;
    }

    @Override
    public T result() throws Exception {
        try (
            PreparedStatement stmt = this.query.prepared(this.session.connection())
        ) {
            stmt.executeUpdate();
            try (ResultSet rset = stmt.getGeneratedKeys()) {
                return new ResultSetAsValue<T>(() -> rset).value();
            }
        }
    }
}
