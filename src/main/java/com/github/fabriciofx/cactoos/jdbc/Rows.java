/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

/**
 * Rows.
 * @since 0.9.0
 */
public interface Rows {
    /**
     * Return the amount of rows.
     * @return The amount of rows
     */
    int count();

    /**
     * Add a {@link Rows} to rows.
     * @param row A row
     */
    void add(Row row);

    /**
     * Return a {@link Row} according a index.
     * @param index The index of row
     * @return A row
     * @throws Exception if something goes wrong
     */
    Row row(int index) throws Exception;
}
