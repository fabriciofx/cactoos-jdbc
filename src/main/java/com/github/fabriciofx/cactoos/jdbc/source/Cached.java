/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.source;

import com.github.fabriciofx.cactoos.jdbc.Cache;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.cache.TableCache;

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
    private final Cache cache;

    /**
     * Ctor.
     * @param source The source
     */
    public Cached(final Source source) {
        this(source, new TableCache());
    }

    /**
     * Ctor.
     * @param source The source
     * @param cache The cache
     */
    public Cached(
        final Source source,
        final Cache cache
    ) {
        this.origin = source;
        this.cache = cache;
    }

    @Override
    public Session session() throws Exception {
        return new com.github.fabriciofx.cactoos.jdbc.session.Cached(
            this.origin.session(),
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
