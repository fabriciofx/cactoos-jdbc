/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.cache.hash.Murmur3Hash;
import com.github.fabriciofx.cactoos.jdbc.sql.BytesQuery;
import org.cactoos.Text;
import org.cactoos.text.HexOf;
import org.cactoos.text.Sticky;
import org.cactoos.text.UncheckedText;

/**
 * CacheKey.
 * @since 0.9.0
 */
public final class CacheKey implements Key<Query> {
    /**
     * Query.
     */
    private final Query qry;

    /**
     * Hash.
     */
    private final UncheckedText hsh;

    /**
     * Ctor.
     * @param query The query
     */
    public CacheKey(final Query query) {
        this(
            query,
            new Sticky(new HexOf(new Murmur3Hash(new BytesQuery(query))))
        );
    }

    /**
     * Ctor.
     * @param query The query
     * @param hash The query's hash
     */
    public CacheKey(final Query query, final Text hash) {
        this(query, new UncheckedText(hash));
    }

    /**
     * Ctor.
     * @param query The query
     * @param hash The query's hash
     */
    public CacheKey(final Query query, final UncheckedText hash) {
        this.qry = query;
        this.hsh = hash;
    }

    @Override
    public Query domain() {
        return this.qry;
    }

    @Override
    public String hash() {
        return this.hsh.asString();
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof CacheKey
            && this.hash().equals(CacheKey.class.cast(other).hash());
    }

    @Override
    public int hashCode() {
        return this.hash().hashCode();
    }
}
