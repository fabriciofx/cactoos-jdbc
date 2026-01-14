/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.paginated;

import org.apache.calcite.sql.SqlBasicCall;
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
        final SqlIdentifier sub = new SqlIdentifier(
            "__paginated_subquery__",
            select.getParserPosition()
        );
        final SqlBasicCall from = (SqlBasicCall)
            SqlStdOperatorTable.AS.createCall(
                select.getParserPosition(),
                select,
                sub
            );
        final SqlNodeList list = new SqlNodeList(
            select.getParserPosition()
        );
        list.add(SqlIdentifier.star(select.getParserPosition()));
        final SqlNode count = SqlStdOperatorTable.COUNT.createCall(
            select.getParserPosition(),
            SqlIdentifier.star(select.getParserPosition())
        );
        final SqlNode over = SqlStdOperatorTable.OVER.createCall(
            select.getParserPosition(),
            count,
            SqlNodeList.EMPTY
        );
        final SqlNode alias = SqlStdOperatorTable.AS.createCall(
            select.getParserPosition(),
            over,
            new SqlIdentifier(
                "__total_count__",
                select.getParserPosition()
            )
        );
        list.add(alias);
        final SqlSelect outer = new SqlSelect(
            select.getParserPosition(),
            null,
            list,
            from,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
        final int offset = (this.page - 1) * this.size;
        return new SqlOrderBy(
            select.getParserPosition(),
            outer,
            this.order.orderList,
            SqlLiteral.createExactNumeric(
                String.valueOf(offset),
                this.order.getParserPosition()
            ),
            SqlLiteral.createExactNumeric(
                String.valueOf(this.size),
                this.order.getParserPosition()
            )
        );
    }
}
