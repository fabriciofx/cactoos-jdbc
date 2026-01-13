/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.param.BoolParam;
import com.github.fabriciofx.cactoos.jdbc.param.DateParam;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalParam;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import org.cactoos.scalar.ScalarOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;
import org.llorllale.cactoos.matchers.Matches;
import org.llorllale.cactoos.matchers.Throws;

/**
 * NamedQuery tests.
 *
 * @since 0.9.0
 */
final class NamedQueryTest {
    @Test
    void valid() {
        new Assertion<>(
            "must build a simple query",
            () -> new NamedQuery(
                "INSERT INTO foo2 (name) VALUES (:name)",
                new TextParam("name", "Yegor Bugayenko")
            ).sql(),
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
                    () -> new NamedQuery(
                        "INSERT INTO foo2 (name) VALUES (:name)",
                        new TextParam("address", "Sunset Boulevard")
                    ).sql()
                )
            )
        ).affirm();
    }

    @Test
    void manyValues() {
        new Assertion<>(
            "must build a query with many values",
            () -> new NamedQuery(
                """
                INSERT INTO employee (name, birthday, address, married, salary)
                VALUES (:name, :birthday, :address, :married, :salary)
                """,
                new TextParam("name", "John Wick"),
                new DateParam("birthday", "1980-08-16"),
                new TextParam("address", "Boulevard Street, 34"),
                new BoolParam("married", false),
                new DecimalParam("salary", "13456.00")
            ).sql(),
            new IsText(
                """
                INSERT INTO employee (name, birthday, address, married, salary)
                VALUES (?, ?, ?, ?, ?)
                """
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
                    () -> new NamedQuery(
                        """
                        INSERT INTO employee (name, birthday, address, married,
                        salary) VALUES (:name, :birthday, :address, :married,
                        :salary)
                        """,
                        new TextParam("name", "John Wick"),
                        new DateParam("address", "1980-08-16"),
                        new TextParam("birthday", "Boulevard Street, 34"),
                        new BoolParam("married", false),
                        new DecimalParam("salary", "13456.00")
                    ).sql()
                )
            )
        ).affirm();
    }

    @Test
    void named() {
        new Assertion<>(
            "must parse a query for all columns",
            () -> new NamedQuery(
                """
                INSERT INTO employee (id, name, birthday, address, married,
                salary) VALUES (:id, :name, :birthday, :address, :married, \
                :salary)
                """,
                new ParamsOf(
                    new IntParam("id", 1),
                    new TextParam("name", "John Wick"),
                    new DateParam("birthday", "1980-08-15"),
                    new TextParam("address", "Boulevard Street, 34"),
                    new BoolParam("married", false),
                    new DecimalParam("salary", "13456.00")
                )
            ).sql(),
            new IsText(
                """
                INSERT INTO employee (id, name, birthday, address, married,
                salary) VALUES (?, ?, ?, ?, ?, ?)
                """
            )
        ).affirm();
    }
}
