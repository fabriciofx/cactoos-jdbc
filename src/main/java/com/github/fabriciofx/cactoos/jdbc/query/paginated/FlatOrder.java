/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.paginated;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlOrderBy;
import org.apache.calcite.sql.SqlSelect;
import org.cactoos.Scalar;

/**
 * FlatOrder.
 *
 * <p>The page of a select without GROUP BY or DISTINCT that keeps its
 * ORDER BY, which Calcite holds in a {@link SqlOrderBy} around the
 * select.</p>
 *
 * @since 0.9.0
 */
public final class FlatOrder implements Scalar<SqlNode> {

    /**
     * Ordered select.
     */
    private final SqlOrderBy order;

    /**
     * Page number.
     */
    private final int page;

    /**
     * Rows per page.
     */
    private final int size;

    /**
     * Ctor.
     *
     * @param order An ordered select
     * @param page Page number
     * @param size Rows per page
     */
    public FlatOrder(final SqlOrderBy order, final int page, final int size) {
        this.order = order;
        this.page = page;
        this.size = size;
    }

    @Override
    public SqlNode value() throws Exception {
        final SqlSelect select = (SqlSelect) this.order.query;
        return new FlatSelect(
            new SqlSelect(
                select.getParserPosition(),
                new SqlNodeList(select.getParserPosition()),
                select.getSelectList(),
                select.getFrom(),
                select.getWhere(),
                select.getGroup(),
                select.getHaving(),
                select.getWindowList(),
                select.getQualify(),
                this.order.orderList,
                null,
                null,
                select.getHints()
            ),
            this.page,
            this.size
        ).value();
    }
}
