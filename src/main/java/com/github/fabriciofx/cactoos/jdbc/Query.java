/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import org.cactoos.Bytes;

/**
 * Query.
 *
 * <p>There is no thread-safety guarantee.</p>
 *
 * @since 0.1
 */
public interface Query extends Bytes {

    /**
     * Return the Query params.
     *
     * @return The query parameters
     */
    Iterable<Params> params();

    /**
     * Return the SQL query.
     *
     * @return The SQL query
     * @throws Exception if something goes wrong
     */
    String sql() throws Exception;
}
