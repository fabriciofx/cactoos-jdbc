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
    private final String url;

    /**
     * User name.
     */
    private final String username;

    /**
     * User password.
     */
    private final String password;

    /**
     * Ctor.
     * @param url JDBC URL
     * @param user User name
     * @param password User password
     */
    public Driver(
        final String url,
        final String user,
        final String password
    ) {
        this.url = url;
        this.username = user;
        this.password = password;
    }

    @Override
    public Connection connection() throws Exception {
        return DriverManager.getConnection(
            this.url,
            this.username,
            this.password
        );
    }
}
