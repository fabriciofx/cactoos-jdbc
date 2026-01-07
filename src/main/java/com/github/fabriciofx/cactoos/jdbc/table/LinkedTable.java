/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.table;

import com.github.fabriciofx.cactoos.jdbc.Columns;
import com.github.fabriciofx.cactoos.jdbc.Row;
import com.github.fabriciofx.cactoos.jdbc.Rows;
import com.github.fabriciofx.cactoos.jdbc.Table;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import org.cactoos.text.Lowered;

/**
 * LinkedTable.
 * @since 0.9.0
 */
public final class LinkedTable implements Table {
    /**
     * ResultSet.
     */
    private final ResultSet rset;

    /**
     * Ctor.
     * @param rset A {@link ResultSet}
     */
    public LinkedTable(final ResultSet rset) {
        this.rset = rset;
    }

    @Override
    public Rows rows() throws Exception {
        final Rows rows = new LinkedRows();
        final ResultSetMetaData meta = this.rset.getMetaData();
        while (this.rset.next()) {
            final Row row = new LinkedRow();
            for (int col = 1; col <= meta.getColumnCount(); ++col) {
                row.add(
                    new Lowered(meta.getColumnLabel(col)).asString(),
                    this.rset.getObject(col)
                );
            }
            rows.add(row);
        }
        return rows;
    }

    @Override
    public Columns columns() throws Exception {
        final ResultSetMetaData meta = this.rset.getMetaData();
        final Columns columns = new LinkedColumns();
        for (int col = 1; col <= meta.getColumnCount(); ++col) {
            columns.add(
                new Lowered(meta.getColumnName(col)).asString(),
                meta.getColumnType(col)
            );
        }
        return columns;
    }
}
