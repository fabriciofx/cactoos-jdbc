/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.scalar;

import java.util.List;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlDynamicParam;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.cactoos.Scalar;

/**
 * HasParams.
 *
 * <p>Tells whether a parsed SQL fragment holds a positional param
 * ({@code ?}) anywhere inside it. Reordering two fragments that hold params
 * changes which value each param receives.</p>
 *
 * @since 0.9.0
 */
public final class HasParams implements Scalar<Boolean> {

    /**
     * Parsed SQL fragment.
     */
    private final SqlNode node;

    /**
     * Ctor.
     *
     * @param node A parsed SQL fragment
     */
    public HasParams(final SqlNode node) {
        this.node = node;
    }

    @Override
    public Boolean value() {
        final List<SqlNode> children;
        if (this.node instanceof SqlCall call) {
            children = call.getOperandList();
        } else if (this.node instanceof SqlNodeList list) {
            children = list.getList();
        } else {
            children = List.of();
        }
        boolean found = this.node instanceof SqlDynamicParam;
        for (final SqlNode child : children) {
            found = found || child != null && new HasParams(child).value();
        }
        return found;
    }
}
