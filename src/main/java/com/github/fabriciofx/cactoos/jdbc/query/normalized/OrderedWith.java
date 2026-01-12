/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.normalized;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlWith;
import org.cactoos.Scalar;

/**
 * OrderedWith.
 * @since 0.9.0
 */
public final class OrderedWith implements Scalar<SqlNode> {
    /**
     * Shuttle.
     */
    private final Shuttle shuttle;

    /**
     * With.
     */
    private final SqlWith with;

    /**
     * Ctor.
     * @param shuttle A Shuttle
     * @param with A SqlWith
     */
    public OrderedWith(final Shuttle shuttle, final SqlWith with) {
        this.shuttle = shuttle;
        this.with = with;
    }

    @Override
    public SqlNode value() throws Exception {
        final SqlNodeList withs = new SortedWith(
            this.shuttle,
            this.with.withList
        ).value();
        final SqlNode body = this.with.body.accept(this.shuttle);
        return new SqlWith(
            this.with.getParserPosition(),
            withs,
            body
        );
    }
}
