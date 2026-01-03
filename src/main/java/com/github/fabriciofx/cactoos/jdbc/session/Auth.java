/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.connexio.JdbcConnexio;
import com.github.fabriciofx.cactoos.jdbc.url.UrlFromSource;
import javax.sql.DataSource;
import org.cactoos.Text;

/**
 * Authenticated session.
 *
 * @since 0.1
 */
public final class Auth implements Session {
    /**
     * The DataSource.
     */
    private final DataSource source;

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
     * @param source DataSource
     * @param username User name
     * @param password User password
     */
    public Auth(
        final DataSource source,
        final String username,
        final String password
    ) {
        this.source = source;
        this.jdbc = new UrlFromSource(this.source);
        this.user = username;
        this.pass = password;
    }

    @Override
    public Connexio connexio() throws Exception {
        return new JdbcConnexio(
            this.source.getConnection(this.user, this.pass)
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
