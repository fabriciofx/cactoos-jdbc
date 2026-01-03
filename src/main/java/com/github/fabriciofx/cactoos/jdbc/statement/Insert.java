/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Statement;
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
    private final Connexio connexio;

    /**
     * The SQL query.
     */
    private final Query qry;

    /**
     * Ctor.
     * @param connexio A Connexio
     * @param query A SQL query
     */
    public Insert(final Connexio connexio, final Query query) {
        this.connexio = connexio;
        this.qry = query;
    }

    @Override
    public Boolean execute() throws Exception {
        try (PreparedStatement stmt = this.connexio.prepared(this.qry)) {
            return stmt.execute();
        }
    }

    @Override
    public Query query() {
        return this.qry;
    }
}
