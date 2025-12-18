/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import org.cactoos.Text;
import org.cactoos.text.Randomized;

/**
 * Random database name for tests.
 *
 * @since 0.2
 */
public final class RandomDatabaseName implements Text {
    /**
     * Database name.
     */
    private final Text name;

    /**
     * Ctor.
     */
    public RandomDatabaseName() {
        this(
            new Randomized(
                // @checkstyle MagicNumber (1 line)
                5,
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
            )
        );
    }

    /**
     * Ctor.
     * @param txt Database name
     */
    public RandomDatabaseName(final Text txt) {
        this.name = txt;
    }

    @Override
    public String asString() throws Exception {
        return this.name.asString();
    }
}
