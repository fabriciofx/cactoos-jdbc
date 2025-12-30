package com.github.fabriciofx.cactoos.jdbc.prepared;

import com.github.fabriciofx.cactoos.jdbc.cache.Cache;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import org.cactoos.Text;

public final class Cached extends PreparedStatementEnvelope {
    private final PreparedStatement prepared;
    private final Text normalized;
    private final Cache<String, ResultSet> cache;

    /**
     * Ctor.
     *
     * @param origin Decorated PreparedStatement
     */
    public Cached(
        final PreparedStatement origin,
        final PreparedStatement prepared,
        final Text normalized,
        final Cache<String, ResultSet> cache
    ) {
        super(origin);
        this.prepared = prepared;
        this.normalized = normalized;
        this.cache = cache;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        try {
            if (!this.cache.contains(this.normalized.asString())) {
                this.cache.store(
                    this.normalized.asString(),
                    this.prepared.executeQuery()
                );
            }
            final ResultSet rset = this.cache.retrieve(this.normalized.asString());
            final RowSetFactory rsf = RowSetProvider.newFactory();
            final CachedRowSet crs = rsf.createCachedRowSet();
            crs.populate(rset);
            rset.beforeFirst();
            return crs;
        } catch (final Exception ex) {
            throw new SQLException(ex);
        }
    }
}
