/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.BoolOf;
import com.github.fabriciofx.cactoos.jdbc.param.DateOf;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalOf;
import com.github.fabriciofx.cactoos.jdbc.param.IntOf;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import java.sql.ResultSet;
import java.time.LocalDate;
import javax.sql.DataSource;
import org.junit.jupiter.api.Disabled;
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
    @Disabled
    @Test
    void cacheASelect() throws Exception {
        final String name = "Rob Pike";
        final String city = "San Francisco";
        try (Server<DataSource> server = new H2Server()) {
            server.start();
            final Session session = new Cached(new NoAuth(server.resource()));
            try (Connexio connexio = session.connexio()) {
                new Update(
                    connexio,
                    new QueryOf(
                        "CREATE TABLE person (id INT, name VARCHAR(30), created_at DATE, city VARCHAR(20), working BOOLEAN, height DECIMAL(20,2), PRIMARY KEY (id))"
                    )
                ).execute();
                new Insert(
                    connexio,
                    new QueryOf(
                        "INSERT INTO person (id, name, created_at, city, working, height) VALUES (:id, :name, :created_at, :city, :working, :height)",
                        new IntOf("id", 1),
                        new TextOf("name", name),
                        new DateOf("created_at", LocalDate.now()),
                        new TextOf("city", city),
                        new BoolOf("working", true),
                        new DecimalOf("height", "1.86")
                    )
                ).execute();
                try (
                    ResultSet rset = new Select(
                        connexio,
                        new QueryOf("SELECT name FROM person WHERE id = :id", new IntOf("id", 1))
                    ).execute()
                ) {
                    new Assertion<>(
                        "must have the name",
                        rset.next(),
                        new IsTrue()
                    ).affirm();
                    new Assertion<>(
                        "must have the correct name",
                        new org.cactoos.text.TextOf(rset.getString("name")),
                        new IsText(name)
                    ).affirm();
                }
                try (
                    ResultSet rset = new Select(
                        connexio,
                        new QueryOf("SELECT city FROM person WHERE id = :id", new IntOf("id", 1))
                    ).execute()
                ) {
                    new Assertion<>(
                        "must have the city",
                        rset.next(),
                        new IsTrue()
                    ).affirm();
                    new Assertion<>(
                        "must have the correct city",
                        new org.cactoos.text.TextOf(rset.getString("city")),
                        new IsText(city)
                    ).affirm();
                }
            }
        }
    }
}
