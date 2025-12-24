/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Driver session.
 *
 * @since 0.1
 */
public final class Driver implements Session {
    /**
     * JDBC URL.
     */
    private final String jdbc;

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
     * @param url JDBC URL
     * @param username User name
     * @param password User password
     */
    public Driver(
        final String url,
        final String username,
        final String password
    ) {
        this.jdbc = url;
        this.user = username;
        this.pass = password;
    }

    @Override
    public Connection connection() throws Exception {
        return DriverManager.getConnection(
            this.jdbc,
            this.user,
            this.pass
        );
    }

    @Override
    public String url() throws Exception {
        return this.jdbc;
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
