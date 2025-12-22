/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import java.sql.Connection;

/**
 * Statement.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @param <T> Type of the result
 * @since 0.1
 */
public interface Statement<T> {
    /**
     * Execute it and returns a single or set of data.
     * @return The result of statement execution
     * @throws Exception If fails
     */
    T execute() throws Exception;

    /**
     * Get the {@link java.sql.Connection} where the statement was executed.
     * @return The {@link java.sql.Connection}
     */
    Connection connection();

    /**
     * Get the {@link Query} which statement executes.
     * @return The {@link Query}
     */
    Query query();
}
