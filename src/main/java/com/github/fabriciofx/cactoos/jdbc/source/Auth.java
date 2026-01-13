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
 * Authenticated Source.
 *
 * @since 0.1
 */
public final class Auth implements Source {
    /**
     * DataSource.
     */
    private final DataSource src;

    /**
     * JDBC URL.
     */
    private final Text jdbc;

    /**
     * User name.
     */
    private final String user;

    /**
     * User password.
     */
    private final String pass;

    /**
     * Ctor.
     * @param src DataSource
     * @param username User name
     * @param password User password
     */
    public Auth(
        final DataSource src,
        final String username,
        final String password
    ) {
        this.src = src;
        this.jdbc = new JdbcUrl(this.src);
        this.user = username;
        this.pass = password;
    }

    @Override
    public Session session() throws Exception {
        return new JdbcSession(
            this.src.getConnection(this.user, this.pass)
        );
    }

    @Override
    public String url() throws Exception {
        return this.jdbc.asString();
    }

    @Override
    public String username() {
        return this.user;
    }

    @Override
    public String password() {
        return this.pass;
    }
}
