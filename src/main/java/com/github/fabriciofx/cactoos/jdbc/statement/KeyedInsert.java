/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import com.github.fabriciofx.cactoos.jdbc.query.KeyedQuery;
import com.github.fabriciofx.cactoos.jdbc.result.ResultSetAsRows;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * KeyedInsert.
 * Insert statement which returns a key.
 *
 * @param <T> Type of the key
 * @since 0.9.0
 */
public final class KeyedInsert<T> implements Statement<T> {
    /**
     * The connection.
     */
    private final Connection connexio;

    /**
     * WithKey query.
     */
    private final KeyedQuery qry;

    /**
     * Ctor.
     *
     * @param connection A Session
     * @param query A {@link KeyedQuery} query
     */
    public KeyedInsert(final Connection connection, final KeyedQuery query) {
        this.connexio = connection;
        this.qry = query;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T execute() throws Exception {
        try (PreparedStatement stmt = this.qry.prepared(this.connexio)) {
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
    public Connection connection() {
        return this.connexio;
    }

    @Override
    public Query query() {
        return this.qry;
    }
}
