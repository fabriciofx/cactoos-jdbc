/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

/**
 * SQL.
 *
 * Represent a SQL code.
 *
 * @since 0.9.0
 */
public interface Sql {
    /**
     * Return a named SQL.
     * @return The named SQL code
     */
    String named();

    /**
     * Returns a parsed named SQL according to position of the parameters.
     * @return A positioned SQL code
     * @throws Exception If fails
     */
    String parsed() throws Exception;
}
