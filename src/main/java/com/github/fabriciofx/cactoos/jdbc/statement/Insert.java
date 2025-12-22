/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Insert statement.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class Insert implements Statement<Boolean> {
    /**
     * The connection.
     */
    private final Connection connexio;

    /**
     * The SQL query.
     */
    private final Query qry;

    /**
     * Ctor.
     * @param connection A Session
     * @param query A SQL query
     */
    public Insert(final Connection connection, final Query query) {
        this.connexio = connection;
        this.qry = query;
    }

    @Override
    public Boolean execute() throws Exception {
        try (PreparedStatement stmt = this.qry.prepared(this.connexio)) {
            return stmt.execute();
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
