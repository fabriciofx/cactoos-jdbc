/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

/**
 * Pages.
 *
 * <p>There is no thread-safety guarantee.</p>
 *
 * @param <T> Type of the page's content
 * @since 0.8.0
 */
@FunctionalInterface
public interface Pages<T> {

    /**
     * Get the pages of data.
     *
     * @param number The number of page
     * @param count The amount of items per page
     * @return A page with items of T
     * @throws Exception If fails
     */
    Page<T> page(int number, int count) throws Exception;
}
