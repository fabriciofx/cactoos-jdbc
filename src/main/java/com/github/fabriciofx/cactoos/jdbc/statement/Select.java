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
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

/**
 * Select statement.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class Select implements Statement<ResultSet> {
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
     * @param connexio A Connection
     * @param query A SQL query
     */
    public Select(final Connexio connexio, final Query query) {
        this.connexio = connexio;
        this.qry = query;
    }

    @Override
    public ResultSet execute() throws Exception {
        try (PreparedStatement stmt = this.connexio.prepared(this.qry)) {
            try (ResultSet rset = stmt.executeQuery()) {
                final RowSetFactory rsf = RowSetProvider.newFactory();
                final CachedRowSet crs = rsf.createCachedRowSet();
                crs.populate(rset);
                return crs;
            }
        }
    }

    @Override
    public Query query() {
        return this.qry;
    }
}
