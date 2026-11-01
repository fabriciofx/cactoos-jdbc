/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.text;

import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;
import org.cactoos.Scalar;
import org.cactoos.Text;
import org.cactoos.text.Replaced;
import org.cactoos.text.Sticky;
import org.cactoos.text.TextOf;
import org.cactoos.text.Trimmed;

/**
 * Pretty. Print a pretty SQL from a {@link SqlNode}.
 *
 * @since 0.9.0
 */
public final class Pretty implements Text {

    /**
     * SQL.
     */
    private final Text sql;

    /**
     * Ctor.
     *
     * @param node A {@link SqlNode}
     */
    public Pretty(final SqlNode node) {
        this(() -> node);
    }

    /**
     * Ctor.
     *
     * @param node A Scalar of {@link SqlNode}
     */
    public Pretty(final Scalar<SqlNode> node) {
        this.sql = new Sticky(
            () -> new Trimmed(
                new Replaced(
                    new TextOf(
                        new SqlPrettyWriter(
                            SqlPrettyWriter.config()
                                .withIndentation(0)
                                .withSelectListItemsOnSeparateLines(false)
                                .withClauseStartsLine(false).withDialect(
                                    SqlDialect.DatabaseProduct.UNKNOWN
                                        .getDialect()
                                )
                        ).format(node.value())
                    ),
                    "\\s+",
                    " "
                )
            ).asString()
        );
    }

    @Override
    public String asString() throws Exception {
        return this.sql.asString();
    }
}
