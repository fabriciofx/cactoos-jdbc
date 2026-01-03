/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

/**
 * Session.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public interface Session {
    /**
     * Create a {@link Connexio}.
     * @return A {@link Connexio}
     * @throws Exception If fails
     */
    Connexio connexio() throws Exception;

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
