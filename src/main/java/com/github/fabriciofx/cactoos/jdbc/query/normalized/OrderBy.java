/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.normalized;

import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlOrderBy;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;

/**
 * OrderBy.
 *
 * @since 0.9.0
 */
public final class OrderBy {
    /**
     * OrderBy.
     */
    private final SqlOrderBy order;

    /**
     * Ctor.
     *
     * @param order A SqlOrderBy
     */
    public OrderBy(final SqlOrderBy order) {
        this.order = order;
    }

    /**
     * Checks if order by is semantic.
     *
     * @param query An order by expression
     * @return True if order by is semantic, false otherwise
     */
    public boolean semantic(final SqlNode query) {
        boolean semantic = false;
        if (this.order.fetch != null || this.order.offset != null) {
            semantic = true;
        } else if (query instanceof SqlSelect select) {
            if (select.getQualify() != null) {
                semantic = true;
            } else if (select.getWindowList() != null
                && !select.getWindowList().isEmpty()
            ) {
                semantic = true;
            } else {
                semantic = OrderBy.containsWindow(select.getSelectList())
                    || OrderBy.containsWindow(select.getWhere())
                    || OrderBy.containsWindow(select.getHaving());
            }
        }
        return semantic;
    }

    private static boolean containsWindow(final SqlNode node) {
        boolean found = false;
        if (node instanceof SqlBasicCall call) {
            found = call.getOperator() == SqlStdOperatorTable.OVER;
            for (final SqlNode operand : call.getOperandList()) {
                found = found || containsWindow(operand);
            }
        }
        if (node instanceof SqlNodeList list) {
            for (final SqlNode element : list) {
                found = found || containsWindow(element);
            }
        }
        return found;
    }
}
