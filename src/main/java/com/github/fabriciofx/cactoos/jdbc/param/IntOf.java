/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.sql.PreparedStatement;

/**
 * Integer param.
 *
 * @since 0.2
 */
public final class IntOf implements Param {
    /**
     * Name.
     */
    private final String id;

    /**
     * Value.
     */
    private final Integer integer;

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public IntOf(final String name, final Integer value) {
        this.id = name;
        this.integer = value;
    }

    @Override
    public String name() {
        return this.id;
    }

    @Override
    public void prepare(
        final PreparedStatement stmt,
        final int index
    ) throws Exception {
        stmt.setInt(index, this.integer);
    }

    @Override
    public String asString() throws Exception {
        return this.integer.toString();
    }
}
