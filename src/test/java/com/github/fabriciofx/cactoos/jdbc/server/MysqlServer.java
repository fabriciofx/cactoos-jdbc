/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.server;

import com.github.fabriciofx.cactoos.jdbc.ServerEnvelope;
import com.github.fabriciofx.cactoos.jdbc.ServerInContainer;
import com.github.fabriciofx.cactoos.jdbc.script.EmptyScript;
import com.github.fabriciofx.cactoos.jdbc.script.ScriptOf;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * MySQL server, for unit testing.
 *
 * @since 0.2
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class MysqlServer extends ServerEnvelope {
    /**
     * Ctor.
     */
    public MysqlServer() {
        this(new EmptyScript());
    }

    /**
     * Ctor.
     * @param text SQL Script to initialize the database
     */
    public MysqlServer(final ScriptOf text) {
        super(
            new ServerInContainer(
                new MySQLContainer<>(
                    DockerImageName
                        .parse("mysql/mysql-server:latest")
                        .asCompatibleSubstituteFor("mysql")
                ),
                text
            )
        );
    }

}
