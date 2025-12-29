package com.github.fabriciofx.cactoos.jdbc.prepared;

import com.github.fabriciofx.cactoos.jdbc.cache.Table;
import com.github.fabriciofx.cactoos.jdbc.sql.cache.ColumnsNames;
import com.github.fabriciofx.cactoos.jdbc.sql.cache.TablesNames;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.RowSetProvider;

public final class Cached extends PreparedStatementEnvelope {
    private final PreparedStatement expanded;
    private final String sql;
    private final Map<String, Table> cache;

    /**
     * Ctor.
     *
     * @param origin Decorated PreparedStatement
     */
    public Cached(
        final PreparedStatement origin,
        final PreparedStatement expanded,
        final String sql,
        final Map<String, Table> cache
    ) {
        super(origin);
        this.expanded = expanded;
        this.sql = sql;
        this.cache = cache;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        try {
            final List<String> tables = new TablesNames(this.sql).value();
            final List<String> columns = new ColumnsNames(this.sql).value();
            if (!this.cache.containsKey(tables.get(0))) {
                this.cache.put(
                    tables.get(0),
                    new Table(this.expanded.executeQuery(), tables.get(0))
                );
            }
            final Table table = this.cache.get(tables.get(0));
            final RowSetFactory rsf = RowSetProvider.newFactory();
            final CachedRowSet target = rsf.createCachedRowSet();
            final RowSetMetaDataImpl meta = new RowSetMetaDataImpl();
            int col = 1;
            for (final String column : table.columns().names()) {
                if (columns.contains(column)) {
                    meta.setColumnCount(col);
                    meta.setColumnName(col, column);
                    meta.setColumnType(col, table.columns().type(column));
                    col++;
                }
            }
            target.setMetaData(meta);
            for (final String column : columns) {
                for (int idx = 0; idx < table.rows().count(); idx++) {
                    target.moveToInsertRow();
                    target.updateObject(
                        column,
                        table.rows().row(idx).get(column)
                    );
                    target.insertRow();
                    target.moveToCurrentRow();
                }
            }
            target.beforeFirst();
            return target;
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }
}
