/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import javax.sql.DataSource;
import org.cactoos.scalar.ScalarOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Update tests.
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class UpdateTest {
    @Test
    void createTable() throws Exception {
        try (Server<DataSource> server = new H2Server()) {
            server.start();
            try (Connexio connexio = new NoAuth(server.resource()).connexio()) {
                new Assertion<>(
                    "must create a table",
                    new ScalarOf<>(
                        () -> new Update(
                            connexio,
                            new QueryOf(
                                "CREATE TABLE foo1 (id INT, name VARCHAR(50), PRIMARY KEY (id))"
                            )
                        ).execute()
                    ),
                    new HasValue<>(0)
                ).affirm();
            }
        }
    }
}
