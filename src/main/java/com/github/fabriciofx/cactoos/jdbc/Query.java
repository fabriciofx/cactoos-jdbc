/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.cactoos.Text;

/**
 * Query.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public interface Query extends Text {
    /**
     * Create a PreparedStatement.
     * @param connection A connection to the DataSource
     * @return A PreparedStatement
     * @throws Exception If fails
     */
    PreparedStatement prepared(Connection connection) throws Exception;

    /**
     * Return the Query params.
     * @return The query parameters
     */
    Params params();

    /**
     * Return the SQL query.
     * @return The SQL query
     */
    Sql sql();
}
