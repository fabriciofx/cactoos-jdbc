/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.query.Normalized;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.select.IsSelect;
import java.io.IOException;
import java.sql.PreparedStatement;
import javax.sql.rowset.CachedRowSet;
import org.cactoos.text.TextOf;

/**
 * Cached.
 * A decorator for Session that allows caching results.
 * @since 0.9.0
 */
public final class Cached implements Session {
    /**
     * Session.
     */
    private final Session origin;

    /**
     * Cache.
     */
    private final Cache<String, CachedRowSet> cache;

    /**
     * Ctor.
     * @param session A session
     * @param cache The cache
     */
    public Cached(
        final Session session,
        final Cache<String, CachedRowSet> cache
    ) {
        this.origin = session;
        this.cache = cache;
    }

    @Override
    public PreparedStatement prepared(final Query query) throws Exception {
        final PreparedStatement prepared;
        if (new IsSelect(query.sql()).value()) {
            prepared = new com.github.fabriciofx.cactoos.jdbc.prepared.Cached(
                this.origin.prepared(query),
                this.origin.prepared(
                    new QueryOf(
                        new TextOf(new Normalized(query).sql()),
                        query.params()
                    )
                ),
                new Normalized(query),
                this.cache
            );
        } else {
            prepared = this.origin.prepared(query);
        }
        return prepared;
    }

    @Override
    public PreparedStatement batched(final Query query) throws Exception {
        return this.origin.batched(query);
    }

    @Override
    public PreparedStatement keyed(final Query query) throws Exception {
        return this.origin.keyed(query);
    }

    @Override
    public void autoCommit(final boolean enabled) throws Exception {
        this.origin.autoCommit(enabled);
    }

    @Override
    public void commit() throws Exception {
        this.origin.commit();
    }

    @Override
    public void rollback() throws Exception {
        this.origin.rollback();
    }

    @Override
    public void close() throws IOException {
        this.origin.close();
    }
}
