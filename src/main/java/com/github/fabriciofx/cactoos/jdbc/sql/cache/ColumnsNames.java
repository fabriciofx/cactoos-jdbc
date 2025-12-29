package com.github.fabriciofx.cactoos.jdbc.sql.cache;

import java.util.ArrayList;
import java.util.List;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParser;
import org.cactoos.Scalar;

public final class ColumnsNames implements Scalar<List<String>> {
    private final String sql;

    public ColumnsNames(final String sql) {
        this.sql = sql;
    }

    @Override
    public List<String> value() throws Exception {
        final SqlParser parser = SqlParser.create(this.sql);
        final SqlNode node = parser.parseQuery();
        final SqlSelect select = (SqlSelect) node;
        final List<String> columns = new ArrayList<>();
        for (SqlNode item : select.getSelectList()) {
            columns.add(item.toString().toLowerCase());
        }
        return columns;
    }
}
