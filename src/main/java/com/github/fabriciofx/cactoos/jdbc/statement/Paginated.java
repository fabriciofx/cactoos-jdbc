/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

/**
 * Paginated, a decorator for {@link Select} which paginate the result.
 *
 * @since 0.9.0
 */
public final class Paginated implements Statement<ResultSet> {
    /**
     * The Select statement.
     */
    private final Select origin;

    /**
     * The query which contains paginated SQL code.
     */
    private final Query qry;

    /**
     * Ctor.
     * @param select The {@link Select} statement
     * @param page The page number
     * @param size The page size
     */
    public Paginated(final Select select, final int page, final int size) {
        this.origin = select;
        this.qry = new com.github.fabriciofx.cactoos.jdbc.query.Paginated(
            this.origin.query(),
            page,
            size
        );
    }

    @Override
    public ResultSet execute() throws Exception {
        try (
            PreparedStatement stmt = this.qry.prepared(this.origin.connection())
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
    public Connection connection() {
        return this.origin.connection();
    }

    @Override
    public Query query() {
        return this.qry;
    }
}
