/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
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
     * The session.
     */
    private final Session sssn;

    /**
     * The SQL query.
     */
    private final Query qry;

    /**
     * Ctor.
     * @param session A Session
     * @param query A SQL query
     */
    public Select(final Session session, final Query query) {
        this.sssn = session;
        this.qry = query;
    }

    @Override
    public ResultSet execute() throws Exception {
        try (
            PreparedStatement stmt = this.qry.prepared(this.sssn.connection())
        ) {
            try (ResultSet rset = stmt.executeQuery()) {
                final RowSetFactory rsf = RowSetProvider.newFactory();
                final CachedRowSet crs = rsf.createCachedRowSet();
                crs.populate(rset);
                return crs;
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
