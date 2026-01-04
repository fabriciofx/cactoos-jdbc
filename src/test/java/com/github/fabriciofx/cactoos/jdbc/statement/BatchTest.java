/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import com.github.fabriciofx.cactoos.jdbc.query.Named;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.source.NoAuth;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;

/**
 * Batch tests.
 * @since 0.9.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class BatchTest {
    @Test
    void batch() throws Exception {
        try (Server<DataSource> server = new H2Server()) {
            server.start();
            try (Session session = new NoAuth(server.resource()).session()) {
                new Update(
                    session,
                    new QueryOf(
                        "CREATE TABLE client (id INT, name VARCHAR(50), age INT, PRIMARY KEY (id))"
                    )
                ).execute();
                new Batch(
                    session,
                    new Named(
                        new QueryOf(
                            "INSERT INTO client (id, name, age) VALUES (:id, :name, :age)",
                            new ParamsOf(
                                new IntParam("id", 1),
                                new TextParam("name", "Jeff Bridges"),
                                // @checkstyle MagicNumber (1 line)
                                new IntParam("age", 34)
                            ),
                            new ParamsOf(
                                new IntParam("id", 2),
                                new TextParam("name", "Anna Miller"),
                                // @checkstyle MagicNumber (1 line)
                                new IntParam("age", 26)
                            ),
                            new ParamsOf(
                                // @checkstyle MagicNumber (3 lines)
                                new IntParam("id", 3),
                                new TextParam("name", "Michal Douglas"),
                                new IntParam("age", 32)
                            )
                        )
                    )
                ).execute();
            }
        }
    }
}
