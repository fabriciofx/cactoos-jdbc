/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.sql.PreparedStatement;

/**
 * Boolean param.
 *
 * @since 0.2
 */
public final class BoolOf implements Param {
    /**
     * Name.
     */
    private final String id;

    /**
     * Value.
     */
    private final Boolean bool;

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public BoolOf(final String name, final Boolean value) {
        this.id = name;
        this.bool = value;
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
        stmt.setBoolean(index, this.bool);
    }

    @Override
    public String asString() throws Exception {
        return this.bool.toString();
    }
}
