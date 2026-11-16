/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.scalar;

import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.fake.server.RandomName;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * {@link HasViews} tests.
 *
 * @since 0.9.0
 */
final class HasViewsTest {

    @Test
    void findsViewAmongTables() throws Exception {
        try (
            Connection connection = DriverManager.getConnection(
                String.format("jdbc:h2:mem:%s", new RandomName().asString())
            );
            Statement stmt = connection.createStatement()
        ) {
            stmt.execute("CREATE TABLE pilot (id INT, name VARCHAR(30))");
            stmt.execute("CREATE VIEW crew AS SELECT name FROM pilot");
            new Assertion<>(
                "must find a view among the tables of a query",
                new HasViews(
                    connection,
                    new TableNames(
                        new QueryOf(
                            """
                            SELECT p.id FROM pilot p JOIN crew c
                            ON p.name = c.name
                            """
                        )
                    )
                ),
                new HasValue<>(true)
            ).affirm();
        }
    }

    @Test
    void dontFindViewAmongPlainTables() throws Exception {
        try (
            Connection connection = DriverManager.getConnection(
                String.format("jdbc:h2:mem:%s", new RandomName().asString())
            );
            Statement stmt = connection.createStatement()
        ) {
            stmt.execute("CREATE TABLE ship (id INT, model VARCHAR(30))");
            stmt.execute("CREATE VIEW fleet AS SELECT model FROM ship");
            new Assertion<>(
                "must not find a view when a query reads only tables",
                new HasViews(
                    connection,
                    new TableNames(new QueryOf("SELECT model FROM ship"))
                ),
                new HasValue<>(false)
            ).affirm();
        }
    }
}
