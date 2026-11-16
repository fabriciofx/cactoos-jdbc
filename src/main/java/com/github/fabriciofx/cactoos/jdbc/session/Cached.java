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
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.cactoos.set.SetOf;

/**
 * Cached.
 *
 * <p>A decorator for Session that allows caching results.</p>
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
     * Names of the tables changed by the open transaction.
     */
    private final Set<String> tables;

    /**
     * Ctor.
     *
     * @param session A session
     * @param cache The cache
     */
    public Cached(final Session session, final Cache<Query, Table> cache) {
        this(session, cache, new CopyOnWriteArraySet<>());
    }

    /**
     * Ctor.
     *
     * @param session A session
     * @param cache The cache
     * @param tables Names of the tables changed by the open transaction
     */
    public Cached(
        final Session session,
        final Cache<Query, Table> cache,
        final Set<String> tables
    ) {
        this.origin = session;
        this.cache = cache;
        this.tables = tables;
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
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
            case INSERT, UPDATE, DELETE, MERGE -> {
                final Set<String> names = new TableNames(plan.query()).value();
                this.cache.store().entries().invalidate(
                    new MetadataInvalidate<>(names)
                );
                final PreparedStatement stmt = this.origin.prepared(plan);
                if (!stmt.getConnection().getAutoCommit()) {
                    this.tables.addAll(names);
                }
                yield stmt;
            }
            case TRUNCATE_TABLE, DROP_TABLE, ALTER_TABLE -> {
                this.cache.clear();
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
        this.cache.store().entries().invalidate(
            new MetadataInvalidate<>(new SetOf<>(this.tables))
        );
        this.tables.clear();
    }

    @Override
    public void rollback() throws Exception {
        this.origin.rollback();
        this.tables.clear();
    }

    @Override
    public void close() throws IOException {
        this.origin.close();
    }
}
