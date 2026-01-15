/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Plan;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.plan.Simple;
import com.github.fabriciofx.cactoos.jdbc.query.Merged;
import com.github.fabriciofx.cactoos.jdbc.query.Normalized;
import com.github.fabriciofx.cactoos.jdbc.sql.QueryKind;
import java.io.IOException;
import java.sql.PreparedStatement;
import org.cactoos.Func;

/**
 * Cached.
 * <p>
 * A decorator for Session that allows caching results.
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
    private final Cache<String, Table> cache;

    /**
     * Hash function.
     */
    private final Func<Query, String> hash;

    /**
     * Ctor.
     *
     * @param session A session
     * @param cache The cache
     * @param hash The hash function
     */
    public Cached(
        final Session session,
        final Cache<String, Table> cache,
        final Func<Query, String> hash
    ) {
        this.origin = session;
        this.cache = cache;
        this.hash = hash;
    }

    @Override
    public PreparedStatement prepared(final Plan plan) throws Exception {
        final PreparedStatement prepared;
        switch (new QueryKind(plan.query()).value()) {
            case SELECT:
            case WITH:
            case ORDER_BY:
                prepared =
                    new com.github.fabriciofx.cactoos.jdbc.prepared.Cached(
                        this.origin.prepared(plan),
                        this.origin.prepared(
                            new Simple(new Normalized(plan.query()))
                        ),
                        new Normalized(new Merged(plan.query())),
                        this.cache,
                        this.hash
                    );
                break;
            case DELETE:
                prepared = this.origin.prepared(plan);
                this.cache.store().clear();
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
