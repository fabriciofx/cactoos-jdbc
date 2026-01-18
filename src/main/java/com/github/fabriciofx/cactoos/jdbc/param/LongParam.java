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
 * LongParam.
 *
 * @since 0.2
 */
public final class LongParam implements Param {
    /**
     * Name.
     */
    private final String id;

    /**
     * Value.
     */
    private final long num;

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public LongParam(final String name, final long value) {
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
        stmt.setLong(index, this.num);
    }

    @Override
    public SqlNode value(final SqlParserPos from) {
        return SqlLiteral.createExactNumeric(String.valueOf(this.num), from);
    }

    @Override
    public byte[] asBytes() throws Exception {
        return new byte[] {
            (byte) Types.BIGINT,
            (byte) this.num,
            (byte) (this.num >>> 8),
            (byte) (this.num >>> 16),
            (byte) (this.num >>> 24),
            (byte) (this.num >>> 32),
            (byte) (this.num >>> 40),
            (byte) (this.num >>> 48),
            (byte) (this.num >>> 56),
        };
    }
}
