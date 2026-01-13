/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.normalized;

import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWithItem;
import org.cactoos.Text;

/**
 * NodeName.
 * @since 0.9.0
 */
public final class NodeName implements Text {
    /**
     * Node.
     */
    private final SqlNode node;

    /**
     * Ctor.
     * @param node A SqlNode
     */
    public NodeName(final SqlNode node) {
        this.node = node;
    }

    @Override
    public String asString() throws Exception {
        String name = this.node.toString();
        if (this.node instanceof final SqlWithItem item) {
            name = item.name.toString();
        } else if (this.node instanceof final SqlBasicCall call) {
            if ("AS".equals(call.getOperator().getName())) {
                name = call.getOperandList().get(1).toString();
            }
        } else if (this.node instanceof final SqlIdentifier id) {
            name = id.names.get(id.names.size() - 1);
        }
        return name;
    }
}
