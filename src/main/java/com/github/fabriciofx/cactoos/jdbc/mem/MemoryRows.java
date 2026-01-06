/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.mem;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.cactoos.Scalar;
import org.cactoos.scalar.Sticky;

/**
 * MemoryRows.
 *
 * Converts a {@link ResultSetMetaData} in columns, with names and types.
 * @since 0.9.0
 */
public final class MemoryRows implements Scalar<List<Map<String, Object>>> {
    /**
     * Rows.
     */
    private final Scalar<List<Map<String, Object>>> scalar;

    /**
     * Ctor.
     * @param rset A {@link ResultSet}
     */
    public MemoryRows(final ResultSet rset) {
        this.scalar = new Sticky<>(
            () -> {
                final List<Map<String, Object>> rows = new LinkedList<>();
                final ResultSetMetaData meta = rset.getMetaData();
                while (rset.next()) {
                    final Map<String, Object> row = new LinkedHashMap<>();
                    for (int col = 1; col <= meta.getColumnCount(); ++col) {
                        row.put(meta.getColumnLabel(col), rset.getObject(col));
                    }
                    rows.add(row);
                }
                return rows;
            }
        );
    }

    @Override
    public List<Map<String, Object>> value() throws Exception {
        return this.scalar.value();
    }
}
