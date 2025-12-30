/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.BoolOf;
import com.github.fabriciofx.cactoos.jdbc.param.DateOf;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalOf;
import com.github.fabriciofx.cactoos.jdbc.param.IntOf;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import com.github.fabriciofx.cactoos.jdbc.query.BatchedQuery;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.scalar.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.statement.Batch;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import java.sql.Connection;
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
            try (Connection connection = session.connection()) {
                new Update(
                    connection,
                    new QueryOf(
                        "CREATE TABLE person (id INT, name VARCHAR(30), created_at DATE, city VARCHAR(20), working BOOLEAN, height DECIMAL(20,2), PRIMARY KEY (id))"
                    )
                ).execute();
            }
            try (Connection connection = session.connection()) {
                new Batch(
                    connection,
                    new BatchedQuery(
                        "INSERT INTO person (id, name, created_at, city, working, height) VALUES (:id, :name, :created_at, :city, :working, :height)",
                        new ParamsOf(
                            new IntOf("id", 1),
                            new TextOf("name", "Rob Pike"),
                            new DateOf("created_at", LocalDate.now()),
                            new TextOf("city", "San Francisco"),
                            new BoolOf("working", true),
                            new DecimalOf("height", "1.86")
                        ),
                        new ParamsOf(
                            new IntOf("id", 2),
                            new TextOf("name", "Ana Pivot"),
                            new DateOf("created_at", LocalDate.now()),
                            new TextOf("city", "Washington"),
                            new BoolOf("working", false),
                            new DecimalOf("height", "1.62")
                        )
                    )
                ).execute();
            }
            try (Connection connection = session.connection()) {
                new Assertion<>(
                    "must select a person name",
                    new ResultSetAsValue<>(
                        new Select(
                            connection,
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
