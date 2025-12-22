/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.result;

import com.github.fabriciofx.cactoos.jdbc.param.IntOf;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import java.sql.Connection;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * ResultSetAsValue tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.9.0
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class ResultSetAsValueTest {
    @Test
    void value() throws Exception {
        try (Server<DataSource> server = new H2Server()) {
            server.start();
            try (
                Connection connection = new NoAuth(server.resource()).connection()
            ) {
                new Update(
                    connection,
                    new QueryOf(
                        "CREATE TABLE contact (id INT AUTO_INCREMENT, name VARCHAR(50) NOT NULL, CONSTRAINT pk_contact PRIMARY KEY(id))"
                    )
                ).execute();
                new Insert(
                    connection,
                    new QueryOf(
                        "INSERT INTO contact (name) VALUES (?)",
                        new TextOf("name", "Joseph Klimber")
                    )
                ).execute();
                new Assertion<>(
                    "must generated key value",
                    new ResultSetAsValue<>(
                        new Select(
                            connection,
                            new QueryOf(
                                "SELECT name FROM contact WHERE id = :id",
                                new IntOf("id", 1)
                            )
                        )
                    ),
                    new HasValue<>("Joseph Klimber")
                ).affirm();
            }
        }
    }
}
