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
 * StatementSelect.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class Select implements Statement<ResultSet> {
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
    public Select(final Session sssn, final Query qry) {
        this.session = sssn;
        this.query = qry;
    }

    @Override
    public ResultSet execute() throws Exception {
        try (
            PreparedStatement stmt = this.query.prepared(this.session.connection())
        ) {
            try (ResultSet rset = stmt.executeQuery()) {
                final RowSetFactory rsf = RowSetProvider.newFactory();
                final CachedRowSet crs = rsf.createCachedRowSet();
                crs.populate(rset);
                return crs;
            }
        }
    }

    public Query query() {
        return this.query;
    }

    public Session session() {
        return this.session;
    }
}
