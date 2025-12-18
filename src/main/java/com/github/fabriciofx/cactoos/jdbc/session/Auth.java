/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import java.sql.Connection;
import javax.sql.DataSource;

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
     * User name.
     */
    private final String user;

    /**
     * User password.
     */
    private final String password;

    /**
     * Ctor.
     * @param source DataSource
     * @param user User name
     * @param password User password
     */
    public Auth(
        final DataSource source,
        final String user,
        final String password
    ) {
        this.source = source;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection connection() throws Exception {
        return this.source.getConnection(this.user, this.password);
    }
}
