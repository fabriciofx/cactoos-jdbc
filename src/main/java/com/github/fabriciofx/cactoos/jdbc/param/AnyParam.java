/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.param;

import com.github.fabriciofx.cactoos.jdbc.Param;
import java.sql.PreparedStatement;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParserPos;

/**
 * AnyParam.
 *
 * @since 0.8.1
 */
public final class AnyParam implements Param {
    /**
     * Name.
     */
    private final String id;

    /**
     * Value.
     */
    private final Object object;

    /**
     * Ctor.
     * @param name The id
     * @param value The data
     */
    public AnyParam(final String name, final Object value) {
        this.id = name;
        this.object = value;
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
        stmt.setObject(index, this.object);
    }

    @Override
    public SqlNode value(final SqlParserPos from) {
        return SqlLiteral.createCharString(this.object.toString(), from);
    }
}
