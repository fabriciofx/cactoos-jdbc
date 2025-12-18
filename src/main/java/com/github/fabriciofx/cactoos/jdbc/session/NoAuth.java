/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import java.sql.Connection;
import javax.sql.DataSource;

/**
 * No authenticated session.
 *
 * @since 0.1
 */
public final class NoAuth implements Session {
    /**
     * DataSource.
     */
    private final DataSource source;

    /**
     * Ctor.
     * @param source DataSource
     */
    public NoAuth(final DataSource source) {
        this.source = source;
    }

    @Override
    public Connection connection() throws Exception {
        return this.source.getConnection();
    }
}
