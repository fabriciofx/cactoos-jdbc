package com.github.fabriciofx.cactoos.jdbc.sql;

import java.util.Set;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeName;

public final class CacheableTable extends AbstractTable {
    private final String table;
    private final Set<String> columns;

    public CacheableTable(final String table, final Set<String> columns) {
        this.table = table;
        this.columns = columns;
    }

    @Override
    public RelDataType getRowType(final RelDataTypeFactory factory) {
        final RelDataTypeFactory.Builder builder = factory.builder();
        for (String column : this.columns) {
            if (!column.equals(this.table)) {
                builder.add(column, SqlTypeName.ANY);
            }
        }
        if (builder.getFieldCount() == 0) {
            builder.add("DUMMY_COL", SqlTypeName.ANY);
        }
        return builder.build();
    }
}
