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
package com.github.fabriciofx.cactoos.jdbc.source;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.cactoos.scalar.Solid;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;
import org.postgresql.ds.PGSimpleDataSource;

/**
 * MySQL result source, for unit testing.
 *
 * @since 0.2
 */
public final class PgsqlSource implements DataSource {
    /**
     * Origin DataSource.
     */
    private final Unchecked<PGSimpleDataSource> origin;

    /**
     * Ctor.
     * @param dbname Database name
     */
    public PgsqlSource(final String dbname) {
        this("localhost", dbname);
    }

    /**
     * Ctor.
     * @param hostname Server hostname or IPv4 Address
     * @param dbname Database name
     */
    public PgsqlSource(final String hostname, final String dbname) {
        // @checkstyle MagicNumber (1 line)
        this(hostname, 5432, dbname);
    }

    /**
     * Ctor.
     * @param hostname Server hostname or IPv4 Address
     * @param port Server port
     * @param dbname Database name
     */
    public PgsqlSource(
        final String hostname,
        final int port,
        final String dbname
    ) {
        this.origin = new Unchecked<>(
            new Solid<>(
                () -> {
                    final PGSimpleDataSource pds = new PGSimpleDataSource();
                    pds.setUrl(
                        new FormattedText(
                            "jdbc:postgresql://%s:%d/%s",
                            hostname,
                            port,
                            dbname
                        ).asString()
                    );
                    return pds;
                }
            )
        );
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.origin.value().getConnection();
    }

    @Override
    public Connection getConnection(
        final String username,
        final String password
    ) throws SQLException {
        return this.origin.value().getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() {
        return this.origin.value().getLogWriter();
    }

    @Override
    public void setLogWriter(final PrintWriter writer) throws SQLException {
        this.origin.value().setLogWriter(writer);
    }

    @Override
    public void setLoginTimeout(final int seconds) throws SQLException {
        this.origin.value().setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() {
        return this.origin.value().getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.origin.value().getParentLogger();
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        return this.origin.value().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return this.origin.value().isWrapperFor(iface);
    }
}
