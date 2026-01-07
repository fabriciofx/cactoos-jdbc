/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.table;

import com.github.fabriciofx.cactoos.jdbc.Row;
import java.util.LinkedHashMap;
import java.util.Map;
import org.cactoos.text.FormattedText;

/**
 * LinkedRow.
 * @since 0.9.0
 */
public final class LinkedRow implements Row {
    /**
     * Row's items.
     */
    private final Map<String, Object> items;

    /**
     * Ctor.
     */
    public LinkedRow() {
        this(new LinkedHashMap<>());
    }

    /**
     * Ctor.
     * @param items Data to initialize the columns
     */
    public LinkedRow(final Map<String, Object> items) {
        this.items = items;
    }

    @Override
    public void add(final String column, final Object value) {
        this.items.put(column, value);
    }

    @Override
    public <T> T value(final String column, final Class<T> klass)
        throws Exception {
        if (!this.items.containsKey(column)) {
            throw new IllegalArgumentException(
                new FormattedText(
                    "Column '%s' does not exist",
                    column
                ).asString()
            );
        }
        return klass.cast(this.items.get(column));
    }
}
