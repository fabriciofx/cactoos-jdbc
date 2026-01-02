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
 * Long param.
 *
 * @since 0.2
 */
public final class LongOf implements Param {
    /**
     * Name.
     */
    private final String id;

    /**
     * Value.
     */
    private final Long num;

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public LongOf(final String name, final Long value) {
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
        return SqlLiteral.createExactNumeric(this.num.toString(), from);
    }
}
