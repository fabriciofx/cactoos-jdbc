/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.rows;

import com.github.fabriciofx.cactoos.jdbc.Row;
import com.github.fabriciofx.cactoos.jdbc.Rows;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import org.cactoos.text.FormattedText;

/**
 * LinkedRows.
 * @since 0.9.0
 */
public final class LinkedRows implements Rows {
    /**
     * The rows.
     */
    private final List<Row> items;

    /**
     * Ctor.
     */
    public LinkedRows() {
        this(new LinkedList<>());
    }

    /**
     * Ctor.
     * @param items Data to initialize the columns
     */
    public LinkedRows(final List<Row> items) {
        this.items = items;
    }

    @Override
    public int count() {
        return this.items.size();
    }

    @Override
    public void add(final Row row) {
        this.items.add(row);
    }

    @Override
    public Row row(final int index) throws Exception {
        if (index < 0 || index >= this.items.size()) {
            throw new IndexOutOfBoundsException(
                new FormattedText(
                    "Row '%d' does not exist [0,%d]",
                    index,
                    this.items.size() - 1
                ).asString()
            );
        }
        return this.items.get(index);
    }

    @Override
    public byte[] asBytes() throws Exception {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            for (final Row row : this.items) {
                stream.write(row.asBytes());
            }
            return stream.toByteArray();
        }
    }
}
