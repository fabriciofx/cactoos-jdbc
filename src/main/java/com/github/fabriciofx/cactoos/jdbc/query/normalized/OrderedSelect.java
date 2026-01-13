/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.normalized;

import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.SqlSelectKeyword;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.cactoos.Scalar;

/**
 * OrderedSelect.
 * @since 0.9.0
 */
public final class OrderedSelect implements Scalar<SqlNode> {
    /**
     * Shuttle.
     */
    private final Shuttle shuttle;

    /**
     * Select.
     */
    private final SqlSelect select;

    /**
     * Ctor.
     * @param shuttle A Shuttle
     * @param select A SqlSelect
     */
    public OrderedSelect(final Shuttle shuttle, final SqlSelect select) {
        this.shuttle = shuttle;
        this.select = select;
    }

    @Override
    public SqlNode value() throws Exception {
        SqlSelect ordered = this.select;
        if (this.select.getSelectList() != null) {
            this.select.setSelectList(
                new SortedSelect(this.select.getSelectList()).value()
            );
        }
        if (this.select.getFrom() != null) {
            this.select.setFrom(
                new SortedFrom(this.shuttle, this.select.getFrom()).value()
            );
        }
        if (this.select.getWhere() != null) {
            this.select.setWhere(
                new SortedExpression(
                    this.shuttle,
                    this.select.getWhere()
                ).value()
            );
        }
        final SqlNodeList keywords = SqlNodeList.of(
            SqlLiteral.createSymbol(
                SqlSelectKeyword.DISTINCT,
                SqlParserPos.ZERO
            )
        );
        final SqlNodeList group = this.select.getGroup();
        if (group != null && !group.isEmpty()) {
            ordered = new SqlSelect(
                this.select.getParserPosition(),
                keywords,
                this.select.getSelectList(),
                this.select.getFrom(),
                this.select.getWhere(),
                new SortedNodes(this.shuttle, group).value(),
                this.select.getHaving(),
                this.select.getWindowList(),
                this.select.getQualify(),
                this.select.getOrderList(),
                this.select.getOffset(),
                this.select.getFetch(),
                this.select.getHints()
            );
        }
        if (ordered.getHaving() != null) {
            ordered.setHaving(
                new SortedExpression(this.shuttle, ordered.getHaving()).value()
            );
        }
        final SqlNodeList orders = ordered.getOrderList();
        if (orders != null && !orders.isEmpty()) {
            for (int idx = 0; idx < orders.size(); ++idx) {
                orders.set(
                    idx,
                    new SortedExpression(
                        this.shuttle,
                        orders.get(idx)
                    ).value()
                );
            }
        }
        return ordered;
    }
}
