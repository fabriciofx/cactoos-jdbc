/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.param.BoolOf;
import com.github.fabriciofx.cactoos.jdbc.param.DateOf;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalOf;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import org.cactoos.text.Joined;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.IsText;

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
    void withoutValues() throws Exception {
        MatcherAssert.assertThat(
            "Can't build a named query without values",
            new QueryOf("SELECT * FROM employee"),
            new IsText("SELECT * FROM employee")
        );
    }

    @Test
    void valid() throws Exception {
        MatcherAssert.assertThat(
            "Can't build a simple named query",
            new QueryOf(
                "INSERT INTO foo2 (name) VALUES (:name)",
                new TextOf("name", "Yegor Bugayenko")
            ),
            new IsText("INSERT INTO foo2 (name) VALUES (?)")
        );
    }

    @Test
    void invalid() throws Exception {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
                new QueryOf(
                    "INSERT INTO foo2 (name) VALUES (:name)",
                    new TextOf("address", "Sunset Boulevard")
                ).asString();
            }
        );
    }

    @Test
    void manyValues() throws Exception {
        MatcherAssert.assertThat(
            "Can't build a named query with many values",
            new QueryOf(
                new Joined(
                    " ",
                    "INSERT INTO employee",
                    "(name, birthday, address, married, salary)",
                    "VALUES (:name, :birthday, :address, :married, :salary)"
                ),
                new TextOf("name", "John Wick"),
                new DateOf("birthday", "1980-08-16"),
                new TextOf("address", "Boulevard Street, 34"),
                new BoolOf("married", false),
                new DecimalOf("salary", "13456.00")
            ),
            new IsText(
                new Joined(
                    " ",
                    "INSERT INTO employee",
                    "(name, birthday, address, married, salary)",
                    "VALUES (?, ?, ?, ?, ?)"
                )
            )
        );
    }

    @Test
    void outOfOrder() throws Exception {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> {
                new QueryOf(
                    new Joined(
                        " ",
                        "INSERT INTO employee",
                        "(name, birthday, address, married, salary)",
                        "VALUES (:name, :birthday, :address, :married, :salary)"
                    ),
                    new TextOf("name", "John Wick"),
                    new DateOf("address", "1980-08-16"),
                    new TextOf("birthday", "Boulevard Street, 34"),
                    new BoolOf("married", false),
                    new DecimalOf("salary", "13456.00")
                ).asString();
            }
        );
    }
}
