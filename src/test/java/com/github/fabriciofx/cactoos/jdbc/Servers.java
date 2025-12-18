/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.cactoos.list.ListOf;

/**
 * Represents a set of RDBMS servers.
 *
 * @since 0.2
 */
@SuppressWarnings("PMD.AvoidCatchingGenericException")
public final class Servers implements Closeable {
    /**
     * All servers.
     */
    private final List<Server> all;

    /**
     * Ctor.
     * @param srvs Servers.
     */
    public Servers(final Server... srvs) {
        this.all = new ListOf<>(srvs);
    }

    /**
     * Get a list of valid Sessions.
     * @return A list of Sessions.
     * @throws Exception if fails
     */
    public Iterable<Session> sessions() throws Exception {
        final List<Session> sessions = new ArrayList<>(0);
        for (final Server server : this.all) {
            server.start();
            sessions.add(server.session());
        }
        return sessions;
    }

    @Override
    public void close() throws IOException {
        try {
            for (final Server server : this.all) {
                server.stop();
            }
            // @checkstyle IllegalCatchCheck (1 line)
        } catch (final Exception ex) {
            throw new IOException(ex);
        }
    }
}
