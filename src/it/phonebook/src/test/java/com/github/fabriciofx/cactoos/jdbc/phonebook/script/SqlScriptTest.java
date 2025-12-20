/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.script;

import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.script.SqlScript;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import com.github.fabriciofx.fake.server.db.server.MysqlServer;
import javax.sql.DataSource;
import org.cactoos.io.ResourceOf;
import org.junit.jupiter.api.Test;

/**
 * SqlScript tests.
 *
 * @since 0.2
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle MethodNameCheck (500 lines)
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class SqlScriptTest {
    @Test
    void h2Server() throws Exception {
        try (
            Server<DataSource> server = new H2Server(
                new SqlScript(
                    new ResourceOf(
                        "phonebook/phonebook-h2.sql"
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
            Server<DataSource> server = new MysqlServer(
                new SqlScript(
                    new ResourceOf(
                        "phonebook/phonebook-mysql.sql"
                    )
                )
            )
        ) {
            server.start();
        }
    }
}
