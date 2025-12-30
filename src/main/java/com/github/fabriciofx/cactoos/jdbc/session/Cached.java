package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.cache.Cache;
import com.github.fabriciofx.cactoos.jdbc.cache.ResultSetCache;
import java.sql.Connection;
import java.sql.ResultSet;

public final class Cached implements Session {
    private final Session origin;
    private final Cache<String, ResultSet> cache;

    public Cached(final Session session) {
        this(session, new ResultSetCache());
    }

    public Cached(final Session session, final Cache<String, ResultSet> cache) {
        this.origin = session;
        this.cache = cache;
    }

    @Override
    public Connection connection() throws Exception {
        return new com.github.fabriciofx.cactoos.jdbc.connection.Cached(
            this.origin.connection(),
            this.cache
        );
    }

    @Override
    public String url() throws Exception {
        return this.origin.url();
    }

    @Override
    public String username() {
        return this.origin.username();
    }

    @Override
    public String password() {
        return this.origin.password();
    }
}
