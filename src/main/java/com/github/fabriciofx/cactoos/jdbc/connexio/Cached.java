/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.connexio;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.select.IsSelect;
import com.github.fabriciofx.cactoos.jdbc.sql.NormalizedSql;
import java.io.IOException;
import java.sql.PreparedStatement;
import javax.sql.rowset.CachedRowSet;

/**
 * Cached.
 * A decorator for Connexio that allows caching results.
 * @since 0.9.0
 */
public final class Cached implements Connexio {
    /**
     * Connexio.
     */
    private final Connexio origin;

    /**
     * Cache.
     */
    private final Cache<String, CachedRowSet> cache;

    /**
     * Ctor.
     * @param connexio A connexio
     * @param cache The cache
     */
    public Cached(
        final Connexio connexio,
        final Cache<String, CachedRowSet> cache
    ) {
        this.origin = connexio;
        this.cache = cache;
    }

    @Override
    public PreparedStatement prepared(final Query query) throws Exception {
        final PreparedStatement prepared;
        if (new IsSelect(query.sql().parsed()).value()) {
            prepared = new com.github.fabriciofx.cactoos.jdbc.prepared.Cached(
                this.origin.prepared(query),
                this.origin.prepared(
                    new QueryOf(
                        new NormalizedSql(query.sql().parsed()),
                        query.params()
                    )
                ),
                new NormalizedSql(query.sql().parsed()),
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
