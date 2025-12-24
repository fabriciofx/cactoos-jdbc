/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import java.sql.Connection;

/**
 * Session.
 * Create a {@link java.sql.Connection}.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public interface Session {
    /**
     * Create a {@link java.sql.Connection}.
     * @return A {@link java.sql.Connection}
     * @throws Exception If fails
     */
    Connection connection() throws Exception;

    /**
     * Session JDBC URL.
     * @return The JDBC URL
     */
    String url() throws Exception;

    /**
     * Session username.
     * @return The username
     */
    String username();

    /**
     * Session password.
     * @return The password.
     */
    String password();
}
