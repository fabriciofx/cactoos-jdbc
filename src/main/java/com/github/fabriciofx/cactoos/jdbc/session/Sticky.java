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
     * Session.
     */
    private final Session origin;

    /**
     * The Connection.
     */
    private final Scalar<Connection> connexio;

    /**
     * Ctor.
     * @param session Session
     */
    public Sticky(final Session session) {
        this.origin = session;
        this.connexio = new org.cactoos.scalar.Sticky<>(session::connection);
    }

    @Override
    public Connection connection() throws Exception {
        return this.connexio.value();
    }

    @Override
    public String url() {
        return this.origin.url();
    }

    @Override
    public String username() {
        return this.origin.username();
    }

    @Override
    public String password() {
        return this.origin.password();
    }
}
