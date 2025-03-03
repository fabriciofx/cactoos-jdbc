/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2025 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import java.sql.Connection;
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
    public ResultSet result() throws Exception {
        // @checkstyle NestedTryDepthCheck (10 lines)
        try (Connection conn = this.session.connection()) {
            try (PreparedStatement stmt = this.query.prepared(conn)) {
                try (ResultSet rset = stmt.executeQuery()) {
                    final RowSetFactory rsf = RowSetProvider.newFactory();
                    final CachedRowSet crs = rsf.createCachedRowSet();
                    crs.populate(rset);
                    return crs;
                }
            }
        }
    }
}
