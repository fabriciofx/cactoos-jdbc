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
 * BoolParam.
 *
 * @since 0.2
 */
public final class BoolParam implements Param {
    /**
     * Name.
     */
    private final String id;

    /**
     * Value.
     */
    private final boolean bool;

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public BoolParam(final String name, final boolean value) {
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
    public SqlNode value(final SqlParserPos from) {
        return SqlLiteral.createBoolean(this.bool, from);
    }

    @Override
    public byte[] asBytes() throws Exception {
        final byte[] bytes = new byte[2];
        bytes[0] = (byte) Types.BOOLEAN;
        if (this.bool) {
            bytes[1] = (byte) 1;
        }
        return bytes;
    }
}
