/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsValue;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.fake.server.Servers;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import com.github.fabriciofx.fake.server.db.server.MysqlServer;
import com.github.fabriciofx.fake.server.db.server.PgsqlServer;
import javax.sql.DataSource;
import org.cactoos.text.Joined;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Update tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class UpdateTest {
    @Test
    void createTable() throws Exception {
        try (
            Servers<DataSource> servers = new Servers<>(
                new H2Server(),
                new MysqlServer(),
                new PgsqlServer()
            )
        ) {
            for (final DataSource source : servers.resources()) {
                final Session session = new NoAuth(source);
                new Assertion<>(
                    "must create a table",
                    new ResultAsValue<>(
                        new Update(
                            session,
                            new QueryOf(
                                new Joined(
                                    " ",
                                    "CREATE TABLE foo1 (id INT,",
                                    "name VARCHAR(50), PRIMARY KEY (id))"
                                )
                            )
                        )
                    ),
                    new HasValue<>(0)
                ).affirm();
            }
        }
    }
}
