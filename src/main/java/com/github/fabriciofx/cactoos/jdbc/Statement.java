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
 * @since 0.1
 */
@FunctionalInterface
public interface Statement<T> {
    /**
     * Execute it and returns a single or set of data(s).
     * @return A Result of a type
     * @throws Exception If fails
     */
    T result() throws Exception;
}
