/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import java.util.List;

/**
 * Page.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @param <T> Type of the page's content
 * @since 0.9.0
 */
public interface Page<T> {
    /**
     * Get the items of this page.
     *
     * @return An item's list
     */
    List<T> items();

    /**
     * Get the total amount of items.
     * @return The total number of items
     */
    long total();

    /**
     * Get the current page number.
     * @return The current page number
     */
    int number();

    /**
     * Get the amount items in this page.
     * @return The number of items in this page
     */
    int size();
}
