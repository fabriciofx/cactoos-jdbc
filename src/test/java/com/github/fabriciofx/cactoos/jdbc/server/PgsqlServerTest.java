/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.server;

import com.github.fabriciofx.cactoos.jdbc.Server;
import com.github.fabriciofx.cactoos.jdbc.script.ScriptOf;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.Joined;
import org.junit.jupiter.api.Test;

/**
 * PostgreSQL Server test.
 *
 * @since 0.2
 * @checkstyle JavadocMethodCheck (500 lines)
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class PgsqlServerTest {
    @Test
    void startAndStop() throws Exception {
        try (
            Server pgsql = new PgsqlServer(
                new ScriptOf(
                    new ResourceOf(
                        new Joined(
                            "/",
                            "com/github/fabriciofx/cactoos/jdbc/phonebook",
                            "phonebook-pgsql.sql"
                        )
                    )
                )
            )
        ) {
            pgsql.start();
        }
    }
}
