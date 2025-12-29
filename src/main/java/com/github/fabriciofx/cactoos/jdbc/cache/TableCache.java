package com.github.fabriciofx.cactoos.jdbc.cache;

import java.util.Map;
import org.cactoos.map.MapOf;

public final class TableCache implements Cache<String, Table> {
    private final Map<String, Table> tables;

    public TableCache() {
        this(new MapOf<>());
    }

    public TableCache(final Map<String, Table> tables) {
        this.tables = tables;
    }

    @Override
    public Table retrieve(final String name) {
        return this.tables.get(name);
    }

    @Override
    public void store(final String name, final Table table) {
        this.tables.put(name, table);
    }

    @Override
    public boolean contains(final String name) {
        return this.tables.containsKey(name);
    }

    @Override
    public void clear() {
        this.tables.clear();
    }
}
