/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.cacheability;

import java.util.Set;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeName;

/**
 * Table.
 * @since 0.9.0
 */
public final class Table extends AbstractTable {
    /**
     * Table name.
     */
    private final String name;

    /**
     * Columns names.
     */
    private final Set<String> columns;

    /**
     * Ctor.
     * @param name Table name
     * @param columns Columns names
     */
    public Table(final String name, final Set<String> columns) {
        this.name = name;
        this.columns = columns;
    }

    @Override
    public RelDataType getRowType(final RelDataTypeFactory factory) {
        final RelDataTypeFactory.Builder builder = factory.builder();
        for (final String column : this.columns) {
            if (!column.equals(this.name)) {
                builder.add(column, SqlTypeName.ANY);
            }
        }
        if (builder.getFieldCount() == 0) {
            builder.add("DUMMY_COL", SqlTypeName.ANY);
        }
        return builder.build();
    }
}
