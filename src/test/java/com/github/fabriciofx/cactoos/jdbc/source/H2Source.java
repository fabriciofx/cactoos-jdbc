/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */

package com.github.fabriciofx.cactoos.jdbc.source;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;

/**
 * H2 result source, for unit testing.
 *
 * @since 0.1
 */
public final class H2Source implements DataSource {
    /**
     * JDBC URL.
     */
    private final Unchecked<String> url;

    /**
     * H2 driver.
     */
    private final Driver driver;

    /**
     * Public ctor.
     * @param dbname DB name
     */
    public H2Source(final String dbname) {
        this(new org.h2.Driver(), dbname);
    }

    /**
     * Public ctor.
     * @param drvr H2 Driver
     * @param dbname DB name
     */
    public H2Source(final Driver drvr, final String dbname) {
        this.driver = drvr;
        this.url = new Unchecked<>(
            new Sticky<>(
                () -> new FormattedText(
                    new Joined(
                        "",
                        "jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1;",
                        "INIT=CREATE SCHEMA IF NOT EXISTS %s\\;SET SCHEMA %s"
                    ),
                    dbname,
                    dbname,
                    dbname
                ).asString()
            )
        );
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.driver.connect(this.url.value(), new Properties());
    }

    @Override
    public Connection getConnection(
        final String username,
        final String password
    ) throws SQLException {
        final Properties props = new Properties();
        props.put("user", username);
        props.put("password", password);
        return this.driver.connect(this.url.value(), props);
    }

    @Override
    public PrintWriter getLogWriter() {
        throw new UnsupportedOperationException("#getLogWriter()");
    }

    @Override
    public void setLogWriter(final PrintWriter writer) {
        throw new UnsupportedOperationException("#setLogWriter()");
    }

    @Override
    public void setLoginTimeout(final int seconds) {
        throw new UnsupportedOperationException("#setLoginTimeout()");
    }

    @Override
    public int getLoginTimeout() {
        throw new UnsupportedOperationException("#getLoginTimeout()");
    }

    @Override
    public Logger getParentLogger() {
        throw new UnsupportedOperationException("#getParentLogger()");
    }

    @Override
    public <T> T unwrap(final Class<T> iface) {
        throw new UnsupportedOperationException("#unwrap()");
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) {
        throw new UnsupportedOperationException("#isWrapperFor()");
    }
}
