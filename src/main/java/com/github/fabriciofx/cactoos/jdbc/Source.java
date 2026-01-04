/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

/**
 * Source.
 * @since 0.9.0
 */
public interface Source {
    /**
     * Create a {@link Connexio}.
     * @return A {@link Connexio}
     * @throws Exception If fails
     */
    Connexio connexio() throws Exception;

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
