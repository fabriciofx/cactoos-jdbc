/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

/**
 * Statement.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @param <T> Type of the result
 * @since 0.9.0
 */
public interface Statement<T> {
    /**
     * Execute it and returns a single or set of data.
     * @return The result of statement execution
     * @throws Exception If fails
     */
    T execute() throws Exception;
}
