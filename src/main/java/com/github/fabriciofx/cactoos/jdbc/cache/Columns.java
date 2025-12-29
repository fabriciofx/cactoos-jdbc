package com.github.fabriciofx.cactoos.jdbc.cache;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cactoos.Scalar;
import org.cactoos.scalar.Sticky;

public final class Columns {
    private final Scalar<Map<String, Integer>> items;

    public Columns(final ResultSet rset) {
        this.items = new Sticky<>(
            () -> {
                final ResultSetMetaData meta = rset.getMetaData();
                final Map<String, Integer> columns = new HashMap<>();
                for (int idx = 1; idx <= meta.getColumnCount(); idx++) {
                    columns.put(
                        meta.getColumnName(idx).toLowerCase(),
                        meta.getColumnType(idx)
                    );
                }
                return columns;
            }
        );
    }

    public int count() throws Exception {
        return this.items.value().size();
    }

    public List<String> names() throws Exception {
        return new ArrayList<>(this.items.value().keySet());
    }

    public int type(final String name) throws Exception {
        return this.items.value().get(name);
    }
}
