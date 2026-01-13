/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.normalized;

import java.util.ArrayList;
import java.util.List;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.cactoos.Scalar;
import org.cactoos.text.ComparableText;

/**
 * SortedWith.
 * @since 0.9.0
 */
public final class SortedWith implements Scalar<SqlNodeList> {
    /**
     * Shuttle.
     */
    private final Shuttle shuttle;

    /**
     * With nodes.
     */
    private final SqlNodeList list;

    /**
     * Ctor.
     * @param shuttle A Shuttle
     * @param list With nodes list
     */
    public SortedWith(final Shuttle shuttle, final SqlNodeList list) {
        this.shuttle = shuttle;
        this.list = list;
    }

    @Override
    public SqlNodeList value() throws Exception {
        final List<SqlNode> nodes = new ArrayList<>(this.list.size());
        for (final SqlNode node : this.list) {
            nodes.add(node.accept(this.shuttle));
        }
        nodes.sort(
            (left, right) -> new ComparableText(
                new NodeName(left)
            ).compareTo(new NodeName(right))
        );
        return new SqlNodeList(nodes, this.list.getParserPosition());
    }
}
