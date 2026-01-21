/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.paginated;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlOrderBy;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.cactoos.Scalar;

/**
 * GroupedSelect.
 *
 * @since 0.9.0
 */
public final class GroupedOrder implements Scalar<SqlNode> {
    /**
     * Select.
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
     * @param order A SqlSelect
     * @param page Page number
     * @param size Rows per page
     */
    public GroupedOrder(
        final SqlOrderBy order,
        final int page,
        final int size
    ) {
        this.order = order;
        this.page = page;
        this.size = size;
    }

    @Override
    public SqlNode value() throws Exception {
        final SqlSelect select = (SqlSelect) this.order.query;
        final SqlNodeList list = new SqlNodeList(
            select.getParserPosition()
        );
        list.add(SqlIdentifier.star(select.getParserPosition()));
        list.add(
            SqlStdOperatorTable.AS.createCall(
                select.getParserPosition(),
                SqlStdOperatorTable.OVER.createCall(
                    select.getParserPosition(),
                    SqlStdOperatorTable.COUNT.createCall(
                        select.getParserPosition(),
                        SqlIdentifier.star(select.getParserPosition())
                    ),
                    SqlNodeList.EMPTY
                ),
                new SqlIdentifier(
                    "__total_count__",
                    select.getParserPosition()
                )
            )
        );
        return new SqlOrderBy(
            select.getParserPosition(),
            new SqlSelect(
                select.getParserPosition(),
                null,
                list,
                SqlStdOperatorTable.AS.createCall(
                    select.getParserPosition(),
                    select,
                    new SqlIdentifier(
                        "__paginated_subquery__",
                        select.getParserPosition()
                    )
                ),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            ),
            this.order.orderList,
            SqlLiteral.createExactNumeric(
                String.valueOf((this.page - 1) * this.size),
                this.order.getParserPosition()
            ),
            SqlLiteral.createExactNumeric(
                String.valueOf(this.size),
                this.order.getParserPosition()
            )
        );
    }
}
