/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import com.github.fabriciofx.cactoos.jdbc.plan.Keyed;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.NoSuchElementException;

/**
 * KeyedInsert. Insert statement which returns a key.
 *
 * @param <T> Type of the key
 * @since 0.9.0
 */
public final class KeyedInsert<T> implements Statement<T> {
    /**
     * Session.
     */
    private final Session session;

    /**
     * Query.
     */
    private final Query qry;

    /**
     * Ctor.
     *
     * @param session A {@link Session}
     * @param query A SQL {@link Query}
     */
    public KeyedInsert(final Session session, final Query query) {
        this.session = session;
        this.qry = query;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T execute() throws Exception {
        try (PreparedStatement stmt = this.session.prepared(new Keyed(this.qry))) {
            stmt.executeUpdate();
            try (ResultSet rset = stmt.getGeneratedKeys()) {
                if (rset.next()) {
                    return (T) rset.getObject(1);
                }
                throw new NoSuchElementException("generated key not found");
            }
        }
    }

    @Override
    public Query query() {
        return this.qry;
    }
}
