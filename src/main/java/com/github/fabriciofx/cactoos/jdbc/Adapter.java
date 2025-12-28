/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import java.sql.ResultSet;

/**
 * Adapt a ResultSet in an object.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @param <T> The adapted object.
 * @since 0.8.0
 */
@FunctionalInterface
public interface Adapter<T> {
    /**
     * Transform the ResultSet in an object.
     * @param rset The ResultSet
     * @return The adapted object
     * @throws Exception If fails
     */
    T adapt(ResultSet rset) throws Exception;
}
