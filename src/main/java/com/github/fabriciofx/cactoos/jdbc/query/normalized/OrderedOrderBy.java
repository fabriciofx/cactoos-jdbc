/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.normalized;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlOrderBy;
import org.cactoos.Scalar;
import org.cactoos.scalar.Unchecked;

/**
 * OrderedOrderBy.
 * @since 0.9.0
 */
public final class OrderedOrderBy implements Scalar<SqlNode> {
    /**
     * Shuttle.
     */
    private final Shuttle shuttle;

    /**
     * OrderBy.
     */
    private final SqlOrderBy order;

    /**
     * Ctor.
     * @param shuttle A Shuttle
     * @param order A SqlOrderBy
     */
    public OrderedOrderBy(final Shuttle shuttle, final SqlOrderBy order) {
        this.shuttle = shuttle;
        this.order = order;
    }

    @Override
    public SqlNode value() throws Exception {
        final SqlNode ordered;
        final SqlNode query = this.order.query.accept(this.shuttle);
        if (new OrderBy(this.order).semantic(query)) {
            final SqlNodeList sorted = new SqlNodeList(
                this.order.orderList.getParserPosition()
            );
            for (final SqlNode node : this.order.orderList) {
                sorted.add(
                    new Unchecked<>(
                        new SortedExpression(this.shuttle, node)
                    ).value()
                );
            }
            ordered = new SqlOrderBy(
                this.order.getParserPosition(),
                query,
                sorted,
                this.order.offset,
                this.order.fetch
            );
        } else {
            ordered = query;
        }
        return ordered;
    }
}
