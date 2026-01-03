/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.query.Named;
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
 * Insert tests.
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class InsertTest {
    @Test
    void insert() throws Exception {
        try (Server<DataSource> server = new H2Server()) {
            server.start();
            try (Connexio connexio = new NoAuth(server.resource()).connexio()) {
                new Update(
                    connexio,
                    new QueryOf(
                        "CREATE TABLE t01 (id INT, name VARCHAR(50), PRIMARY KEY (id))"
                    )
                ).execute();
                new Assertion<>(
                    "must insert into table",
                    new ScalarOf<>(
                        () -> new Insert(
                            connexio,
                            new Named(
                                new QueryOf(
                                    "INSERT INTO t01 (id, name) VALUES (:id, :name)",
                                    new IntParam("id", 1),
                                    new TextParam("name", "Yegor Bugayenko")
                                )
                            )
                        ).execute()
                    ),
                    new HasValue<>(false)
                ).affirm();
            }
        }
    }
}
