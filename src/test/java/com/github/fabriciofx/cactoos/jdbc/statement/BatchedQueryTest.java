/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.param.IntOf;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;

/**
 * BatchedQuery tests.
 *
 * @since 0.9.0
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class BatchedQueryTest {
    @Test
    void batch() throws Exception {
        try (Server<DataSource> server = new H2Server()) {
            server.start();
            try (Connexio connexio = new NoAuth(server.resource()).connexio()) {
                new Update(
                    connexio,
                    new QueryOf(
                        "CREATE TABLE client (id INT, name VARCHAR(50), age INT, PRIMARY KEY (id))"
                    )
                ).execute();
                new Batch(
                    connexio,
                    new QueryOf(
                        "INSERT INTO client (id, name, age) VALUES (:id, :name, :age)",
                        new ParamsOf(
                            new IntOf("id", 1),
                            new TextOf("name", "Jeff Bridges"),
                            // @checkstyle MagicNumber (1 line)
                            new IntOf("age", 34)
                        ),
                        new ParamsOf(
                            new IntOf("id", 2),
                            new TextOf("name", "Anna Miller"),
                            // @checkstyle MagicNumber (1 line)
                            new IntOf("age", 26)
                        ),
                        new ParamsOf(
                            // @checkstyle MagicNumber (3 lines)
                            new IntOf("id", 3),
                            new TextOf("name", "Michal Douglas"),
                            new IntOf("age", 32)
                        )
                    )
                ).execute();
            }
        }
    }
}
