/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.result;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.query.Named;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.scalar.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.source.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * ResultSetAsValue tests.
 * @since 0.9.0
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class ResultSetAsValueTest {
    @Test
    void value() throws Exception {
        try (Server<DataSource> server = new H2Server()) {
            server.start();
            try (Connexio connexio = new NoAuth(server.resource()).connexio()) {
                new Update(
                    connexio,
                    new QueryOf(
                        "CREATE TABLE contact (id INT AUTO_INCREMENT, name VARCHAR(50) NOT NULL, CONSTRAINT pk_contact PRIMARY KEY(id))"
                    )
                ).execute();
                new Insert(
                    connexio,
                    new Named(
                        new QueryOf(
                            "INSERT INTO contact (name) VALUES (:name)",
                            new TextParam("name", "Joseph Klimber")
                        )
                    )
                ).execute();
                new Assertion<>(
                    "must generated key value",
                    new ResultSetAsValue<>(
                        new Select(
                            connexio,
                            new Named(
                                new QueryOf(
                                    "SELECT name FROM contact WHERE id = :id",
                                    new IntParam("id", 1)
                                )
                            )
                        )
                    ),
                    new HasValue<>("Joseph Klimber")
                ).affirm();
            }
        }
    }
}
