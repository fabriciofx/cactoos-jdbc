/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import com.github.fabriciofx.cactoos.jdbc.result.ResultSetAsRows;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Insert with keys statement.
 *
 * @param <T> Type of the key
 * @since 0.1
 */
public final class InsertWithKey<T> implements Statement<T> {
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
     *
     * @param session A Session
     * @param query  A SQL query
     */
    public InsertWithKey(final Session session, final Query query) {
        this.sssn = session;
        this.qry = query;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T execute() throws Exception {
        try (
            PreparedStatement stmt = this.qry.prepared(this.sssn.connection())
        ) {
            stmt.executeUpdate();
            try (ResultSet rset = stmt.getGeneratedKeys()) {
                final Iterator<Map<String, Object>> iter = new ResultSetAsRows(
                    rset
                ).iterator();
                if (!iter.hasNext()) {
                    throw new NoSuchElementException();
                }
                return (T) iter.next().values().toArray()[0];
            }
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
