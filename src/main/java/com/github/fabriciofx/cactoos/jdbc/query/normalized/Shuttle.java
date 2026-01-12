/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.normalized;

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOrderBy;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.SqlWith;
import org.apache.calcite.sql.util.SqlShuttle;
import org.cactoos.scalar.Unchecked;

/**
 * Normalized Shuttle.
 * @since 0.9.0
 */
public final class Shuttle extends SqlShuttle {
    @Override
    public SqlNode visit(final SqlCall call) {
        final SqlNode ordered;
        if (call instanceof final SqlWith with) {
            ordered = new Unchecked<>(new OrderedWith(this, with)).value();
        } else if (call instanceof SqlOrderBy orderBy) {
            ordered = new Unchecked<>(
                new OrderedOrderBy(this, orderBy)
            ).value();
        } else if (call instanceof SqlSelect select) {
            ordered = new Unchecked<>(new OrderedSelect(this, select)).value();
        } else {
            ordered = super.visit(call);
        }
        return ordered;
    }
}
