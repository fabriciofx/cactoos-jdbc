/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.server;

import com.github.fabriciofx.cactoos.jdbc.ServerEnvelope;
import com.github.fabriciofx.cactoos.jdbc.ServerInContainer;
import com.github.fabriciofx.cactoos.jdbc.script.EmptyScript;
import com.github.fabriciofx.cactoos.jdbc.script.ScriptOf;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * PostgreSQL server, for unit testing.
 *
 * @since 0.2
 */
public final class PgsqlServer extends ServerEnvelope {

    /**
     * Ctor.
     */
    public PgsqlServer() {
        this(new EmptyScript());
    }

    /**
     * Ctor.
     * @param text SQL Script to initialize the database
     */
    public PgsqlServer(final ScriptOf text) {
        super(
            new ServerInContainer(
                new PostgreSQLContainer<>("postgres:latest"),
                text
            )
        );
    }
}
