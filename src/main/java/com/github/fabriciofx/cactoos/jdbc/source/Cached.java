/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.source;

import com.github.fabriciofx.cactoos.jdbc.Store;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.cache.store.TableStore;
import com.github.fabriciofx.cactoos.jdbc.hash.Murmur3Hash;
import org.cactoos.Func;

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
    private final Store<String, Table> cache;

    /**
     * Hash Function.
     */
    private final Func<Query, String> hash;

    /**
     * Ctor.
     * @param source The source
     */
    public Cached(final Source source) {
        this(source, new TableStore(), new Murmur3Hash());
    }

    /**
     * Ctor.
     * @param source The source
     * @param cache The cache
     */
    public Cached(
        final Source source,
        final Store<String, Table> cache
    ) {
        this(source, cache, new Murmur3Hash());
    }

    /**
     * Ctor.
     * @param source The source
     * @param cache The cache
     * @param hash The hash function
     */
    public Cached(
        final Source source,
        final Store<String, Table> cache,
        final Func<Query, String> hash
    ) {
        this.origin = source;
        this.cache = cache;
        this.hash = hash;
    }

    @Override
    public Session session() throws Exception {
        return new com.github.fabriciofx.cactoos.jdbc.session.Cached(
            this.origin.session(),
            this.cache,
            this.hash
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
