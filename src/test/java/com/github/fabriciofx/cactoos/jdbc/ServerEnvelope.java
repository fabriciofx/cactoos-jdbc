/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import java.io.IOException;

/**
 * Enveloper for {@link Server}.
 *
 * @since 0.2
 */
public abstract class ServerEnvelope implements Server {
    /**
     * Original server.
     */
    private final Server server;

    /**
     * Ctor.
     * @param server Original server.
     */
    protected ServerEnvelope(final Server server) {
        this.server = server;
    }

    @Override
    public final void start() throws Exception {
        this.server.start();
    }

    @Override
    public final void stop() throws Exception {
        this.server.stop();
    }

    @Override
    public final Session session() {
        return this.server.session();
    }

    @Override
    public final void close() throws IOException {
        this.server.close();
    }
}
