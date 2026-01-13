/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.source;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.session.JdbcSession;
import com.github.fabriciofx.cactoos.jdbc.url.JdbcUrl;
import javax.sql.DataSource;
import org.cactoos.Text;

/**
 * No authenticated source.
 *
 * @since 0.1
 */
public final class NoAuth implements Source {
    /**
     * DataSource.
     */
    private final DataSource src;

    /**
     * JDBC URL.
     */
    private final Text jdbc;

    /**
     * Ctor.
     * @param src DataSource
     */
    public NoAuth(final DataSource src) {
        this.src = src;
        this.jdbc = new JdbcUrl(this.src);
    }

    @Override
    public Session session() throws Exception {
        return new JdbcSession(this.src.getConnection());
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
