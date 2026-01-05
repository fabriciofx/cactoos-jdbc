package com.github.fabriciofx.cactoos.jdbc.sql;

import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelVisitor;
import org.apache.calcite.rel.core.Aggregate;
import org.apache.calcite.rel.core.Window;

public final class CacheableVisitor extends RelVisitor {
    private final AtomicBoolean cacheable;

    public CacheableVisitor(final AtomicBoolean cacheable) {
        this.cacheable = cacheable;
    }

    @Override
    public void visit(
        final RelNode node,
        final int ordinal,
        final RelNode parent
    ) {
        if (node instanceof Aggregate || node instanceof Window) {
            this.cacheable.set(false);
        }
        super.visit(node, ordinal, parent);
    }
}
