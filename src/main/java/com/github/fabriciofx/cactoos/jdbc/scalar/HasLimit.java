/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.scalar;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOrderBy;
import org.cactoos.Scalar;

/**
 * HasLimit.
 *
 * <p>Tells whether a parsed query already restricts its rows through LIMIT,
 * OFFSET or FETCH, which Calcite keeps in a {@link SqlOrderBy}.</p>
 *
 * @since 0.9.0
 */
public final class HasLimit implements Scalar<Boolean> {

    /**
     * Parsed query.
     */
    private final SqlNode node;

    /**
     * Ctor.
     *
     * @param node A parsed query
     */
    public HasLimit(final SqlNode node) {
        this.node = node;
    }

    @Override
    public Boolean value() {
        return this.node instanceof SqlOrderBy order
            && (order.fetch != null || order.offset != null);
    }
}
