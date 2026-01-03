/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.connexio;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Query;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Transacted.
 * A decorator for Connexio which allows transactions. This decorator only
 * close the JDBC connecion after a commit or rollback.
 * @since 0.9.0
 */
public final class Transacted implements Connexio {
    /**
     * Connexio.
     */
    private final Connexio origin;

    /**
     * Close or not the connection.
     */
    private final AtomicBoolean closeable;

    /**
     * Ctor.
     * @param connexio A Connexio
     */
    public Transacted(final Connexio connexio) {
        this.origin = connexio;
        this.closeable = new AtomicBoolean(false);
    }

    @Override
    public PreparedStatement prepared(final Query query) throws Exception {
        return this.origin.prepared(query);
    }

    @Override
    public PreparedStatement batched(final Query query) throws Exception {
        return this.origin.batched(query);
    }

    @Override
    public PreparedStatement keyed(final Query query)
        throws Exception {
        return this.origin.keyed(query);
    }

    @Override
    public void autoCommit(final boolean enabled) throws Exception {
        this.origin.autoCommit(false);
    }

    @Override
    public void commit() throws Exception {
        this.origin.commit();
        this.closeable.set(true);
    }

    @Override
    public void rollback() throws Exception {
        this.origin.rollback();
        this.closeable.set(true);
    }

    @Override
    public void close() throws IOException {
        if (this.closeable.get()) {
            this.origin.close();
        }
    }
}
