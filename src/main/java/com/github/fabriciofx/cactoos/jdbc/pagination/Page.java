/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.pagination;

import java.util.List;

/**
 * Page.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @param <T> Type of the page's content
 * @since 0.8.0
 */
public interface Page<T> {
    /**
     * Get the content of this page.
     * @return A content's list
     */
    List<T> content();

    /**
     * Check if there is a next page.
     * @return True if contains or false if it doesn't
     */
    boolean hasNext();

    /**
     * Get the next page.
     * @return The next page
     */
    Page<T> next();

    /**
     * Check if there is a previous page.
     * @return True if contains or false if it doesn't
     */
    boolean hasPrevious();

    /**
     * Get the previous page.
     * @return The previous page
     */
    Page<T> previous();
}
