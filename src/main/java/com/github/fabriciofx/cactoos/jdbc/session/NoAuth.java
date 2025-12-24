/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import java.sql.Connection;
import javax.sql.DataSource;
import org.cactoos.Text;

/**
 * No authenticated session.
 *
 * @since 0.1
 */
public final class NoAuth implements Session {
    /**
     * DataSource.
     */
    private final DataSource source;

    /**
     * JDBC URL.
     */
    private final Text jdbc;

    /**
     * Ctor.
     * @param source DataSource
     */
    public NoAuth(final DataSource source) {
        this.source = source;
        this.jdbc = new UrlFromSource(this.source);
    }

    @Override
    public Connection connection() throws Exception {
        return this.source.getConnection();
    }

    @Override
    public String url() throws Exception {
        return this.jdbc.asString();
    }

    @Override
    public String username() {
        return "";
    }

    @Override
    public String password() {
        return "";
    }
}
