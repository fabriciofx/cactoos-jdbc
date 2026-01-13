/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.cacheability;

import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelVisitor;
import org.apache.calcite.rel.core.Aggregate;
import org.apache.calcite.rel.core.Window;

/**
 * Visitor.
 * @since 0.9.0
 */
public final class Visitor extends RelVisitor {
    /**
     * Cacheability.
     */
    private final AtomicBoolean cacheability;

    /**
     * Ctor.
     * @param cacheability Checks if it is cacheable
     */
    public Visitor(final AtomicBoolean cacheability) {
        this.cacheability = cacheability;
    }

    @Override
    public void visit(
        final RelNode node,
        final int ordinal,
        final RelNode parent
    ) {
        if (node instanceof Aggregate || node instanceof Window) {
            this.cacheability.set(false);
        }
        super.visit(node, ordinal, parent);
    }
}
