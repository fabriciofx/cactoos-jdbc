/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.Types;

/**
 * DecimalParam.
 *
 * @since 0.2
 */
public final class DecimalParam implements Param {
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
    public DecimalParam(final String name, final String value) {
        this(name, new BigDecimal(value));
    }

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public DecimalParam(final String name, final BigDecimal value) {
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
    public byte[] asBytes() throws Exception {
        final BigInteger unscaled = this.decimal.unscaledValue();
        final byte[] magnitude = unscaled.toByteArray();
        final byte[] result = new byte[1 + Integer.BYTES + magnitude.length];
        final ByteBuffer buffer = ByteBuffer.wrap(result);
        buffer.put((byte) Types.DECIMAL);
        buffer.putInt(this.decimal.scale());
        buffer.put(magnitude);
        return result;
    }
}
