/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.math.BigDecimal;
import java.sql.PreparedStatement;

/**
 * Decimal param.
 *
 * @since 0.2
 */
public final class DecimalOf implements Param {
    /**
     * Name.
     */
    private final String id;

    /**
     * Value.
     */
    private final BigDecimal decimal;

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public DecimalOf(final String name, final String value) {
        this(name, new BigDecimal(value));
    }

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public DecimalOf(final String name, final BigDecimal value) {
        this.id = name;
        this.decimal = value;
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
        stmt.setBigDecimal(index, this.decimal);
    }

    @Override
    public String asString() throws Exception {
        return this.decimal.toString();
    }
}
