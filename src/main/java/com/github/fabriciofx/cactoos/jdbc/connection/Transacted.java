/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Transacted connection.
 * A decorator for {@link java.sql.Connection} that once created, only can be
 * closed after a commit() or rollback().
 * @since 0.9.0
 */
public final class Transacted extends ConnectionEnvelope {
    /**
     * Determines whether a connection can be closed or not.
     */
    private final AtomicBoolean closable;

    /**
     * Ctor.
     *
     * @param connection Original connection to be decorated
     */
    public Transacted(final Connection connection) {
        super(connection);
        this.closable = new AtomicBoolean(false);
    }

    @Override
    public void commit() throws SQLException {
        super.commit();
        this.closable.set(true);
    }

    @Override
    public void rollback() throws SQLException {
        super.rollback();
        this.closable.set(true);
    }

    @Override
    public void close() throws SQLException {
        if (this.closable.get()) {
            super.close();
        }
    }
}
