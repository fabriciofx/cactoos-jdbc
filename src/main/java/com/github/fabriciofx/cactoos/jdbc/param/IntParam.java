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
 * IntParam.
 *
 * @since 0.2
 */
public final class IntParam implements Param {
    /**
     * Name.
     */
    private final String id;

    /**
     * Value.
     */
    private final int integer;

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public IntParam(final String name, final int value) {
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
    public SqlNode value(final SqlParserPos from) {
        return SqlLiteral.createExactNumeric(
            String.valueOf(this.integer),
            from
        );
    }

    @Override
    public byte[] asBytes() throws Exception {
        return new byte[] {
            (byte) Types.INTEGER,
            (byte) this.integer,
            (byte) (this.integer >>> 8),
            (byte) (this.integer >>> 16),
            (byte) (this.integer >>> 24),
        };
    }
}
