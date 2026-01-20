/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.cache.key.KeyEnvelope;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.sql.BytesQuery;

/**
 * CacheKey.
 * @since 0.9.0
 */
public final class CacheKey extends KeyEnvelope<Query> {
    /**
     * Ctor.
     * @param query A query
     */
    public CacheKey(final Query query) {
        super(query, new BytesQuery(query));
    }
}
