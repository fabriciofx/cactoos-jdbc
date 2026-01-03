/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.cache.CachedRowSetCache;
import javax.sql.rowset.CachedRowSet;

/**
 * Cached.
 * A {@link Session} decorator to cache query data.
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
     * @param session The session
     */
    public Cached(final Session session) {
        this(session, new CachedRowSetCache());
    }

    /**
     * Ctor.
     * @param session The session
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
    public Connexio connexio() throws Exception {
        return new com.github.fabriciofx.cactoos.jdbc.connexio.Cached(
            this.origin.connexio(),
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
