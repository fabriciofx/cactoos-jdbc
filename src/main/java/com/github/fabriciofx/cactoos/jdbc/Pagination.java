/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import com.github.fabriciofx.cactoos.jdbc.pagination.Pages;

/**
 * Pagination.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @param <T> Type of the page's content
 * @since 0.8.0
 */
@FunctionalInterface
public interface Pagination<T> {
    /**
     * Get the pages of data.
     * @param limit The number of data by page
     * @return The pages of data
     */
    Pages<T> pages(int limit);
}
