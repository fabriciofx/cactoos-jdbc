/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.cache.policy;

import com.github.fabriciofx.cactoos.jdbc.Table;
import com.github.fabriciofx.cactoos.jdbc.cache.Policy;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * MaxSizePolicy.
 * @since 0.9.0
 */
public final class MaxSizePolicy implements Policy<Map<String, Table>, Table> {
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
     * @param max Maximum size
     */
    public MaxSizePolicy(final int max) {
        this.max = max;
    }

    @Override
    public List<Table> apply(final Map<String, Table> results) {
        final List<Table> removed = new LinkedList<>();
        while (results.size() > this.max) {
            removed.add(results.remove(results.keySet().iterator().next()));
        }
        return removed;
    }
}
