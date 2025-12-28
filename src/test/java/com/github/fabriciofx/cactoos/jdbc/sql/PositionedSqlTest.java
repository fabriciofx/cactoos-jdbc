/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql;

import com.github.fabriciofx.cactoos.jdbc.param.BoolOf;
import com.github.fabriciofx.cactoos.jdbc.param.DateOf;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalOf;
import com.github.fabriciofx.cactoos.jdbc.param.IntOf;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import org.cactoos.text.Concatenated;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasString;

/**
 * Positioned SQL tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.9.0
 * @checkstyle JavadocMethodCheck (500 lines)
 */
final class PositionedSqlTest {
    @Test
    void parse() {
        new Assertion<>(
            "must parse a query for all columns",
            () -> new PositionedSql(
                new Concatenated(
                    "INSERT INTO employee (id, name, birthday, address, married, salary) ",
                    "VALUES (:id, :name, :birthday, :address, :married, :salary)"
                ),
                new ParamsOf(
                    new IntOf("id", 1),
                    new TextOf("name", "John Wick"),
                    new DateOf("birthday", "1980-08-15"),
                    new TextOf("address", "Boulevard Street, 34"),
                    new BoolOf("married", false),
                    new DecimalOf("salary", "13456.00")
                )
            ).parse(),
            new HasString(
                new Concatenated(
                    "INSERT INTO employee (id, name, birthday, address, married, salary) ",
                    "VALUES (?, ?, ?, ?, ?, ?)"
                )
            )
        ).affirm();
    }
}
