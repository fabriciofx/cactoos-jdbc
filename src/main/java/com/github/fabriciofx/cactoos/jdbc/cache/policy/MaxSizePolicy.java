/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.policy;

import com.github.fabriciofx.cactoos.jdbc.cache.Entry;
import com.github.fabriciofx.cactoos.jdbc.cache.Key;
import com.github.fabriciofx.cactoos.jdbc.cache.Policy;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * MaxSizePolicy.
 * @param <D> The domain key type
 * @param <V> The value type stored
 * @since 0.9.0
 */
public final class MaxSizePolicy<D, V> implements Policy<D, V> {
    /**
     * Max size.
     */
    private final int max;

    /**
     * Ctor.
     */
    public MaxSizePolicy() {
        this(Integer.MAX_VALUE);
    }

    /**
     * Ctor.
     *
     * @param max Maximum size
     */
    public MaxSizePolicy(final int max) {
        this.max = max;
    }

    @Override
    public List<Entry<D, V>> apply(final Map<Key<D>, Entry<D, V>> input)
        throws Exception {
        final List<Entry<D, V>> removed = new LinkedList<>();
        while (input.size() > this.max) {
            removed.add(input.remove(input.keySet().iterator().next()));
        }
        return removed;
    }
}
