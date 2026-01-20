/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.cache.Store;
import com.github.fabriciofx.cactoos.cache.cache.CacheEnvelope;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Table;

/**
 * TableCache.
 * @since 0.9.0
 */
public final class TableCache extends CacheEnvelope<Query, Table> {
    /**
     * Ctor.
     */
    public TableCache() {
        this(new TableStore());
    }

    /**
     * Ctor.
     * @param store A store
     */
    public TableCache(final Store<Query, Table> store) {
        super(store);
    }
}
