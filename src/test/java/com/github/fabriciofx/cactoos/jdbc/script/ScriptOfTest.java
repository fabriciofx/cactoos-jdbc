/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.script;

import com.github.fabriciofx.cactoos.jdbc.Server;
import com.github.fabriciofx.cactoos.jdbc.server.H2Server;
import com.github.fabriciofx.cactoos.jdbc.server.MysqlServer;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.Joined;
import org.junit.jupiter.api.Test;

/**
 * SqlScript tests.
 *
 * @since 0.2
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle MethodNameCheck (500 lines)
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class ScriptOfTest {
    @Test
    void h2Server() throws Exception {
        try (
            Server server = new H2Server(
                new ScriptOf(
                    new ResourceOf(
                        new Joined(
                            "/",
                            "com/github/fabriciofx/cactoos/jdbc/phonebook",
                            "phonebook-h2.sql"
                        )
                    )
                )
            )
        ) {
            server.start();
        }
    }

    @Test
    void mysqlServer() throws Exception {
        try (
            Server server = new MysqlServer(
                new ScriptOf(
                    new ResourceOf(
                        new Joined(
                            "/",
                            "com/github/fabriciofx/cactoos/jdbc/phonebook",
                            "phonebook-mysql.sql"
                        )
                    )
                )
            )
        ) {
            server.start();
        }
    }
}
