/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.url;

import java.sql.Connection;
import java.util.Objects;
import javax.sql.DataSource;
import org.cactoos.Text;
import org.cactoos.text.Sticky;

/**
 * JdbcUrl.
 * Get a JDBC URL from a DataSource
 * @since 0.9.0
 */
public final class JdbcUrl implements Text {
    /**
     * DataSource.
     */
    private final Text text;

    /**
     * Ctor.
     * @param source A DataSource
     */
    public JdbcUrl(final DataSource source) {
        this.text = new Sticky(
            () -> {
                try (Connection connection = source.getConnection()) {
                    return Objects.requireNonNullElse(
                        connection.getMetaData().getURL(),
                        ""
                    );
                }
            }
        );
    }

    @Override
    public String asString() throws Exception {
        return this.text.asString();
    }
}
