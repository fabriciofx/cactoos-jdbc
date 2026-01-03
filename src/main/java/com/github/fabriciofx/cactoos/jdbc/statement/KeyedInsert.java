/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Statement;
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
     * The connection.
     */
    private final Connexio connexio;

    /**
     * Query.
     */
    private final Query qry;

    /**
     * Ctor.
     *
     * @param connexio A Connection
     * @param query A {@link Query} query
     */
    public KeyedInsert(final Connexio connexio, final Query query) {
        this.connexio = connexio;
        this.qry = query;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T execute() throws Exception {
        try (PreparedStatement stmt = this.connexio.keyed(this.qry)) {
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
