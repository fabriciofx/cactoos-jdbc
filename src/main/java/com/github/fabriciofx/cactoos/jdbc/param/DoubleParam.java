/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.sql.PreparedStatement;
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
    private final Double num;

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public DoubleParam(final String name, final Double value) {
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
        return SqlLiteral.createExactNumeric(this.num.toString(), from);
    }
}
