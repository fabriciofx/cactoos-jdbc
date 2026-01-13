/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.normalized;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.cactoos.Scalar;
import org.cactoos.scalar.Unchecked;

/**
 * SortedNodes.
 * @since 0.9.0
 */
public final class SortedNodes implements Scalar<SqlNodeList> {
    /**
     * Shuttle.
     */
    private final Shuttle shuttle;

    /**
     * Nodes list.
     */
    private final SqlNodeList list;

    /**
     * Ctor.
     * @param shuttle A Shuttle
     * @param list A nodes list
     */
    public SortedNodes(final Shuttle shuttle, final SqlNodeList list) {
        this.shuttle = shuttle;
        this.list = list;
    }

    @Override
    public SqlNodeList value() throws Exception {
        final List<SqlNode> sorted = new ArrayList<>(this.list.size());
        for (final SqlNode node : this.list) {
            sorted.add(
                new Unchecked<>(
                    new SortedExpression(this.shuttle, node)
                ).value()
            );
        }
        sorted.sort(Comparator.comparing(SqlNode::toString));
        return new SqlNodeList(sorted, this.list.getParserPosition());
    }
}
