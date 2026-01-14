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
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.cactoos.Scalar;

/**
 * GroupedSelect.
 * @since 0.9.0
 */
public final class GroupedSelect implements Scalar<SqlNode> {
    /**
     * Select.
     */
    private final SqlSelect select;

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
     * @param select A SqlSelect
     * @param page Page number
     * @param size Rows per page
     */
    public GroupedSelect(
        final SqlSelect select,
        final int page,
        final int size
    ) {
        this.select = select;
        this.page = page;
        this.size = size;
    }

    @Override
    public SqlNode value() throws Exception {
        final SqlIdentifier sub = new SqlIdentifier(
            "__paginated_subquery__",
            this.select.getParserPosition()
        );
        final SqlBasicCall from = (SqlBasicCall)
            SqlStdOperatorTable.AS.createCall(
                this.select.getParserPosition(),
                this.select,
                sub
            );
        final SqlNodeList list = new SqlNodeList(
            this.select.getParserPosition()
        );
        list.add(SqlIdentifier.star(this.select.getParserPosition()));
        final SqlNode count = SqlStdOperatorTable.COUNT.createCall(
            this.select.getParserPosition(),
            SqlIdentifier.star(this.select.getParserPosition())
        );
        final SqlNode over = SqlStdOperatorTable.OVER.createCall(
            this.select.getParserPosition(),
            count,
            SqlNodeList.EMPTY
        );
        final SqlNode alias = SqlStdOperatorTable.AS.createCall(
            this.select.getParserPosition(),
            over,
            new SqlIdentifier(
                "__total_count__",
                this.select.getParserPosition()
            )
        );
        list.add(alias);
        final int off = (this.page - 1) * this.size;
        return new SqlSelect(
            this.select.getParserPosition(),
            null,
            list,
            from,
            null,
            null,
            null,
            null,
            null,
            null,
            SqlLiteral.createExactNumeric(
                String.valueOf(off),
                this.select.getParserPosition()
            ),
            SqlLiteral.createExactNumeric(
                String.valueOf(this.size),
                this.select.getParserPosition()
            ),
            null
        );
    }
}
