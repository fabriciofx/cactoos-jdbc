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
package com.github.fabriciofx.cactoos.jdbc.result;

import com.github.fabriciofx.cactoos.jdbc.DataStream;
import com.github.fabriciofx.cactoos.jdbc.Result;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import com.github.fabriciofx.cactoos.jdbc.stream.XmlDataStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class RsetAsDataStream implements Result<DataStream> {
    private final Statement<ResultSet> statement;
    private final DataStream stream;

    public RsetAsDataStream(final Statement<ResultSet> stmt) {
        this(stmt, new XmlDataStream("select"));
    }

    public RsetAsDataStream(final Statement<ResultSet> stmt, final DataStream strm) {
        this.statement = stmt;
        this.stream = strm;
    }

    @Override
    public DataStream result() throws SQLException {
        try (final ResultSet rset = this.statement.result(null)) {
            while (rset.next()) {
                final ResultSetMetaData rsmd = rset.getMetaData();
                final int cols = rsmd.getColumnCount();
                for (int i = 1; i <= cols; i++) {
                    final String name = rsmd.getColumnName(i).toLowerCase();
                    final Object value = rset.getObject(i);
                    this.stream.with(name, () -> value.toString());
                }
            }
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
        return this.stream;
    }
}
