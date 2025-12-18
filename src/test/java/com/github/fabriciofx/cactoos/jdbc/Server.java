/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import java.io.IOException;

/**
 * Server.
 *
 * <p>Represents a RDBMS server.</p>
 *
 * @since 0.2
 */
public interface Server extends AutoCloseable {
    /**
     * Start the server.
     * @throws Exception If fails
     */
    void start() throws Exception;

    /**
     * Stop the server.
     * @throws Exception If fails
     */
    void stop() throws Exception;

    /**
     * Return a Session from server.
     * @return A Session
     */
    Session session();

    @Override
    void close() throws IOException;
}
