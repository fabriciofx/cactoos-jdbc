/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.table;

import com.github.fabriciofx.cactoos.jdbc.Columns;
import com.github.fabriciofx.cactoos.jdbc.Row;
import com.github.fabriciofx.cactoos.jdbc.Rows;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.columns.LinkedColumns;
import com.github.fabriciofx.cactoos.jdbc.row.LinkedRow;
import com.github.fabriciofx.cactoos.jdbc.rows.LinkedRows;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import org.cactoos.Scalar;
import org.cactoos.scalar.Sticky;
import org.cactoos.text.Lowered;

/**
 * LinkedTable.
 * @since 0.9.0
 */
public final class LinkedTable implements Table {
    /**
     * Rows.
     */
    private final Scalar<Rows> records;

    /**
     * Columns.
     */
    private final Scalar<Columns> labels;

    /**
     * Ctor.
     * @param rset A {@link ResultSet}
     */
    public LinkedTable(final ResultSet rset) {
        this.records = new Sticky<>(
            () -> {
                final Rows rows = new LinkedRows();
                final ResultSetMetaData meta = rset.getMetaData();
                while (rset.next()) {
                    final Row row = new LinkedRow();
                    for (int col = 1; col <= meta.getColumnCount(); ++col) {
                        row.add(
                            new Lowered(meta.getColumnLabel(col)).asString(),
                            rset.getObject(col)
                        );
                    }
                    rows.add(row);
                }
                return rows;
            }
        );
        this.labels = new Sticky<>(
            () -> {
                final ResultSetMetaData meta = rset.getMetaData();
                final Columns columns = new LinkedColumns();
                for (int col = 1; col <= meta.getColumnCount(); ++col) {
                    columns.add(
                        new Lowered(meta.getColumnName(col)).asString(),
                        meta.getColumnType(col)
                    );
                }
                return columns;
            }
        );
    }

    @Override
    public Rows rows() throws Exception {
        return this.records.value();
    }

    @Override
    public Columns columns() throws Exception {
        return this.labels.value();
    }

    @Override
    public byte[] asBytes() throws Exception {
        return this.records.value().asBytes();
    }
}
