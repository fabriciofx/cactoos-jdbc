/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache;

/**
 * Key.
 * @param <D> The domain type
 * @since 0.9.0
 */
public interface Key<D> {
    /**
     * Retrieve the domain associated with this key.
     * @return The domain
     */
    D domain();

    /**
     * Calculate the key's hash.
     * @return The key's hash
     */
    String hash();
}
