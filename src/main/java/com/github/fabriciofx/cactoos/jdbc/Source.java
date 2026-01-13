/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

/**
 * Source.
 * @since 0.9.0
 */
public interface Source {
    /**
     * Create a {@link Session}.
     * @return A {@link Session}
     * @throws Exception If fails
     */
    Session session() throws Exception;

    /**
     * Source JDBC URL.
     * @return The JDBC URL
     */
    String url() throws Exception;

    /**
     * Source username.
     * @return The username
     */
    String username();

    /**
     * Source password.
     * @return The password.
     */
    String password();
}
