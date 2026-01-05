package com.github.fabriciofx.cactoos.jdbc.sql;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.util.SqlShuttle;

public final class CacheableShuttle extends SqlShuttle {
    private final AtomicBoolean cacheable;
    private final Set<String> tables;
    private final Set<String> columns;

    public CacheableShuttle(
        final AtomicBoolean cacheable,
        final Set<String> tables,
        final Set<String> columns
    ) {
        this.cacheable = cacheable;
        this.tables = tables;
        this.columns = columns;
    }

    @Override
    public SqlNode visit(final SqlIdentifier id) {
        if (id.isStar()) {
            this.cacheable.set(false);
        }
        if (id.names.size() > 1) {
            this.tables.add(id.names.get(0).toUpperCase());
            this.columns.add(id.names.get(1).toUpperCase());
        } else {
            final String name = id.getSimple().toUpperCase();
            this.tables.add(name);
            this.columns.add(name);
        }
        return super.visit(id);
    }
}
