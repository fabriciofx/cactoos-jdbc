package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.cache.Cache;
import com.github.fabriciofx.cactoos.jdbc.cache.Table;
import com.github.fabriciofx.cactoos.jdbc.cache.TableCache;
import java.sql.Connection;

public final class Cached implements Session {
    private final Session origin;
    private final Cache<String, Table> cache;

    public Cached(final Session session) {
        this(session, new TableCache());
    }

    public Cached(final Session session, final Cache<String, Table> cache) {
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
