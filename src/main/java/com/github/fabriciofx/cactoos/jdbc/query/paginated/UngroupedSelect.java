/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.paginated;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.cactoos.Scalar;

/**
 * UngroupedSelect.
 * @since 0.9.0
 */
public final class UngroupedSelect implements Scalar<SqlNode> {
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
     * @param select A SqlSelect
     * @param page Page number
     * @param size Rows per page
     */
    public UngroupedSelect(
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
        final SqlNodeList list = new SqlNodeList(
            this.select.getParserPosition()
        );
        list.addAll(this.select.getSelectList());
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
        final int offset = (this.page - 1) * this.size;
        return new SqlSelect(
            this.select.getParserPosition(),
            new SqlNodeList(this.select.getParserPosition()),
            list,
            this.select.getFrom(),
            this.select.getWhere(),
            this.select.getGroup(),
            this.select.getHaving(),
            this.select.getWindowList(),
            this.select.getQualify(),
            this.select.getOrderList(),
            SqlLiteral.createExactNumeric(
                String.valueOf(offset),
                this.select.getParserPosition()
            ),
            SqlLiteral.createExactNumeric(
                String.valueOf(this.size),
                this.select.getParserPosition()
            ),
            this.select.getHints()
        );
    }
}
