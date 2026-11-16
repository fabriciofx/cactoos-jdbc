/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.scalar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.cactoos.Scalar;

/**
 * HasViews.
 *
 * <p>Tells whether any of the table names read by a query is a database
 * view, according to the connection metadata.</p>
 *
 * @since 0.9.0
 */
public final class HasViews implements Scalar<Boolean> {

    /**
     * Connection.
     */
    private final Connection connection;

    /**
     * Table names.
     */
    private final Scalar<Set<String>> names;

    /**
     * Ctor.
     *
     * @param connection The connection
     * @param names Table names
     */
    public HasViews(
        final Connection connection,
        final Scalar<Set<String>> names
    ) {
        this.connection = connection;
        this.names = names;
    }

    @Override
    public Boolean value() throws Exception {
        final Set<String> tables = this.names.value().stream()
            .map(name -> name.toLowerCase(Locale.ROOT))
            .collect(Collectors.toSet());
        boolean found = false;
        try (
            ResultSet rset = this.connection.getMetaData().getTables(
                this.connection.getCatalog(),
                "%",
                "%",
                new String[]{"VIEW"}
            )
        ) {
            while (!found && rset.next()) {
                final String view = rset.getString("TABLE_NAME")
                    .toLowerCase(Locale.ROOT);
                found = tables.contains(view) || tables.contains(
                    String.join(
                        ".",
                        Objects.requireNonNullElse(
                            rset.getString("TABLE_SCHEM"),
                            ""
                        ).toLowerCase(Locale.ROOT),
                        view
                    )
                );
            }
        }
        return found;
    }
}
