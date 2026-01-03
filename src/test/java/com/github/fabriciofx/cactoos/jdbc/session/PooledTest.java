/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.BoolParam;
import com.github.fabriciofx.cactoos.jdbc.param.DateParam;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalParam;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.scalar.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.statement.Batch;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import java.time.LocalDate;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Pooled tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.9.0
 * @checkstyle JavadocMethodCheck (500 lines)
 */
final class PooledTest {
    @Test
    void pooled() throws Exception {
        try (Server<DataSource> server = new H2Server()) {
            server.start();
            final Session session = new Pooled(new NoAuth(server.resource()));
            try (Connexio connexio = session.connexio()) {
                new Update(
                    connexio,
                    new QueryOf(
                        "CREATE TABLE person (id INT, name VARCHAR(30), created_at DATE, city VARCHAR(20), working BOOLEAN, height DECIMAL(20,2), PRIMARY KEY (id))"
                    )
                ).execute();
            }
            try (Connexio connexio = session.connexio()) {
                new Batch(
                    connexio,
                    new QueryOf(
                        "INSERT INTO person (id, name, created_at, city, working, height) VALUES (:id, :name, :created_at, :city, :working, :height)",
                        new ParamsOf(
                            new IntParam("id", 1),
                            new TextParam("name", "Rob Pike"),
                            new DateParam("created_at", LocalDate.now()),
                            new TextParam("city", "San Francisco"),
                            new BoolParam("working", true),
                            new DecimalParam("height", "1.86")
                        ),
                        new ParamsOf(
                            new IntParam("id", 2),
                            new TextParam("name", "Ana Pivot"),
                            new DateParam("created_at", LocalDate.now()),
                            new TextParam("city", "Washington"),
                            new BoolParam("working", false),
                            new DecimalParam("height", "1.62")
                        )
                    )
                ).execute();
            }
            try (Connexio connexio = session.connexio()) {
                new Assertion<>(
                    "must select a person name",
                    new ResultSetAsValue<>(
                        new Select(
                            connexio,
                            new QueryOf(
                                "SELECT name FROM person"
                            )
                        )
                    ),
                    new HasValue<>("Rob Pike")
                ).affirm();
            }
        }
    }
}
