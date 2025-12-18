/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.pagination;

/**
 * Pages.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @param <T> Type of the pages' content
 * @since 0.8.0
 */
public interface Pages<T> {
    /**
     * Get the number of pages.
     * @return The number of pages
     */
    int count();

    /**
     * Get a page by the number.
     * @param number The page number
     * @return A page
     */
    Page<T> page(int number);
}
