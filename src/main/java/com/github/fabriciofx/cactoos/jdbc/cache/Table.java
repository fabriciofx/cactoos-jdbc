package com.github.fabriciofx.cactoos.jdbc.cache;

import java.sql.ResultSet;

public final class Table {
    private final Rows rows;
    private final Columns columns;
    private final String name;

    public Table(final ResultSet rset, final String name) {
        this.rows = new Rows(rset);
        this.columns = new Columns(rset);
        this.name = name;
    }

    public Rows rows() {
        return this.rows;
    }

    public Columns columns() {
        return this.columns;
    }

    public String name() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("Table(%s)", this.name);
    }
}
