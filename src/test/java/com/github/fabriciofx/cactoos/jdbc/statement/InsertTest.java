/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.param.IntOf;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsValue;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import java.sql.Connection;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Insert tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class InsertTest {
    @Test
    void insert() throws Exception {
        try (Server<DataSource> server = new H2Server()) {
            server.start();
            try (
                Connection connection = new NoAuth(server.resource()).connection()
            ) {
                new Update(
                    connection,
                    new QueryOf(
                        "CREATE TABLE t01 (id INT, name VARCHAR(50), PRIMARY KEY (id))"
                    )
                ).execute();
                new Assertion<>(
                    "must insert into table",
                    new ResultAsValue<>(
                        new Insert(
                            connection,
                            new QueryOf(
                                "INSERT INTO t01 (id, name) VALUES (:id, :name)",
                                new IntOf("id", 1),
                                new TextOf("name", "Yegor Bugayenko")
                            )
                        )
                    ),
                    new HasValue<>(false)
                ).affirm();
            }
        }
    }
}
