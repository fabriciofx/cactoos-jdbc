/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
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
import com.github.fabriciofx.cactoos.jdbc.query.Named;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import java.sql.ResultSet;
import java.time.LocalDate;
import javax.sql.DataSource;
import org.cactoos.text.Concatenated;
import org.cactoos.text.TextOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;
import org.llorllale.cactoos.matchers.IsTrue;

/**
 * Cached tests.
 *
 * @since 0.9.0
 * @checkstyle NestedTryDepthCheck (500 lines)
 */
final class CachedTest {
    @Test
    void cacheASelect() throws Exception {
        final String name = "Rob Pike";
        final String city = "San Francisco";
        try (Server<DataSource> server = new H2Server()) {
            server.start();
            final Source source = new Cached(new NoAuth(server.resource()));
            try (Session session = source.session()) {
                new Update(
                    session,
                    new QueryOf(
                        new Concatenated(
                            "CREATE TABLE person (id INT, name VARCHAR(30), ",
                            "created_at DATE, city VARCHAR(20), working ",
                            "BOOLEAN, height DECIMAL(20,2), PRIMARY KEY (id))"
                        )
                    )
                ).execute();
                new Insert(
                    session,
                    new Named(
                        new QueryOf(
                            new Concatenated(
                                "INSERT INTO person (id, name, created_at, ",
                                "city, working, height) VALUES (:id, :name, ",
                                ":created_at, :city, :working, :height)"
                            ),
                            new IntParam("id", 1),
                            new TextParam("name", name),
                            new DateParam("created_at", LocalDate.now()),
                            new TextParam("city", city),
                            new BoolParam("working", true),
                            new DecimalParam("height", "1.86")
                        )
                    )
                ).execute();
                try (
                    ResultSet rset = new Select(
                        session,
                        new Named(
                            new QueryOf(
                                "SELECT name FROM person WHERE id = :id",
                                new IntParam("id", 1)
                            )
                        )
                    ).execute()
                ) {
                    new Assertion<>(
                        "must have the name",
                        rset.next(),
                        new IsTrue()
                    ).affirm();
                    new Assertion<>(
                        "must have the correct name",
                        new TextOf(rset.getString("name")),
                        new IsText(name)
                    ).affirm();
                }
                try (
                    ResultSet rset = new Select(
                        session,
                        new Named(
                            new QueryOf(
                                "SELECT city FROM person WHERE id = :id",
                                new IntParam("id", 1)
                            )
                        )
                    ).execute()
                ) {
                    new Assertion<>(
                        "must have the city",
                        rset.next(),
                        new IsTrue()
                    ).affirm();
                    new Assertion<>(
                        "must have the correct city",
                        new TextOf(rset.getString("city")),
                        new IsText(city)
                    ).affirm();
                }
            }
        }
    }
}
