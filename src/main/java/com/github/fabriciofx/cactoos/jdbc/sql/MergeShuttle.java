/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql;

import com.github.fabriciofx.cactoos.jdbc.Param;
import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.query.Named;
import org.apache.calcite.sql.SqlDynamicParam;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.util.SqlShuttle;

/**
 * MergeShuttle.
 * A shuttle that change dynamic parameters for parameters values.
 * @since 0.9.0
 */
public final class MergeShuttle extends SqlShuttle {
    /**
     * Named query.
     */
    private final Named query;

    /**
     * Ctor.
     * @param query A Named query
     */
    public MergeShuttle(final Named query) {
        this.query = query;
    }

    @Override
    public SqlNode visit(final SqlDynamicParam mark) {
        final Params params = this.query.params().iterator().next();
        final Param param = params.param(mark.getIndex());
        return param.value(mark.getParserPosition());
    }
}
