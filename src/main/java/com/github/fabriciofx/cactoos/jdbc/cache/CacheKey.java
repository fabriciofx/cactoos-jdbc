/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.cache.Key;
import com.github.fabriciofx.cactoos.cache.key.KeyOf;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.bytes.QueryAsBytes;

/**
 * CacheKey.
 * @since 0.9.0
 */
public final class CacheKey implements Key<Query> {
    /**
     * Key.
     */
    private final Key<Query> key;

    /**
     * Ctor.
     * @param query A query
     */
    public CacheKey(final Query query) {
        this(new KeyOf<>(query, new QueryAsBytes(query)));
    }

    /**
     * Ctor.
     * @param key A key
     */
    public CacheKey(final Key<Query> key) {
        this.key = key;
    }

    @Override
    public Query domain() {
        return this.key.domain();
    }

    @Override
    public String hash() {
        return this.key.hash();
    }

    @Override
    public boolean equals(final Object other) {
        return this.key.equals(other);
    }

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }
}
