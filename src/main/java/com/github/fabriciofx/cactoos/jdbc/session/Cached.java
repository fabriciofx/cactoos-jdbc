/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.cache.Cache;
import com.github.fabriciofx.cactoos.cache.invalidate.MetadataInvalidate;
import com.github.fabriciofx.cactoos.jdbc.Plan;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.plan.Simple;
import com.github.fabriciofx.cactoos.jdbc.query.Normalized;
import com.github.fabriciofx.cactoos.jdbc.scalar.KindOfQuery;
import com.github.fabriciofx.cactoos.jdbc.scalar.TableNames;
import java.io.IOException;
import java.sql.PreparedStatement;

/**
 * Cached.
 * <p>
 * A decorator for Session that allows caching results.
 *
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
    private final Cache<Query, Table> cache;

    /**
     * Ctor.
     *
     * @param session A session
     * @param cache The cache
     */
    public Cached(
        final Session session,
        final Cache<Query, Table> cache
    ) {
        this.origin = session;
        this.cache = cache;
    }

    @Override
    public PreparedStatement prepared(final Plan plan) throws Exception {
        return switch (new KindOfQuery(plan.query()).value()) {
            case SELECT, WITH, ORDER_BY -> {
                final Normalized normalized = new Normalized(plan.query());
                yield new com.github.fabriciofx.cactoos.jdbc.prepared.Cached(
                    this.origin.prepared(plan),
                    this.origin.prepared(new Simple(normalized)),
                    normalized,
                    this.cache
                );
            }
            case INSERT, UPDATE, DELETE -> {
                this.cache.store().entries().invalidate(
                    new MetadataInvalidate<>(
                        new TableNames(plan.query()).value()
                    )
                );
                yield this.origin.prepared(plan);
            }
            default -> this.origin.prepared(plan);
        };
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
