/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.source;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.cactoos.scalar.Solid;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;

/**
 * MySQL result source, for unit testing.
 *
 * @since 0.2
 */
public final class MysqlSource implements DataSource {
    /**
     * Origin DataSource.
     */
    private final Unchecked<MysqlDataSource> origin;

    /**
     * Ctor.
     * @param dbname Database name
     */
    public MysqlSource(final String dbname) {
        this("localhost", dbname);
    }

    /**
     * Ctor.
     * @param hostname Server hostname or IPv4 Address
     * @param dbname Database name
     */
    public MysqlSource(final String hostname, final String dbname) {
        // @checkstyle MagicNumber (1 line)
        this(hostname, 3306, dbname);
    }

    /**
     * Ctor.
     * @param hostname Server hostname or IPv4 Address
     * @param port Server port
     * @param dbname Database name
     */
    public MysqlSource(
        final String hostname,
        final int port,
        final String dbname
    ) {
        this.origin = new Unchecked<>(
            new Solid<>(
                () -> {
                    final MysqlDataSource mds = new MysqlDataSource();
                    mds.setUrl(
                        new FormattedText(
                            new Joined(
                                "",
                                "jdbc:mysql://%s:%d/%s?useSSL=false",
                                "&useTimezone=true&serverTimezone=UTC"
                            ),
                            hostname,
                            port,
                            dbname
                        ).asString()
                    );
                    return mds;
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
