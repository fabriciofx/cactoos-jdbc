/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.prepared;

import java.sql.PreparedStatement;

/**
 * IndexedPrepared.
 *
 * <p>A PreparedStatement that accepts SQL with column indexes, which
 * compliant JDBC drivers always refuse.</p>
 *
 * @since 0.9.0
 */
final class IndexedPrepared extends PreparedEnvelope {

    /**
     * Amount of rows affected.
     */
    private final int count;

    /**
     * Ctor.
     *
     * @param prepared Decorated PreparedStatement
     * @param count Amount of rows affected
     */
    IndexedPrepared(final PreparedStatement prepared, final int count) {
        super(prepared);
        this.count = count;
    }

    @Override
    public int executeUpdate(final String sql, final int[] columns) {
        return this.count;
    }

    @Override
    public boolean execute(final String sql, final int[] columns) {
        return this.count > 0;
    }
}
