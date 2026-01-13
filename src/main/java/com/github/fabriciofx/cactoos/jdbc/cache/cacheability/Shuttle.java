/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.cacheability;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.util.SqlShuttle;
import org.cactoos.text.UncheckedText;
import org.cactoos.text.Upper;

/**
 * Shuttle.
 * @since 0.9.0
 */
public final class Shuttle extends SqlShuttle {
    /**
     * Cacheability.
     */
    private final AtomicBoolean cacheability;

    /**
     * Tables names.
     */
    private final Set<String> tables;

    /**
     * Columns names.
     */
    private final Set<String> columns;

    /**
     * Ctor.
     * @param cacheability Checks if it is cacheable
     * @param tables Tables names
     * @param columns Columns names
     */
    public Shuttle(
        final AtomicBoolean cacheability,
        final Set<String> tables,
        final Set<String> columns
    ) {
        this.cacheability = cacheability;
        this.tables = tables;
        this.columns = columns;
    }

    @Override
    public SqlNode visit(final SqlIdentifier id) {
        if (id.isStar()) {
            this.cacheability.set(false);
        }
        if (id.names.size() > 1) {
            this.tables.add(
                new UncheckedText(
                    new Upper(id.names.get(0))
                ).asString()
            );
            this.columns.add(
                new UncheckedText(
                    new Upper(id.names.get(1))
                ).asString()
            );
        } else {
            final String name = new UncheckedText(
                new Upper(id.getSimple())
            ).asString();
            this.tables.add(name);
            this.columns.add(name);
        }
        return super.visit(id);
    }
}
