/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.param.BoolOf;
import com.github.fabriciofx.cactoos.jdbc.param.DateOf;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalOf;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import org.cactoos.scalar.ScalarOf;
import org.cactoos.text.Concatenated;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;
import org.llorllale.cactoos.matchers.Matches;
import org.llorllale.cactoos.matchers.Throws;

/**
 * QuerySimple tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 */
final class QueryOfTest {
    @Test
    void withoutValues() {
        final String sql = "SELECT * FROM employee";
        new Assertion<>(
            "must build a query without values",
            () -> new QueryOf(sql).sql().parsed(),
            new IsText("SELECT * FROM employee")
        ).affirm();
    }

    @Test
    void valid() {
        new Assertion<>(
            "must build a simple query",
            () -> new QueryOf(
                "INSERT INTO foo2 (name) VALUES (:name)",
                new TextOf("name", "Yegor Bugayenko")
            ).sql().parsed(),
            new IsText("INSERT INTO foo2 (name) VALUES (?)")
        ).affirm();
    }

    @Test
    void invalid() {
        new Assertion<>(
            "must throws an exception if is an invalid query",
            new Throws<>(IllegalArgumentException.class),
            new Matches<>(
                new ScalarOf<>(
                    () -> new QueryOf(
                        "INSERT INTO foo2 (name) VALUES (:name)",
                        new TextOf("address", "Sunset Boulevard")
                    ).sql().parsed()
                )
            )
        ).affirm();
    }

    @Test
    void manyValues() {
        new Assertion<>(
            "must build a query with many values",
            () -> new QueryOf(
                new Concatenated(
                    "INSERT INTO employee (name, birthday, address, married, salary) ",
                    "VALUES (:name, :birthday, :address, :married, :salary)"
                ),
                new TextOf("name", "John Wick"),
                new DateOf("birthday", "1980-08-16"),
                new TextOf("address", "Boulevard Street, 34"),
                new BoolOf("married", false),
                new DecimalOf("salary", "13456.00")
            ).sql().parsed(),
            new IsText(
                new Concatenated(
                    "INSERT INTO employee (name, birthday, address, married, salary) ",
                    "VALUES (?, ?, ?, ?, ?)"
                )
            )
        ).affirm();
    }

    @Test
    void outOfOrder() {
        new Assertion<>(
            "must throws an exception if values are out of order in query",
            new Throws<>(IllegalArgumentException.class),
            new Matches<>(
                new ScalarOf<>(
                    () -> new QueryOf(
                        new Concatenated(
                            "INSERT INTO employee (name, birthday, address, married, salary) ",
                            "VALUES (:name, :birthday, :address, :married, :salary)"
                        ),
                        new TextOf("name", "John Wick"),
                        new DateOf("address", "1980-08-16"),
                        new TextOf("birthday", "Boulevard Street, 34"),
                        new BoolOf("married", false),
                        new DecimalOf("salary", "13456.00")
                    ).sql().parsed()
                )
            )
        ).affirm();
    }
}
