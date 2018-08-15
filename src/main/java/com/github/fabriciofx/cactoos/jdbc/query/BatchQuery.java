/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.QueryParams;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import org.cactoos.Text;
import org.cactoos.list.ListOf;

/**
 * Batch query.
 *
 * @since 0.1
 */
public final class BatchQuery implements Query {
    /**
     * SQL query.
     */
    private final Text sql;

    /**
     * A list of SQL query parameters.
     */
    private final List<QueryParams> params;

    /**
     * Ctor.
     * @param sql The SQL query
     * @param prms A list of SQL query parameters
     */
    public BatchQuery(final String sql, final QueryParams... prms) {
        this(() -> sql, prms);
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param prms A list of SQL query parameters
     */
    public BatchQuery(final Text sql, final QueryParams... prms) {
        this.sql = new ParsedSql(sql, prms[0]);
        this.params = new ListOf<>(prms);
    }

    @Override
    public PreparedStatement prepared(
        final Connection connection
    ) throws Exception {
        final PreparedStatement stmt = connection.prepareStatement(
            this.sql.asString()
        );
        for (final QueryParams params : this.params) {
            params.prepare(stmt);
            stmt.addBatch();
        }
        return stmt;
    }

    @Override
    public String asString() throws Exception {
        return this.sql.asString();
    }
}
