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
     * Parse a named SQL positioning according to parameters.
     * @return A positioned SQL code
     * @throws Exception If fails
     */
    String parse() throws Exception;
}
