/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

/**
 * Columns.
 * @since 0.9.0
 */
public interface Columns {
    /**
     * Return the amount of columns.
     * @return The amount of columns
     */
    int count();

    /**
     * Adds a type of the column.
     * @param name The name of the column
     * @param type The name of the column
     */
    void add(String name, Integer type);

    /**
     * Retrieve the type of the column.
     * @param name The name of the column
     * @return The type
     * @throws Exception if something goes wrong
     */
    Integer type(String name) throws Exception;

    /**
     * Returns the name of the column at the specified index.
     * @param index The index, that start at 1 to amount of columns
     * @return The column name
     * @throws Exception if something goes wrong
     */
    String name(int index) throws Exception;

    /**
     * Returns the index of a specified column.
     * @param name The column name
     * @return The index
     * @throws Exception if something goes wrong
     */
    int index(String name) throws Exception;
}
