/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.source;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.cache.CachedRowSetCache;
import javax.sql.rowset.CachedRowSet;

/**
 * Cached.
 * A {@link Source} decorator to cache query data.
 * @since 0.9.0
 */
public final class Cached implements Source {
    /**
     * Source.
     */
    private final Source origin;

    /**
     * Cache.
     */
    private final Cache<String, CachedRowSet> cache;

    /**
     * Ctor.
     * @param source The source
     */
    public Cached(final Source source) {
        this(source, new CachedRowSetCache());
    }

    /**
     * Ctor.
     * @param source The source
     * @param cache The cache
     */
    public Cached(
        final Source source,
        final Cache<String, CachedRowSet> cache
    ) {
        this.origin = source;
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
