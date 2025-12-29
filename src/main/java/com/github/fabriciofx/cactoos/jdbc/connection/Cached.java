package com.github.fabriciofx.cactoos.jdbc.connection;

import com.github.fabriciofx.cactoos.jdbc.cache.Table;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public final class Cached extends ConnectionEnvelope {
    private final Map<String, Table> cache;

    public Cached(
        final Connection connection,
        final Map<String, Table> cache
    ) {
        super(connection);
        this.cache = cache;
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql
    ) throws SQLException {
        if (sql.contains("SELECT")) {
            final String expanded = "SELECT * FROM person";
            return new com.github.fabriciofx.cactoos.jdbc.prepared.Cached(
                super.prepareStatement(sql),
                super.prepareStatement(expanded),
                sql,
                this.cache
            );
        }
        return super.prepareStatement(sql);
    }
}
