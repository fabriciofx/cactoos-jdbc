package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.cache.Table;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Cached implements Session {
    private final Map<String, Table> cache;
    private final Session origin;

    public Cached(final Session session) {
        this.origin = session;
        this.cache = new ConcurrentHashMap<>();
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
