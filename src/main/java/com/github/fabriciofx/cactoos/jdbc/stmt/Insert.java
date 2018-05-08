/**
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.stmt;

import com.github.fabriciofx.cactoos.jdbc.DataParam;
import com.github.fabriciofx.cactoos.jdbc.Result;
import com.github.fabriciofx.cactoos.jdbc.DataStreams;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import com.github.fabriciofx.cactoos.jdbc.param.DataParams;
import com.github.fabriciofx.cactoos.jdbc.result.KeysResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class Insert implements Statement {
    private final Result result;
    private final String query;
    private final DataParams params;

    public Insert(
        final String sql,
        final DataParam... prms
    ) {
        this(new KeysResult(), sql, prms);
    }

    public Insert(
        final Result rslt,
        final String sql,
        final DataParam... prms
    ) {
        this.result = rslt;
        this.query = sql;
        this.params = new DataParams(prms);
    }

    @Override
    public void exec(final DataStreams streams, final Connection connection)
        throws SQLException {
        try (
            final PreparedStatement stmt = connection.prepareStatement(
                this.query,
                java.sql.Statement.RETURN_GENERATED_KEYS
            )
        ) {
            this.params.prepare(1, stmt);
            this.result.process(streams, stmt);
        }
    }
}
