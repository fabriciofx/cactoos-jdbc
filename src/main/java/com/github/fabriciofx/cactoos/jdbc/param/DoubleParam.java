/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.sql.PreparedStatement;
import java.sql.Types;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParserPos;

/**
 * DoubleParam.
 *
 * @since 0.2
 */
public final class DoubleParam implements Param {
    /**
     * Name.
     */
    private final String id;

    /**
     * Value.
     */
    private final double num;

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public DoubleParam(final String name, final double value) {
        this.id = name;
        this.num = value;
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
        stmt.setDouble(index, this.num);
    }

    @Override
    public SqlNode value(final SqlParserPos from) {
        return SqlLiteral.createExactNumeric(String.valueOf(this.num), from);
    }

    @Override
    public byte[] asBytes() throws Exception {
        final long bits = Double.doubleToLongBits(this.num);
        return new byte[] {
            (byte) Types.DOUBLE,
            (byte) bits,
            (byte) (bits >>> 8),
            (byte) (bits >>> 16),
            (byte) (bits >>> 24),
            (byte) (bits >>> 32),
            (byte) (bits >>> 40),
            (byte) (bits >>> 48),
            (byte) (bits >>> 56),
        };
    }
}
