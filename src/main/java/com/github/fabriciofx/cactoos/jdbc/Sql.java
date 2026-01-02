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
     * Return the SQL before parsing.
     * @return The SQL code before parsing
     */
    String source();

    /**
     * Parse the SQL code and transform it.
     * @return A parsed SQL code
     * @throws Exception If fails
     */
    String parsed() throws Exception;
}
