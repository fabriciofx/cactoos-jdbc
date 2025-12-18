/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc;

import com.github.fabriciofx.cactoos.jdbc.script.ScriptOf;
import com.github.fabriciofx.cactoos.jdbc.session.Driver;
import java.io.IOException;
import org.testcontainers.containers.JdbcDatabaseContainer;

/**
 * Server inside container for integration testing.
 *
 * @since 0.2
 */
public final class ServerInContainer implements Server {
    /**
     * The container.
     */
    private final JdbcDatabaseContainer<?> container;

    /**
     * SQL Script to initialize the database.
     */
    private final ScriptOf script;

    /**
     * Ctor.
     * @param container The container.
     * @param script Initialization script.
     */
    public ServerInContainer(
        final JdbcDatabaseContainer<?> container,
        final ScriptOf script
    ) {
        this.container = container;
        this.script = script;
    }

    @Override
    public void start() throws Exception {
        this.container.start();
        this.script.run(this.session());
    }

    @Override
    public void stop() throws Exception {
        this.container.stop();
    }

    @Override
    public Session session() {
        return new Driver(
            this.container.getJdbcUrl(),
            this.container.getUsername(),
            this.container.getPassword()
        );
    }

    @Override
    public void close() throws IOException {
        this.container.close();
    }
}
