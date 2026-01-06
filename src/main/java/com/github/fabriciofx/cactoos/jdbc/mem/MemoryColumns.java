/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.mem;

import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.Map;
import org.cactoos.Scalar;
import org.cactoos.scalar.Sticky;

/**
 * MemoryColumns.
 *
 * Converts a {@link ResultSetMetaData} in columns, with names and types.
 * @since 0.9.0
 */
public final class MemoryColumns implements Scalar<Map<String, Integer>> {
    /**
     * Columns.
     */
    private final Scalar<Map<String, Integer>> scalar;

    /**
     * Ctor.
     * @param meta A {@link ResultSetMetaData}
     */
    public MemoryColumns(final ResultSetMetaData meta) {
        this.scalar = new Sticky<>(
            () -> {
                final Map<String, Integer> columns = new LinkedHashMap<>();
                for (int col = 1; col <= meta.getColumnCount(); ++col) {
                    columns.put(
                        meta.getColumnName(col),
                        meta.getColumnType(col)
                    );
                }
                return columns;
            }
        );
    }

    @Override
    public Map<String, Integer> value() throws Exception {
        return this.scalar.value();
    }
}
