/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.normalized;

import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlJoin;
import org.apache.calcite.sql.SqlNode;
import org.cactoos.Scalar;

/**
 * SortedFrom.
 * @since 0.9.0
 */
public final class SortedFrom implements Scalar<SqlNode> {
    /**
     * Shuttle.
     */
    private final Shuttle shuttle;

    /**
     * From.
     */
    private final SqlNode from;

    /**
     * Ctor.
     * @param shuttle A Shuttle
     * @param from A select from
     */
    public SortedFrom(final Shuttle shuttle, final SqlNode from) {
        this.shuttle = shuttle;
        this.from = from;
    }

    @Override
    public SqlNode value() throws Exception {
        final SqlNode sorted;
        if (this.from instanceof final SqlJoin join) {
            sorted = new SqlJoin(
                join.getParserPosition(),
                new SortedFrom(this.shuttle, join.getLeft()).value(),
                join.isNaturalNode(),
                join.getJoinTypeNode(),
                new SortedFrom(this.shuttle, join.getRight()).value(),
                join.getConditionTypeNode(),
                new SortedExpression(this.shuttle, join.getCondition()).value()
            );
        } else if (this.from instanceof SqlCall) {
            sorted = this.from.accept(this.shuttle);
        } else {
            sorted = this.from;
        }
        return sorted;
    }
}
