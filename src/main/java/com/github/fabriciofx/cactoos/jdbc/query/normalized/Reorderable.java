/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.normalized;

import com.github.fabriciofx.cactoos.jdbc.scalar.HasParams;
import java.util.List;
import org.apache.calcite.sql.SqlNode;
import org.cactoos.Scalar;

/**
 * Reorderable.
 *
 * <p>Tells whether sibling SQL fragments can be reordered without changing
 * which value each positional param ({@code ?}) receives. That holds while at
 * most one of them holds params.</p>
 *
 * @since 0.9.0
 */
public final class Reorderable implements Scalar<Boolean> {

    /**
     * Sibling SQL fragments.
     */
    private final List<SqlNode> nodes;

    /**
     * Ctor.
     *
     * @param nodes Sibling SQL fragments
     */
    public Reorderable(final List<SqlNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public Boolean value() {
        int count = 0;
        for (final SqlNode node : this.nodes) {
            if (new HasParams(node).value()) {
                ++count;
            }
        }
        return count < 2;
    }
}
