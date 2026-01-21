/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.source;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.param.BoolParam;
import com.github.fabriciofx.cactoos.jdbc.param.DateParam;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalParam;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.scalar.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.statement.Batch;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.server.RandomName;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Pooled tests.
 *
 * @since 0.9.0
 */
@SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
final class PooledTest {
    @Test
    void pooled() throws Exception {
        final Source source = new Pooled(
            new NoAuth(
                new H2Source(
                    new RandomName().asString()
                )
            )
        );
        try (Session session = source.session()) {
            new Update(
                session,
                new QueryOf(
                    """
                    CREATE TABLE person (id INT, name VARCHAR(30), created_at
                    DATE, city VARCHAR(20), working BOOLEAN, height
                    DECIMAL(20,2), PRIMARY KEY (id))
                    """
                )
            ).execute();
        }
        try (Session session = source.session()) {
            new Batch(
                session,
                new NamedQuery(
                    """
                    INSERT INTO person (id, name, created_at, city, working,
                    height) VALUES (:id, :name, :created_at, :city, :working,
                    :height)
                    """,
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
        try (Session session = source.session()) {
            new Assertion<>(
                "must select a person name",
                new ResultSetAsValue<>(
                    new Select(
                        session,
                        new QueryOf("SELECT name FROM person")
                    )
                ),
                new HasValue<>("Rob Pike")
            ).affirm();
        }
    }
}
