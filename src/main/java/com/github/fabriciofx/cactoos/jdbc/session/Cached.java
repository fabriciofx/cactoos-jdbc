/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Plan;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.plan.Simple;
import com.github.fabriciofx.cactoos.jdbc.query.Merged;
import com.github.fabriciofx.cactoos.jdbc.query.Normalized;
import com.github.fabriciofx.cactoos.jdbc.query.Symmetric;
import com.github.fabriciofx.cactoos.jdbc.sql.StatementKind;
import java.io.IOException;
import java.sql.PreparedStatement;
import javax.sql.rowset.CachedRowSet;

/**
 * Cached. A decorator for Session that allows caching results.
 *
 * @since 0.9.0
 */
@SuppressWarnings("PMD.CloseResource")
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
     *
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
    public PreparedStatement prepared(final Plan plan) throws Exception {
        final PreparedStatement prepared;
        switch (new StatementKind(plan.query().sql()).value()) {
            case SELECT:
                prepared =
                    new com.github.fabriciofx.cactoos.jdbc.prepared.Cached(
                        this.origin.prepared(plan),
                        this.origin.prepared(
                            new Simple(
                                new Normalized(plan.query())
                            )
                        ),
                        new Normalized(new Merged(plan.query())),
                        this.cache
                    );
                break;
            case DELETE:
                prepared = this.origin.prepared(plan);
                final CachedRowSet cached = this.cache.delete(
                    new Symmetric(new Merged(plan.query())).sql()
                );
                cached.close();
                break;
            default:
                prepared = this.origin.prepared(plan);
                break;
        }
        return prepared;
    }

    @Override
    public void autocommit(final boolean enabled) throws Exception {
        this.origin.autocommit(enabled);
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
