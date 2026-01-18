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
 *
 * @since 0.9.0
 */
public final class MaxSizePolicy implements Policy {
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
    public List<Entry> apply(final Map<Key, Entry> input) throws Exception {
        final List<Entry> removed = new LinkedList<>();
        while (input.size() > this.max) {
            removed.add(input.remove(input.keySet().iterator().next()));
        }
        return removed;
    }
}
