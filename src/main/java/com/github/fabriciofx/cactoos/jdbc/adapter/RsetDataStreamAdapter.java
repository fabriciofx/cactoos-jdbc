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
package com.github.fabriciofx.cactoos.jdbc.adapter;

import com.github.fabriciofx.cactoos.jdbc.DataStream;
import com.github.fabriciofx.cactoos.jdbc.stream.XmlDataStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import org.cactoos.Func;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class RsetDataStreamAdapter implements Func<ResultSet, DataStream> {
    private final DataStream stream;

    public RsetDataStreamAdapter(final String chld) {
        this("select", chld);
    }

    public RsetDataStreamAdapter(final String root, final String chld) {
        this(new XmlDataStream(root, chld));
    }

    public RsetDataStreamAdapter(final DataStream strm) {
        this.stream = strm;
    }

    @Override
    public DataStream apply(final ResultSet rset) throws Exception {
        while (rset.next()) {
            final ResultSetMetaData rsmd = rset.getMetaData();
            final int cols = rsmd.getColumnCount();
            for (int i = 1; i <= cols; i++) {
                final String name = rsmd.getColumnName(i).toLowerCase();
                final Object value = rset.getObject(i);
                this.stream.with(name, () -> value.toString());
            }
        }
        rset.close();
        return this.stream;
    }
}
