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
 * SortedSelect.
 * @since 0.9.0
 */
public final class SortedSelect implements Scalar<SqlNodeList> {
    /**
     * Select nodes.
     */
    private final SqlNodeList list;

    /**
     * Ctor.
     * @param list Select nodes
     */
    public SortedSelect(final SqlNodeList list) {
        this.list = list;
    }

    @Override
    public SqlNodeList value() throws Exception {
        final List<SqlNode> nodes = new ArrayList<>(this.list);
        nodes.sort(
            (left, right) -> new ComparableText(
                new NodeName(left)
            ).compareTo(new NodeName(right))
        );
        return new SqlNodeList(nodes, this.list.getParserPosition());
    }
}
