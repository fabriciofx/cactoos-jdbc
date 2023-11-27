/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2023 Fabrício Barros Cabral
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
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.connection.NoCloseConnection;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

/**
 * No Close Session.
 *
 * @since 0.8.1
 */
@SuppressWarnings(
    {
        "PMD.AvoidCatchingGenericException",
        "PMD.CloseResource"
    }
)
public final class NoClose implements Session, Closeable {
    /**
     * The Session.
     */
    private final Session origin;

    /**
     * List of NoCloseConnections.
     */
    private final List<NoCloseConnection> connections;

    /**
     * Ctor.
     *
     * @param session The Session to be decorated
     */
    public NoClose(final Session session) {
        this(session, new LinkedList<>());
    }

    /**
     * Ctor.
     *
     * @param session The Session to be decorated
     * @param cnncts The list of NoCloseConnection
     */
    public NoClose(
        final Session session,
        final List<NoCloseConnection> cnncts
    ) {
        this.origin = session;
        this.connections = cnncts;
    }

    @Override
    public Connection connection() throws Exception {
        final NoCloseConnection connection = new NoCloseConnection(
            this.origin.connection()
        );
        this.connections.add(connection);
        return connection;
    }

    @Override
    public void close() throws IOException {
        try {
            for (final NoCloseConnection connection : this.connections) {
                final Connection real = connection.value();
                if (!real.isClosed()) {
                    real.close();
                }
            }
            // @checkstyle IllegalCatchCheck (1 line)
        } catch (final Exception ex) {
            throw new IOException(ex);
        }
    }
}
