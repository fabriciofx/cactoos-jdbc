/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

/**
 * Query.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public interface Query {
    /**
     * Return the Query params.
     * @return The query parameters
     */
    Iterable<Params> params();

    /**
     * Return the SQL query.
     * @return The SQL query
     */
    Sql sql();
}
