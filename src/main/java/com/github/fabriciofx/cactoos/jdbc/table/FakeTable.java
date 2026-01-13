/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.table;

import com.github.fabriciofx.cactoos.jdbc.Columns;
import com.github.fabriciofx.cactoos.jdbc.Rows;
import com.github.fabriciofx.cactoos.jdbc.Table;

/**
 * FakeTable.
 * @since 0.9.0
 */
public final class FakeTable implements Table {
    /**
     * Rows.
     */
    private final Rows records;

    /**
     * Columns.
     */
    private final Columns labels;

    /**
     * Ctor.
     * @param rows Rows
     * @param columns Columns
     */
    public FakeTable(final Rows rows, final Columns columns) {
        this.records = rows;
        this.labels = columns;
    }

    @Override
    public Rows rows() throws Exception {
        return this.records;
    }

    @Override
    public Columns columns() throws Exception {
        return this.labels;
    }
}
