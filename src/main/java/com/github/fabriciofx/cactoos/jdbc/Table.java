/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

/**
 * Table.
 * @since 0.9.0
 */
public interface Table {
    /**
     * Return the {@link Rows} of a table.
     * @return The rows
     * @throws Exception if something goes wrong
     */
    Rows rows() throws Exception;

    /**
     * Return the {@link Columns} of a table.
     * @return The columns
     * @throws Exception if something goes wrong
     */
    Columns columns() throws Exception;
}
