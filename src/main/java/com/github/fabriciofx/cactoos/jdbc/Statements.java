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
package com.github.fabriciofx.cactoos.jdbc;

import com.github.fabriciofx.cactoos.jdbc.stream.SmartDataStreams;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.cactoos.list.ListOf;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class Statements {
    private final DataStreams streams;
    private final Session session;
    private final List<Statement> list;

    public Statements(final Session sssn, final Statement... stmts) {
        this(new SmartDataStreams(), sssn, stmts);
    }

    public Statements(
        final DataStreams strms,
        final Session sssn,
        final Statement... stmts
    ) {
        this.streams = strms;
        this.session = sssn;
        this.list = new ListOf<>(stmts);
    }

    public DataStreams streams() throws SQLException {
        try (final Connection connection = this.session.connection()) {
            for (final Statement stmt : this.list) {
                stmt.exec(this.streams, connection);
            }
        }
        return this.streams;
    }

    public DataStream stream(final int index) throws SQLException {
        return this.streams().get(index);
    }

    public Iterator<Statement> iterator() {
        return this.list.iterator();
    }
}
