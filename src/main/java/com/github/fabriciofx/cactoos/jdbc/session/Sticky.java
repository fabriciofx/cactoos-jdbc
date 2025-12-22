/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import java.sql.Connection;
import org.cactoos.Scalar;

/**
 * Session that holds always the same Connection.
 *
 * @since 0.4
 */
public final class Sticky implements Session {
    /**
     * The Connection.
     */
    private final Scalar<Connection> connexio;

    /**
     * Ctor.
     * @param session Session
     */
    public Sticky(final Session session) {
        this.connexio = new org.cactoos.scalar.Sticky<>(session::connection);
    }

    @Override
    public Connection connection() throws Exception {
        return this.connexio.value();
    }
}
