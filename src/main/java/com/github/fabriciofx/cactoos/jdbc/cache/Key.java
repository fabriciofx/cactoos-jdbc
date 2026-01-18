/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

import com.github.fabriciofx.cactoos.jdbc.Query;

/**
 * Key.
 * @since 0.9.0
 */
public interface Key {
    /**
     * Retrieve the query associated with this key.
     * @return The query
     */
    Query query();

    /**
     * Calculate the key's hash.
     * @return The key's hash
     */
    String hash();
}
