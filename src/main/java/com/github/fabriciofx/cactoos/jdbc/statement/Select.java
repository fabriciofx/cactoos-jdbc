/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.plan.Simple;
import com.github.fabriciofx.cactoos.jdbc.rset.CachedResultSet;
import com.github.fabriciofx.cactoos.jdbc.table.LinkedTable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Select statement.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class Select implements Statement<ResultSet> {
    /**
     * Session.
     */
    private final Session session;

    /**
     * The SQL query.
     */
    private final Query qry;

    /**
     * Ctor.
     * @param session A {@link Session}
     * @param query A SQL {@link Query}
     */
    public Select(final Session session, final Query query) {
        this.session = session;
        this.qry = query;
    }

    @Override
    public ResultSet execute() throws Exception {
        try (
            PreparedStatement stmt = this.session.prepared(new Simple(this.qry))
        ) {
            try (ResultSet rset = stmt.executeQuery()) {
                final Table table = new LinkedTable(rset);
                return new CachedResultSet(table.rows(), table.columns());
            }
        }
    }
}
