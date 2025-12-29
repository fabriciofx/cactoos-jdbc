package com.github.fabriciofx.cactoos.jdbc.cache;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cactoos.Scalar;
import org.cactoos.scalar.Sticky;

public final class Rows {
    private final Scalar<List<Map<String, Object>>> items;

    public Rows(final ResultSet rset) {
        this.items = new Sticky<>(
            () -> {
                final List<Map<String, Object>> rows = new ArrayList<>();
                final ResultSetMetaData meta = rset.getMetaData();
                while (rset.next()) {
                    final Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        row.put(
                            meta.getColumnLabel(i).toLowerCase(),
                            rset.getObject(i)
                        );
                    }
                    rows.add(row);
                }
                return rows;
            }
        );
    }

    public int count() throws Exception {
        return this.items.value().size();
    }

    public Map<String, Object> row(final int number) throws Exception {
        return this.items.value().get(number);
    }
}
