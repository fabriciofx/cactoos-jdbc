package com.github.fabriciofx.cactoos.jdbc.connection;

import com.github.fabriciofx.cactoos.jdbc.cache.Cache;
import com.github.fabriciofx.cactoos.jdbc.sql.cache.IsSelect;
import com.github.fabriciofx.cactoos.jdbc.sql.cache.NormalizedSelect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class Cached extends ConnectionEnvelope {
    private final Cache<String, ResultSet> cache;

    public Cached(
        final Connection connection,
        final Cache<String, ResultSet> cache
    ) {
        super(connection);
        this.cache = cache;
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql
    ) throws SQLException {
        try {
            final PreparedStatement prepared;
            if (new IsSelect(sql).value()) {
                prepared = new com.github.fabriciofx.cactoos.jdbc.prepared.Cached(
                    super.prepareStatement(sql),
                    super.prepareStatement(
                        new NormalizedSelect(sql).asString(),
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY
                    ),
                    new NormalizedSelect(sql),
                    this.cache
                );
            } else {
                prepared = super.prepareStatement(sql);
            }
            return prepared;
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }
}
