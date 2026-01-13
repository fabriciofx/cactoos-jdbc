/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.table;

import com.github.fabriciofx.cactoos.jdbc.Columns;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.cactoos.text.FormattedText;

/**
 * LinkedColumns.
 * @since 0.9.0
 */
public final class LinkedColumns implements Columns {
    /**
     * Columns' items.
     */
    private final Map<String, Integer> items;

    /**
     * Ctor.
     */
    public LinkedColumns() {
        this(new LinkedHashMap<>());
    }

    /**
     * Ctor.
     * @param items Data to initialize the columns
     */
    public LinkedColumns(final Map<String, Integer> items) {
        this.items = items;
    }

    @Override
    public int count() {
        return this.items.size();
    }

    @Override
    public void add(final String name, final Integer type) {
        this.items.put(name, type);
    }

    @Override
    public Integer type(final String name) throws Exception {
        if (!this.items.containsKey(name)) {
            throw new IllegalArgumentException(
                new FormattedText(
                    "Column name '%s' does not exist",
                    name
                ).asString()
            );
        }
        return this.items.get(name);
    }

    @Override
    public String name(final int index) throws Exception {
        final Set<String> keys = this.items.keySet();
        if (index < 1 || index > keys.size()) {
            throw new IllegalArgumentException(
                new FormattedText(
                    "Column number '%d' does not exist",
                    index
                ).asString()
            );
        }
        return keys.stream().skip(index - 1).findFirst().orElseThrow();
    }

    @Override
    public int index(final String name) throws Exception {
        if (!this.items.containsKey(name)) {
            throw new IllegalArgumentException(
                new FormattedText(
                    "Column name '%s' does not exist",
                    name
                ).asString()
            );
        }
        int index = 0;
        for (final String key : this.items.keySet()) {
            if (key.equals(name)) {
                break;
            }
            ++index;
        }
        return index;
    }
}
