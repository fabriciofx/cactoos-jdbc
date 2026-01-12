/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.parametrized;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.calcite.sql.SqlDynamicParam;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.util.SqlShuttle;

/**
 * ParameterShuttle.
 * A shuttle that change literals for question marks (dynamic parameters).
 * @since 0.9.0
 */
public final class Shuttle extends SqlShuttle {
    /**
     * Index.
     */
    private final AtomicInteger index;

    /**
     * Ctor.
     */
    public Shuttle() {
        this.index = new AtomicInteger(0);
    }

    @Override
    public SqlNode visit(final SqlLiteral literal) {
        return new SqlDynamicParam(
            this.index.getAndIncrement(),
            SqlParserPos.ZERO
        );
    }
}
