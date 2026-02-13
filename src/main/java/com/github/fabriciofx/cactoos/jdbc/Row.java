/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import org.cactoos.Bytes;

/**
 * Row.
 * @since 0.9.0
 */
public interface Row extends Bytes {
    /**
     * Adds a value to a column.
     * @param column The column name
     * @param value The value
     */
    void add(String column, Object value);

    /**
     * Retrieve a value of a column.
     * @param column The column name
     * @param type The type of the value
     * @return The value
     * @param <T> The type of value
     * @throws Exception if something goes wrong
     */
    <T> T value(String column, Class<T> type) throws Exception;
}
