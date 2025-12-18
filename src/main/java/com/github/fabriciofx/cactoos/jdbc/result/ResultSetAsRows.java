/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.result;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.cactoos.Scalar;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

/**
 * Rows as ResultSet.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class ResultSetAsRows implements Iterable<Map<String, Object>> {
    /**
     * Rows.
     */
    private final Scalar<List<Map<String, Object>>> rows;

    /**
     * Ctor.
     * @param rst A ResultSet
     */
    public ResultSetAsRows(final ResultSet rst) {
        this.rows = new Sticky<>(
            () -> {
                final List<Map<String, Object>> rws = new LinkedList<>();
                final ResultSetMetaData rsmd = rst.getMetaData();
                final int cols = rsmd.getColumnCount();
                while (rst.next()) {
                    final Map<String, Object> fields = new LinkedHashMap<>();
                    for (int idx = 1; idx <= cols; ++idx) {
                        final String name = rsmd.getColumnName(idx)
                            .toLowerCase(Locale.ENGLISH);
                        final Object data = rst.getObject(idx);
                        fields.put(name, data);
                    }
                    rws.add(fields);
                }
                return rws;
            }
        );
    }

    @Override
    public Iterator<Map<String, Object>> iterator() {
        return new Unchecked<>(this.rows).value().iterator();
    }
}
